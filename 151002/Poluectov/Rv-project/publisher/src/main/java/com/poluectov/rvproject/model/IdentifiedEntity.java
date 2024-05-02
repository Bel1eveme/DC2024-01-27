package com.poluectov.rvproject.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.math.BigInteger;

public abstract class IdentifiedEntity {

    public abstract Long getId();
    public abstract void setId(Long id);
}
