package by.bsuir.ilya.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class IssueResponseTo {
    private long id;

    private long userId;

    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
