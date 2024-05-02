package com.bsuir.nastassiayankova.beans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "tbl_news")
public class News extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "modified")
    private LocalDateTime modified;

    @OneToMany(mappedBy = "news")
    private List<Message> messageList = new ArrayList<>();

    @OneToMany(mappedBy = "news")
    private List<NewsTag> newsTagList = new ArrayList<>();
}
