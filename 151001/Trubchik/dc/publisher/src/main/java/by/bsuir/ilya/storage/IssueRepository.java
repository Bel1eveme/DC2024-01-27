package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Issue;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class IssueRepository implements InMemoryRepository<Issue> {


    private Map<Long, Issue> issueMemory = new ConcurrentHashMap<>();

    AtomicLong lastId = new AtomicLong();

    @Override
    public Issue findById(long id) {
        Issue issue = issueMemory.get(id);
        return issue;
    }

    @Override
    public List<Issue> findAll() {
        List<Issue> issueList = new ArrayList<>();
        for (Long key : issueMemory.keySet()) {
            Issue issue = issueMemory.get(key);
            ;
            issueList.add(issue);
        }
        return issueList;
    }

    @Override
    public Issue deleteById(long id) {
        Issue result = issueMemory.remove(id);
        return result;
    }

    @Override
    public boolean deleteAll() {
        issueMemory.clear();
        return true;
    }

    @Override
    public Issue insert(Issue insertObject) {
        long id = lastId.getAndIncrement();
        insertObject.setId(id);
        issueMemory.put(id, insertObject);
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Issue updatingIssue) {
        boolean status = issueMemory.replace(id, issueMemory.get(id), updatingIssue);
        return status;
    }

    @Override
    public boolean update(Issue updatingValue) {
        boolean status = issueMemory.replace(updatingValue.getId(), issueMemory.get(updatingValue.getId()), updatingValue);
        return status;
    }

}
