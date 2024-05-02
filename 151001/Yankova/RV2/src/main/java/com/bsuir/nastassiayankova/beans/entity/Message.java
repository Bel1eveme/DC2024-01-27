package com.bsuir.nastassiayankova.beans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "tbl_message")
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Column(name = "content")
    private String content;
}
