package org.example.publisher.impl.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public interface StickerRepository extends JpaRepository<Sticker, BigInteger> {

}
