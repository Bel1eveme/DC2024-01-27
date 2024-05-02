package by.bsuir.ilya.Service;

import by.bsuir.ilya.Entity.Issue;
import by.bsuir.ilya.dto.IssueMapper;
import by.bsuir.ilya.dto.IssueRequestTo;
import by.bsuir.ilya.dto.IssueResponseTo;
import by.bsuir.ilya.redis.RedisIssueRepository;
import by.bsuir.ilya.repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class IssueService implements IService<IssueResponseTo, IssueRequestTo> {
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Autowired
    private RedisIssueRepository redisRepository;

    public List<IssueResponseTo> getAll() {
        List<Issue> issueList = redisRepository.getAll();;
        List<IssueResponseTo> resultList = new ArrayList<>();
        if(!issueList.isEmpty())
        {
            for (int i = 0; i < issueList.size(); i++) {
                resultList.add(IssueMapper.INSTANCE.issueToIssueResponseTo(issueList.get(i)));
            }
        }else
        {
            issueList = issueRepository.findAll();
            for (int i = 0; i < issueList.size(); i++) {
                resultList.add(IssueMapper.INSTANCE.issueToIssueResponseTo(issueList.get(i)));
                redisRepository.add(issueList.get(i));
            }
        }
        return resultList;
    }

    public IssueResponseTo update(IssueRequestTo updatingIssue) {
        Issue issue = IssueMapper.INSTANCE.issueRequestToToIssue(updatingIssue);
        issue.setModified(LocalDateTime.now());
        issue.setCreated(LocalDateTime.now());
        if (validateIssue(issue)) {
            IssueResponseTo responseTo;
            Optional<Issue> redisIssue = redisRepository.getById(issue.getId());
            if(redisIssue.isPresent() && issue.equals(redisIssue.get()))
            {
                return IssueMapper.INSTANCE.issueToIssueResponseTo(redisIssue.get());
            }
            try {
                Issue result = issueRepository.save(issue);
                redisRepository.update(result);
                responseTo = IssueMapper.INSTANCE.issueToIssueResponseTo(result);
            }catch (Exception e)
            {
                e.getMessage();
                return new IssueResponseTo();
            }
            return responseTo;
        } else return new IssueResponseTo();
        //return responseTo;
    }

    public IssueResponseTo getById(long id) {
        Optional<Issue> redisIssue = redisRepository.getById(id);
        Issue result;
        if(redisIssue.isPresent())
        {
            return IssueMapper.INSTANCE.issueToIssueResponseTo(redisIssue.get());
        }
        else {
           result = issueRepository.findById(id);
           redisRepository.add(result);
        }
        return IssueMapper.INSTANCE.issueToIssueResponseTo(result);
    }

    public boolean deleteById(long id) {

        if(issueRepository.existsById(id)) {
                issueRepository.deleteById(id);
                redisRepository.delete(id);
                return true;
        }
        return false;
    }

    public ResponseEntity<IssueResponseTo> add(IssueRequestTo issueRequestTo) {
        Issue issue = IssueMapper.INSTANCE.issueRequestToToIssue(issueRequestTo);
        issue.setCreated(LocalDateTime.now());
        issue.setModified(LocalDateTime.now());
        if (validateIssue(issue)) {
            IssueResponseTo responseTo;
            try {
                Issue result = issueRepository.save(issue);
                redisRepository.add(result);
                responseTo = IssueMapper.INSTANCE.issueToIssueResponseTo(result);
            } catch (Exception e)
            {
                issue.setId(issue.getId()-1);
                return new ResponseEntity<>(IssueMapper.INSTANCE.issueToIssueResponseTo(issue), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(responseTo, HttpStatus.CREATED);
        } else return new ResponseEntity<>(IssueMapper.INSTANCE.issueToIssueResponseTo(issue), HttpStatus.FORBIDDEN);
    }

    private boolean validateIssue(Issue issue) {
        if(issue.getContent()!=null && issue.getTitle()!=null && issue.getUser()!=null) {
            String title = issue.getTitle();
            String content = issue.getContent();
            if ((content.length() >= 4 && content.length() <= 2048) && (title.length() >= 2 && title.length() <= 64)) {
                return true;
            }
        }
        return false;
    }
}
