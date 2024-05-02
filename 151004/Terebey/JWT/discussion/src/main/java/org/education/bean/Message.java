package org.education.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@NoArgsConstructor
@Data
@Table
public class Message {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    String country;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    int id;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    int issueId;

    String content;
}