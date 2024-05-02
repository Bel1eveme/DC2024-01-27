package com.example.dc_project.model.entity;

import com.example.dc_project.model.AEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder

public class Label extends  AEntity{
    private String name;
}
