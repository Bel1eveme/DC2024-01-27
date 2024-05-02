package com.example.rv.impl.tag;

import com.example.rv.impl.tweet.Tweet;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "tbl_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger tg_id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "tbl_m2m_tweet_tag",
            joinColumns = @JoinColumn(name = "tg_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> tweets;
}
