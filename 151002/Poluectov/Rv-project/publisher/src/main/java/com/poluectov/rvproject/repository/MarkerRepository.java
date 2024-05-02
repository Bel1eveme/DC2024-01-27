package com.poluectov.rvproject.repository;

import com.poluectov.rvproject.dto.marker.MarkerRequestTo;
import com.poluectov.rvproject.model.Marker;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
@Component
public interface MarkerRepository extends ICommonRepository<Marker, Long> {

    List<Marker> findByIdIn(List<Long> ids);
}
