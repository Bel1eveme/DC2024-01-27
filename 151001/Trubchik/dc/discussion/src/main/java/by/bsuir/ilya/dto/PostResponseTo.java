package by.bsuir.ilya.dto;

import java.util.UUID;

public class PostResponseTo {
    private Long id = Math.abs(UUID.randomUUID().getLeastSignificantBits());
    private Long issueId;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }
}
