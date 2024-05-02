package org.example.publisher.impl.note;

import org.example.publisher.impl.news.News;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @JoinColumn(name = "news")
    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    @Column(name = "content")
    private String content;
}
