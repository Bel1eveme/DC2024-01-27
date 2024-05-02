package by.bsuir.discussionservice.entity;

import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_message")
public class Message {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @PrimaryKeyClass
    public static class Key implements Serializable {
        @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
        private String country;

        @PrimaryKeyColumn(name = "story_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private Long storyId;

        @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long id;
    }

    @PrimaryKey
    private Key key;

    @Column("content")
    private String content;
}
