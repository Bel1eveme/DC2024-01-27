package by.bsuir.ilya.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_issues", schema = "distcomp")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distcomp_tbl_issues_seq")
    @SequenceGenerator(name = "distcomp_tbl_issues_seq", sequenceName = "distcomp_tbl_issues_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false,name="title")
    private String title;
    @Column(nullable = false,name="content")
    private String content;
    @Column(nullable = false,name="created")
    private LocalDateTime created;
    @Column(nullable = false,name="modified")
    private LocalDateTime modified;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            schema = "distcomp",
            name = "tbl_issue_sticker",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private Set<Sticker> stickers;

    public String getTitle() {
        return title;
    }


    public void setUser(User user)
    {
        this.user = user;
    }
    public User getUser()
    {
        return this.user;
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
