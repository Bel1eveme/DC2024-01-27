package by.bsuir.ilya.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_posts", schema = "distcomp")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distcomp_tbl_posts_seq")
    @SequenceGenerator(name = "distcomp_tbl_posts_seq", sequenceName = "distcomp_tbl_posts_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false,name="content")
    private String content;


    @Column(name = "issue_id",nullable = false)
    private Long issueId;
    public Post() {

    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
