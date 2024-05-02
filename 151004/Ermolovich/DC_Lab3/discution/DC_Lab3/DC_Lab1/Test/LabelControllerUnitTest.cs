using DC_Lab1.DTO.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test
{
    public class LabelControllerUnitTest
    {
        private readonly LabelController _controller;
        private readonly LabelService _service;
        private readonly DC_Lab1.DB.SqLiteDbContext _dbContext;
        private readonly IMapper _mapper = new MapperConfiguration(cfg =>
        {
            cfg.AddMaps(["DC_Lab1"]);
        }).CreateMapper();
        public LabelControllerUnitTest()
        {
            _dbContext = new DC_Lab1.DB.SqLiteDbContext();
            _service = new LabelService(_mapper, _dbContext);
            _controller = new LabelController(_service)
            { ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() } };


        }

        [TestMethod]
        public void GetUpdate()
        {
            var StickerRequest = new LabelRequestTo(0,  "TestName");
            var Sticker = _controller.CreateSticker(StickerRequest).Result.Value as LabelResponseTo;
            Assert.IsNotNull(Sticker);

            var newSticker = new LabelRequestTo(Sticker.Id, "NewTestName");
            _dbContext.ChangeTracker.Clear();
            var StickerResponce = _controller.UpdateSticker(newSticker).Result.Value as LabelResponseTo;
            Assert.IsNotNull(StickerResponce);
            Assert.AreEqual(StickerResponce.name, newSticker.name);
        }
        [TestMethod]
        public void GetEntById()
        {
            var StickerRequest = new LabelRequestTo(0, "TestName");
            var Sticker = _controller.CreateSticker(StickerRequest).Result.Value as LabelResponseTo;
            Assert.IsNotNull(Sticker);
            _dbContext.ChangeTracker.Clear();
            var foundedSticker = _controller.GetStickerById(Sticker.Id).Result.Value as LabelResponseTo;
            Assert.IsNotNull(foundedSticker);
            Assert.AreEqual(foundedSticker.Id, Sticker.Id);
        }
        [TestMethod]
        public async Task GetAllEnt()
        {

            var result = _controller.GetStickers();

            Assert.IsNotNull(result);
            Assert.IsInstanceOfType(result.Value, typeof(IEnumerable<IResponseTo>));
        }
        [TestMethod]
        public async Task CreateEnt()
        {
            var StickerRequest = new LabelRequestTo(0, "TestName");
            var StickerResponse = (await _controller.CreateSticker(StickerRequest)).Value as LabelResponseTo;
            Assert.IsNotNull(StickerResponse);

            Assert.AreEqual(StickerResponse.name, "TestName");
        }
        [TestMethod]
        public async Task DeleteEnt()
        {
            var StickerRequest = new LabelRequestTo(0, "TestName");
            var StickerResponse = (await _controller.CreateSticker(StickerRequest)).Value as LabelResponseTo;
            Assert.IsNotNull(StickerResponse);

            _dbContext.ChangeTracker.Clear();

            var result = await _controller.DeleteSticker(StickerResponse.Id);

            var expected = typeof(NoContentResult);

            Assert.IsInstanceOfType(result, expected);
        }

    }
}
