using Publisher.Entity.Common;
using System.ComponentModel.DataAnnotations;

namespace Publisher.Entity.Db
{
    public class Label(string name) : AbstractEntity
    {
        [MinLength(2)]
        public string Name { get; set; } = name;
        public ICollection<Issue> Issues { get; set; } = [];

        public Label() : this(string.Empty) { }
        public Label(int id, string name) : this(name)
        {
            Id = id;
        }
    }
}
