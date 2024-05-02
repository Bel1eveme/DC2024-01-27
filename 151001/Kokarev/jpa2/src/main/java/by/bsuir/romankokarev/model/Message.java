package by.bsuir.romankokarev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @ManyToOne
    @JoinColumn(name = "news_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private News news;
    @Column(name = "content")
    @Size(min = 2, max = 2048)
    String content;
}
