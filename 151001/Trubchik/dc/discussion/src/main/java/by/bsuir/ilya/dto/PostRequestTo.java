package by.bsuir.ilya.dto;

import java.math.BigInteger;

public class PostRequestTo {
    private Long id;
    private Long issueId;
    private String content;

    public String getContent() {
        return content;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
