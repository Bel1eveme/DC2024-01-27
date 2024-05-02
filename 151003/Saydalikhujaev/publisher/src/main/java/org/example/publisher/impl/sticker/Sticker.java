package org.example.publisher.impl.sticker;

import org.example.publisher.impl.issue.Issue;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "tbl_sticker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger tg_id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "tbl_m2m_issue_sticker",
            joinColumns = @JoinColumn(name = "tg_id"),
            inverseJoinColumns = @JoinColumn(name = "issue_id")
    )
    private List<Issue> issues;
}
