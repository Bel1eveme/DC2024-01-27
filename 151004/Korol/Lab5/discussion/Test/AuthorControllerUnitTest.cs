using AutoMapper;
using DC_Lab1.DB;
using DC_Lab1.DTO.Interface;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Migrations.Operations;

namespace Test
{
    [TestClass]
    public class AuthorControllerUnitTest
    {

        private readonly AuthorController _controller;
        private readonly AuthorService _service;
        private readonly PostgreSQlDbContext _dbContext;
        private readonly IMapper _mapper = new MapperConfiguration(cfg =>
        {
            cfg.AddMaps(["DC_Lab2"]);
        }).CreateMapper();
        public AuthorControllerUnitTest()
        { 
            _dbContext = new PostgreSQlDbContext();
            _service = new AuthorService(_mapper, _dbContext);
            _controller = new AuthorController(_service)
            { ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() } };
        }

        [TestMethod]
        public void GetUpdate()
        {
            var AuthorRequest = new AuthorRequestTo(0,"testLogin", "password", "fname", "lname");
            var Author = _controller.CreateAuthor(AuthorRequest).Result.Value as AuthorResponseTo;
            Assert.IsNotNull(Author);

            var newAuthor = new AuthorRequestTo(Author.Id,"newTestLogin", "newPassword", "newFname", "newLname");
            _dbContext.ChangeTracker.Clear();
            var AuthorResponce = _controller.UpdateAuthor(newAuthor).Result.Value as AuthorResponseTo;
            Assert.IsNotNull(AuthorResponce);
            Assert.AreEqual(AuthorResponce.Login, newAuthor.Login);
        }
        [TestMethod]
        public void GetEntById()
        {
            var AuthorRequest = new AuthorRequestTo(0,"testLogin", "password", "fname", "lname");
            var Author = _controller.CreateAuthor(AuthorRequest).Result.Value as AuthorResponseTo;
            Assert.IsNotNull(Author);
            _dbContext.ChangeTracker.Clear();
            var foundedAuthor = _controller.GetAuthorById(Author.Id).Result.Value as AuthorResponseTo;
            Assert.IsNotNull(foundedAuthor);
            Assert.AreEqual(foundedAuthor.Id, Author.Id);
        }
        [TestMethod]
        public async Task GetAllEnt()
        {
            
            var result = _controller.GetAuthors();

            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result.Value, typeof(IEnumerable<IResponseTo>));
        }
        [TestMethod]
        public async Task CreateEnt()
        {
            var AuthorRequest = new AuthorRequestTo(0,"testLogin", "password", "fname", "lname");
            var AuthorResponse = (await _controller.CreateAuthor(AuthorRequest)).Value as AuthorResponseTo;
            Assert.IsNotNull(AuthorResponse);

            Assert.AreEqual(AuthorResponse.Login, "testLogin");
        }
        [TestMethod]
        public async Task DeleteEnt()
        {
            var AuthorRequest = new AuthorRequestTo(0, "testDeleteLogin", "password", "fname", "lname");
            var AuthorResponse = (await _controller.CreateAuthor(AuthorRequest)).Value as AuthorResponseTo;
            Assert.IsNotNull(AuthorResponse);

            _dbContext.ChangeTracker.Clear();

            var result = await _controller.DeleteAuthor(AuthorResponse.Id);

            var expected = typeof(NoContentResult);

            Assert.IsInstanceOfType(result, expected);
        }

    }
}