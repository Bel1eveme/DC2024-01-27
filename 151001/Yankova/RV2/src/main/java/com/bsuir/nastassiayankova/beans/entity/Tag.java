package com.bsuir.nastassiayankova.beans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "tbl_tag")
public class Tag extends BaseEntity {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<NewsTag> newsTagList = new ArrayList<>();
}
