using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class Tag(string name) : AbstractEntity
    {
        [MinLength(2)]
        public string Name { get; set; } = name;
        public ICollection<Tweet> Tweets { get; set; } = [];

        public Tag() : this(string.Empty) { }
        public Tag(int id, string name) : this(name)
        {
            Id = id;
        }
    }
}
