using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class Sticker(string name) : AbstractEntity
    {
        [MinLength(2)]
        public string Name { get; set; } = name;
        public ICollection<Tweet> Tweets { get; set; } = [];

        public Sticker() : this(string.Empty) { }
        public Sticker(int id, string name) : this(name)
        {
            Id = id;
        }
    }
}
