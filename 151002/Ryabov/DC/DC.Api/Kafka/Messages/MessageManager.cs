using System.Collections.Concurrent;

namespace Forum.Api.Kafka.Messages;


public class MessageManager<TKey, TValue>
{
    private readonly ConcurrentDictionary<TKey, TaskCompletionSource<TValue>> _requests = new();

    public void AddRequest(TKey key, TaskCompletionSource<TValue> tcs)
    {
        _requests[key] = tcs;
    }

    public bool RemoveRequest(TKey key)
    {
        return _requests.TryRemove(key, out _);
    }

    public bool TryCompleteRequest(TKey key, TValue value)
    {
        if (!_requests.TryRemove(key, out var tcs)) return false;
        
        tcs.SetResult(value);
        return true;
    }

    public TaskCompletionSource<TValue>? GetRequest(TKey key)
    {
        return _requests.TryGetValue(key, out var tcs) ? tcs : null;
    }
}