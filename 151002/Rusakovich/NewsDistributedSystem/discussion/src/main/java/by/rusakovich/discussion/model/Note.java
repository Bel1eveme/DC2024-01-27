package by.rusakovich.discussion.model;


import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Getter
@Setter
public class Note {
    @PrimaryKeyColumn(name = "country", type= PrimaryKeyType.PARTITIONED)
    private String country;
    @PrimaryKeyColumn(name = "news_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    private Long newsId;
    @PrimaryKeyColumn(name = "id", ordinal = 1, type= PrimaryKeyType.CLUSTERED)
    private Long id;
    private String content;
}

