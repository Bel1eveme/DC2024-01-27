package com.example.rv.impl.label.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@Data
public class LabelRequestTo{
    private BigInteger id;

    @Size(min = 2, max = 32)
    private String name;
    private List<BigInteger> tweetIds;

}
