package com.example.publicator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Creator creator;
    @Column(name = "title")
    @Size(min = 2, max = 64)
    private String title;
    @Column(name = "content")
    @Size(min = 4, max = 2048)
    private String content;
    @Column(name = "created")
    private Timestamp created;
    @Column(name = "modified")
    private Timestamp modified;


    @ManyToMany
    @JoinTable(
            name = "tbl_issue_tag",
            joinColumns = {@JoinColumn(name = "issue_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id") }
    )
    private List<Tag> tags;

}

