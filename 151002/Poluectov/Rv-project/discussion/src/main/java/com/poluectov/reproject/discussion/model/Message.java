package com.poluectov.reproject.discussion.model;


import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.stereotype.Service;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import java.math.BigInteger;
import java.util.UUID;


@Table(value = "tbl_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Message extends IdentifiedEntity{


    @PrimaryKeyColumn(name = "country",
            type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "issueid",
            ordinal = 0,
            type = PrimaryKeyType.CLUSTERED)
    private Long issueId;

    @PrimaryKeyColumn(
            name = "id",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED)
    private Long id;

    private String content;
}
