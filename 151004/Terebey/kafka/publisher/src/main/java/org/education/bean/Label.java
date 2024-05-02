package org.education.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "tbl_label")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    @ManyToMany
    @JoinTable(
            name = "IssueLabel",
            joinColumns = @JoinColumn(name = "issueId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "labelId", referencedColumnName = "id")
    )
    List<Issue> issues;
}