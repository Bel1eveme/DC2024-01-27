package org.education.bean;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "tbl_issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    Creator creator;

    @ManyToMany(mappedBy = "issues")
    List<Label> markers;

    String title;
    String content;
    LocalDateTime created;
    LocalDateTime modified;
}
