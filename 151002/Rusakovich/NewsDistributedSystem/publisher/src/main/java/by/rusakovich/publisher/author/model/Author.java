package by.rusakovich.publisher.author.model;

import by.rusakovich.publisher.generics.model.IEntity;
import by.rusakovich.publisher.news.model.News;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "tbl_author")
public class Author implements IEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String login;

    @Column(length = 128)
    private String password;

    @Column(length = 64)
    private String firstname;

    @Column(length = 64)
    private String lastname;

    @OneToMany(mappedBy = "author")
    private List<News> news;
}
