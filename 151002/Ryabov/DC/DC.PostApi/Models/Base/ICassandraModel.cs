namespace Forum.PostApi.Models.Base;

public interface ICassandraModel<TKey> where TKey : notnull {
    
    public TKey Id { get; set; }
}