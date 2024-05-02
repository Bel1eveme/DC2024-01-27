package by.rusakovich.publisher.label.model;

import by.rusakovich.publisher.generics.model.IEntity;
import by.rusakovich.publisher.news.model.NewsLabel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_label")
public class Label implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32)
    private String name;

    @OneToMany(mappedBy = "label")
    private List<NewsLabel> news;
}
