package by.bsuir.ilya.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class IssueRequestTo {
    private long id;

    private long userId;

    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
