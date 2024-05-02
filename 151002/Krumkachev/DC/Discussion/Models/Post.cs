using Cassandra.Mapping.Attributes;

namespace Discussion.Models
{
	[Table("tbl_post")]
	public class Post
	{
		[PartitionKey]
		public string Country { get; set; } = string.Empty;

		[ClusteringKey]
		public long IssueId { get; set; }

		[ClusteringKey]
		public long Id { get; set; }

		public string Content { get; set; } = string.Empty;
	}
}
