package by.rusakovich.publisher.news.model;

import by.rusakovich.publisher.author.model.Author;
import by.rusakovich.publisher.generics.model.IEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_news")
@Getter
@Setter
public class News implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String title;

    @Column(length = 2048)
    private String content;

    @CreationTimestamp
    private Date creation;

    @UpdateTimestamp
    private Date modification;

    @OneToMany(mappedBy = "news")
    private List<NewsLabel> labels;

    @ManyToOne
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private Author author;

    @OneToMany(mappedBy = "news")
    private List<Note> notes;
}
