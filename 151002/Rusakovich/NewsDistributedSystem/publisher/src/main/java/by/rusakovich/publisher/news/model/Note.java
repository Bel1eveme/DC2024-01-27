package by.rusakovich.publisher.news.model;

import by.rusakovich.publisher.generics.model.IEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_note")
public class Note implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String content;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;
}
