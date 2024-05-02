package by.bsuir.publisher.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_news")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class News extends BaseEntity {
    @ManyToOne
    private Author author;

    @OneToMany(mappedBy = "news")
    private List<NewsMarker> newsMarkers;

    @Column(unique = true, nullable = false)
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
