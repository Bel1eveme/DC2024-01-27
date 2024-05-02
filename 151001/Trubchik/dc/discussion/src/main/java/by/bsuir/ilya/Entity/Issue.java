package by.bsuir.ilya.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

public class Issue {


    public Issue(){

    }
    private long id;

    private String title;

    private String content;

    private LocalDateTime created;

    private LocalDateTime modified;

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public String getContent() {
        return content;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
