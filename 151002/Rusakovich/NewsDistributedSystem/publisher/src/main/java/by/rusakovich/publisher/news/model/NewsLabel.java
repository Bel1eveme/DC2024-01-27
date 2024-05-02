package by.rusakovich.publisher.news.model;

import by.rusakovich.publisher.generics.model.IEntity;
import by.rusakovich.publisher.label.model.Label;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_news_label")
public class NewsLabel implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    private Label label;
}
