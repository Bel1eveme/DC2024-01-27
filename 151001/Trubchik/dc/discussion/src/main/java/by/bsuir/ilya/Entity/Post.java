package by.bsuir.ilya.Entity;

import jakarta.persistence.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Entity
@Table(name="post")
public class Post {

    @PrimaryKeyColumn(name="country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String country = "BY";

    @PrimaryKeyColumn(name="id",ordinal = 0,type = PrimaryKeyType.CLUSTERED)
    @Id
    private Long id = UUID.randomUUID().getLeastSignificantBits() % 1000000;

    @PrimaryKeyColumn(name="issue_id",ordinal = 0,type = PrimaryKeyType.CLUSTERED)
    private Long issueId;

    @Column(name = "content")
    private String content;

    public Post() {

    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountry()
    {
        return this.country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}