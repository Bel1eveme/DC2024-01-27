package by.bsuir.ilya.Entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_stickers", schema = "distcomp")
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distcomp_tbl_stickers_seq")
    @SequenceGenerator(name = "distcomp_tbl_stickers_seq", sequenceName = "distcomp_tbl_stickers_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false,name="name")
    private String name;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            schema = "distcomp",
            name = "tbl_issue_sticker",
            joinColumns = @JoinColumn(name = "sticker_id"),
            inverseJoinColumns = @JoinColumn(name = "issue_id")
    )
    private Set<Issue> issues;
    public Sticker() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

