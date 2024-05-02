using Confluent.Kafka;
using DC_REST.DTOs.Request;
using DC_REST.DTOs.Response;
using DC_REST.Services.Interfaces;
using Newtonsoft.Json;
using Publisher.Services.Brokers;

namespace DC_REST.Services.Brokers
{
	public class Producer : INoteService
	{
		private ConsumerConfig config = new ConsumerConfig
		{
			BootstrapServers = "localhost:9092",
			GroupId = "Kafka",
			AutoOffsetReset = AutoOffsetReset.Earliest
		};

		public NoteResponseTo CreateNote(NoteRequestTo noteRequestTo)
		{
			var rand = new Random();
			noteRequestTo.Id = rand.Next();
			Produce(noteRequestTo.IssueId.ToString(), "create", JsonConvert.SerializeObject(noteRequestTo));
			var res = new NoteResponseTo()
			{
				Content = noteRequestTo.Content,
				Id = noteRequestTo.Id,
				IssueId = noteRequestTo.IssueId,
			};
			return res;
		}

		public bool DeleteNote(int id)
		{
			var data = new Dictionary<string, int>(){
				{ "id", id }
			};
			Produce("extra", "delete", JsonConvert.SerializeObject(data));
			var resString = Consume();
			bool res;
			if (resString != null)
			{
				var temp = JsonConvert.DeserializeObject<Message>(resString);
				res = bool.Parse(temp.Data);
			}
			else
				throw new Exception("tested");
			return true; 
		}

		public NoteResponseTo GetNoteById(int id)
		{
			var data = new Dictionary<string, int>(){
				{ "id", id }
			};
			Produce("extra", "get", JsonConvert.SerializeObject(data));
			var resString = Consume();
			NoteResponseTo res;
			if (resString != null)
			{
				var temp = JsonConvert.DeserializeObject<Message>(resString);
				res = JsonConvert.DeserializeObject<NoteResponseTo>(temp.Data);
			}
			else
				throw new Exception("tested");
			return res;
		}

		public List<NoteResponseTo> GetAllNotes()
		{
			Produce("extra", "getAll", "");
			var resString = Consume();
			List<NoteResponseTo> res;
			if (resString != null)
			{
				var temp = JsonConvert.DeserializeObject<Message>(resString);
				res = JsonConvert.DeserializeObject<List<NoteResponseTo>>(temp.Data);
			
			}
			else
				throw new Exception("tested");
			return res;
		}

		public NoteResponseTo UpdateNote(int id, NoteRequestTo noteRequestDto)
		{
			var key = noteRequestDto.IssueId != null ? noteRequestDto.IssueId.ToString().ToString() : "extra";
			Produce(key, "update", JsonConvert.SerializeObject(noteRequestDto));
			var resString = Consume();
			NoteResponseTo res;
			if (resString != null)
			{
				var temp = JsonConvert.DeserializeObject<Message>(resString);
				res = JsonConvert.DeserializeObject<NoteResponseTo>(temp.Data);
			}
			else
				throw new Exception("tested");
			return res;
		}

		private string? Consume()
		{
			using (var consumer = new ConsumerBuilder<Ignore, string>(config).Build())
			{
				consumer.Subscribe("OutTopic");
				try
				{
					var consumeResult = consumer.Consume();
					return consumeResult.Value;
				}
				finally
				{
					consumer.Close();
				}
			}
		}

		private void Produce(string key, string command, string data)
		{
			Message message = new Message();
			message.Command = command;
			message.Data = data;
			using (var producer = new ProducerBuilder<string, string>(config).Build())
			{
				producer.Produce("InTopic", new Message<string, string>
				{
					Value = JsonConvert.SerializeObject(message),
					Key = key,
				});
				producer.Flush();
			}
		}
	}
}