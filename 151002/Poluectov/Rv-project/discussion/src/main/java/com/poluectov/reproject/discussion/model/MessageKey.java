package com.poluectov.reproject.discussion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageKey implements Serializable {

    private String country;

    private Long id;

    private Long issueId;

}
