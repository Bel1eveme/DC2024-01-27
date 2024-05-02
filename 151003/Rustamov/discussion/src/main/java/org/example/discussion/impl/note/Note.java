package org.example.discussion.impl.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_note")
public class Note {
    @PrimaryKeyColumn(name = "note_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private BigInteger id;

    @PrimaryKeyColumn(name = "note_newsid", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private BigInteger newsId;

    @PrimaryKeyColumn(name = "note_country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String com_country;

    @Column("note_content")
    private String content;
}
