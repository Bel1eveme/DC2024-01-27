using DC_Lab1.DB;
using DC_Lab1.DTO.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test
{
    [TestClass]
    public class StoryControllerUnitTest
    {
        private readonly StoryController _controller;
        private readonly StoryService _service;
        private readonly PostgreSQlDbContext _dbContext;
        private readonly IMapper _mapper = new MapperConfiguration(cfg =>
        {
            cfg.AddMaps(["DC_Lab1"]);
        }).CreateMapper();

        private AuthorResponseTo Author = null!;
        public StoryControllerUnitTest()
        {
            _dbContext = new PostgreSQlDbContext();
            _service = new StoryService(_mapper, _dbContext);
            _controller = new StoryController(_service)
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

            if ( Authors.Count() == 0)
            {
                Author = ((await AuthorController.CreateAuthor(new(0, "TestAuthor", "TestPassword", "TestFirstName", "TestLastName"))).Value as AuthorResponseTo)!;
            }
            else
            {
                Author = Authors.FirstOrDefault()!;
            }

            _dbContext.ChangeTracker.Clear();
        }

        [TestMethod]
        public void GetUpdate()
        {
            var StoryRequest = new StoryRequestTo(0, Author.Id, "testTitle", "TestContent");
            var Story = _controller.CreateStory(StoryRequest).Result.Value as StoryResponseTo;
            Assert.IsNotNull(Story);

            var newStory = new StoryRequestTo(Story.Id, Author.Id, "newtestTitle", "newTestContent");
            _dbContext.ChangeTracker.Clear();
            var StoryResponce = _controller.UpdateStory(newStory).Result.Value as StoryResponseTo;
            Assert.IsNotNull(StoryResponce);
            Assert.AreEqual(StoryResponce.Title, newStory.Title);
        }

        [TestMethod]
        public void GetEntById()
        {
            var StoryRequest = new StoryRequestTo(0, Author.Id, "testTitle", "TestContent");
            var Story = _controller.CreateStory(StoryRequest).Result.Value as StoryResponseTo;
            Assert.IsNotNull(Story);
            _dbContext.ChangeTracker.Clear();
            var foundedStory = _controller.GetStoryById(Story.Id).Result.Value as StoryResponseTo;
            Assert.IsNotNull(foundedStory);
            Assert.AreEqual(foundedStory.Id, Story.Id);
        }

        [TestMethod]
        public void GetAllEnt()
        {
            var result = _controller.GetStorys();

            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result.Value, typeof(IEnumerable<IResponseTo>));
        }

        [TestMethod]
        public async Task CreateEnt()
        {
            var StoryRequest = new StoryRequestTo(0, Author.Id, "testTitle", "TestContent");
            var StoryResponse = (await _controller.CreateStory(StoryRequest)).Value as StoryResponseTo;
            Assert.IsNotNull(StoryResponse);
            Assert.AreEqual(StoryResponse.Title, "testTitle");
        }

        [TestMethod]
        public async Task DeleteEnt()
        {
            var StoryRequest = new StoryRequestTo(0, Author.Id, "testTitle", "TestContent");
            var StoryResponse = (await _controller.CreateStory(StoryRequest)).Value as StoryResponseTo;
            Assert.IsNotNull(StoryResponse);

            _dbContext.ChangeTracker.Clear();

            var result = await _controller.DeleteStory(StoryResponse.Id);

            var expected = typeof(NoContentResult);

            Assert.IsInstanceOfType(result, expected);
        }

    }
}
