package com.example.dc_project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class AEntity implements IEntity {
    private Long id;
}
