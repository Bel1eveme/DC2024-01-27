using AutoMapper;
using Confluent.Kafka;
using Discussion.DTOs.Request;
using Discussion.Entities;
using Discussion.Repositories;
using Discussion.Services.Interfaces;
using Newtonsoft.Json;

namespace Discussion.Services.Brokers
{
	public class Consumer:IConsumer
	{
		private INoteService _noteService;
		private readonly IMapper _mapper;

		private ConsumerConfig config = new ConsumerConfig
		{
			BootstrapServers = "localhost:9092",
			GroupId = "Kafka",
		};

		public Consumer(INoteService noteService, IMapper mapper)
		{
			_noteService = noteService;
			_mapper = mapper;
		}

		public void StartConsuming()
		{
			CancellationTokenSource cts = new CancellationTokenSource();
			using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
			{
				consumer.Subscribe("InTopic");
				try
				{
					while (true)
					{
						var consumeResult = consumer.Consume(cts.Token);
						if (consumeResult != null)
						{
							ProcessMessage(consumeResult);
						}
					}
				}
				finally
				{
					consumer.Close();
				}
			}
		}

		private void ProcessMessage(ConsumeResult<Ignore, string> _message)
		{
			var message = JsonConvert.DeserializeObject<Message>(_message.Value);
			switch (message.Command)
			{
				case "create":
					{
						var data = JsonConvert.DeserializeObject<Note>(message.Data);
						var note = new Note {Id = data.Id, Content = data.Content, IssueId = data.IssueId};
						var noteRequestDto = _mapper.Map<NoteRequestTo>(note);
						_noteService.CreateNote(noteRequestDto);
						break;
					}
				case "get":
					{
						var data = JsonConvert.DeserializeObject<Dictionary<string, int>>(message.Data);
						var res = _noteService.GetNoteById(data["id"]);
						try
						{
							Produce(res.IssueId.ToString(), "get_response", JsonConvert.SerializeObject(res)); 
						}
						catch (Exception ex) { }
						break;
					}
				case "getAll":
					{
						var res = _noteService.GetAllNotes();
						Produce("extra", "getAll_response", JsonConvert.SerializeObject(res));
						break;
					}
				case "update":
					{
						var data = JsonConvert.DeserializeObject<Note>(message.Data);
						var note = new Note { Id = data.Id, Content = data.Content, IssueId = data.IssueId };
						var noteRequestDto = _mapper.Map<NoteRequestTo>(note);
						var res = _noteService.UpdateNote(data.Id,noteRequestDto);
						//try
						if (res != null)
						{
							Produce(res.IssueId.ToString(), "update_response", JsonConvert.SerializeObject(res));
						}
						//catch (Exception ex) { }
						break;
					}
				case "delete":
					{
						var data = JsonConvert.DeserializeObject<Dictionary<string, int>>(message.Data);
						var res = _noteService.DeleteNote(data["id"]);
						Produce("extra", "delete_response", JsonConvert.SerializeObject(res));
						break;
					}
			}
		}

		private void Produce(string key, string command, string data)
		{
			var message = new Message();
			message.Command = command;
			message.Data = data;
			using (var producer = new ProducerBuilder<string, string>(config).Build())
			{
				producer.Produce("OutTopic", new Message<string, string>
				{
					Value = JsonConvert.SerializeObject(message),
					Key = key,
				});
				producer.Flush();
			}
		}
	}
}
