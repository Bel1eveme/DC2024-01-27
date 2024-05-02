package com.example.rv.impl.label;

import com.example.rv.impl.tweet.Tweet;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "tbl_label")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label
{



    public BigInteger getTg_id() {
        return tg_id;
    }

    public void setTg_id(BigInteger tg_id) {
        this.tg_id = tg_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigInteger tg_id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "tbl_m2m_tweet_label",
            joinColumns = @JoinColumn(name = "tg_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> tweets;
}
