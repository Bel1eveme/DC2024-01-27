using System.ComponentModel.DataAnnotations;

namespace Forum.PostApi.Models.Base;

public class BaseModel<TKey> : ICassandraModel<TKey> where TKey : notnull
{
    public TKey Id { get; set; } = default!;
}