package com.example.rv.impl.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface LabelRepository extends JpaRepository<Label, BigInteger> {

}
