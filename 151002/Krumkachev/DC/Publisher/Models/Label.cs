using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Publisher.Models;

[Table("tbl_Label")]
public class Label : BaseModel
{
	[Required]
	[MinLength(2)]
	[MaxLength(32)]
	[Column(TypeName = "text")]
	public string Name { get; set; }

	public virtual ICollection<Issue> Issue { get; set; } = [];
}