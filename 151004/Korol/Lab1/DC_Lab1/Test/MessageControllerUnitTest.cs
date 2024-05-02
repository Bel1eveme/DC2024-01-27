using DC_Lab1.DTO.Interface;

namespace Test
{
    [TestClass]
    public class MessageControllerUnitTest
    {
        private readonly MessageController _controller;
        private readonly MessagesService _service;
        private readonly DC_Lab1.DB.SqLiteDbContext _dbContext;
        private readonly IMapper _mapper = new MapperConfiguration(cfg =>
        {
            cfg.AddMaps(["DC_Lab1"]);
        }).CreateMapper();

        private StoryResponseTo Story = null!;
        public MessageControllerUnitTest()
        {
            _dbContext = new DC_Lab1.DB.SqLiteDbContext();
            _service = new MessagesService(_mapper, _dbContext);
            _controller = new MessageController(_service)
            { ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() } };
        }

        [TestInitialize]
        public async Task TestInitialize()
        {

            var AuthorService = new AuthorService(_mapper, _dbContext);
            var AuthorController = new AuthorController(AuthorService)
            {
                ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() }
            };

            var Authors = (AuthorController.GetAuthors().Value as IEnumerable<AuthorResponseTo>)!;
            AuthorResponseTo Author;
            if (Authors.Count() == 0)
            {
                Author = ((await AuthorController.CreateAuthor(new(0, "TestAuthor", "TestPassword", "TestFirstName", "TestLastName"))).Value as AuthorResponseTo)!;
            }
            else
            {
                Author = Authors.FirstOrDefault()!;
            }

            var StoryService = new StoryService(_mapper, _dbContext);
            var StoryController = new StoryController(StoryService)
            {
                ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() }
            };
            var StoryList = (StoryController.GetStorys().Value as IEnumerable<StoryResponseTo>)!;
            if (StoryList.Count() == 0)
            {
                Story = ((await StoryController.CreateStory(new(0, Author!.Id, "MessageTestTitle", "content"))).Value as StoryResponseTo)!;
            }
            else
            {
                Story = StoryList.FirstOrDefault()!;
            }
            _dbContext.ChangeTracker.Clear();
        }

        [TestMethod]
        public void GetUpdate()
        {
            var MessageRequest = new MessageRequestTo(0, Story.Id,  "TestContent");
            var Message = _controller.CreateMessage(MessageRequest).Result.Value as MessageResponseTo;
            Assert.IsNotNull(Message);

            var newMessage = new MessageRequestTo(Message.Id, Story.Id,  "newTestContent");
            _dbContext.ChangeTracker.Clear();
            var MessageResponce = _controller.UpdateMessage(newMessage).Result.Value as MessageResponseTo;
            Assert.IsNotNull(MessageResponce);
            Assert.AreEqual(MessageResponce.Content, newMessage.Content);
        }

        [TestMethod]
        public void GetEntById()
        {
            var MessageRequest = new MessageRequestTo(0, Story.Id, "TestContent");
            var Message = _controller.CreateMessage(MessageRequest).Result.Value as MessageResponseTo;
            Assert.IsNotNull(Message);
            _dbContext.ChangeTracker.Clear();
            var foundedMessage = _controller.GetMessageById(Message.Id).Result.Value as MessageResponseTo;
            Assert.IsNotNull(foundedMessage);
            Assert.AreEqual(foundedMessage.Id, Message.Id);
        }

        [TestMethod]
        public void GetAllEnt()
        {
            var result = _controller.GetMessages();

            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result.Value, typeof(IEnumerable<IResponseTo>));
        }

        [TestMethod]
        public async Task CreateEnt()
        {

            var MessageRequest = new MessageRequestTo(0, Story.Id,  "TestContent");
            var MessageResponse = (await _controller.CreateMessage(MessageRequest)).Value as MessageResponseTo;
            Assert.IsNotNull(MessageResponse);

            Assert.AreEqual(MessageResponse.Content, "TestContent");
        }

        [TestMethod]
        public async Task DeleteEnt()
        {
            var MessageRequest = new MessageRequestTo(0, Story.Id,"TestContent");
            var MessageResponse = (await _controller.CreateMessage(MessageRequest)).Value as MessageResponseTo;
            Assert.IsNotNull(MessageResponse);

            _dbContext.ChangeTracker.Clear();

            var result = await _controller.DeleteMessage(MessageResponse.Id);

            var expected = typeof(NoContentResult);

            Assert.IsInstanceOfType(result, expected);
        }

    }
}

