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
@Table(name = "tbl_issue")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Issue extends BaseEntity {
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "issue")
    private List<IssueTag> issueTags;

    @Column(unique = true, nullable = false)
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
