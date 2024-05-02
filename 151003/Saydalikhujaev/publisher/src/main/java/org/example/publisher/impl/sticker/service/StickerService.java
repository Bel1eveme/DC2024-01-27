package org.example.publisher.impl.sticker.service;

import org.example.publisher.api.exception.DuplicateEntityException;
import org.example.publisher.api.exception.EntityNotFoundException;
import org.example.publisher.impl.issue.Issue;
import org.example.publisher.impl.sticker.Sticker;
import org.example.publisher.impl.sticker.StickerRepository;
import org.example.publisher.impl.sticker.dto.StickerRequestTo;
import org.example.publisher.impl.sticker.dto.StickerResponseTo;
import org.example.publisher.impl.sticker.mapper.Impl.StickerMapperImpl;
import org.example.publisher.impl.issue.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "stickerCache")
public class StickerService {

    private final StickerRepository stickerRepository;
    private final IssueRepository issueRepository;

    private final StickerMapperImpl stickerMapper;
    private final String ENTITY_NAME = "sticker";


    @Cacheable(cacheNames = "stickers")
    public List<StickerResponseTo> getStickers() {
        List<Sticker> stickers = stickerRepository.findAll();
        List<StickerResponseTo> stickersTos = new ArrayList<>();
        for (var sticker: stickers) {
            stickersTos.add(stickerMapper.stickerToResponseTo(sticker));
        }
        return stickersTos;
    }

    @Cacheable(cacheNames = "stickers", key = "#id", unless = "#result == null")
    public StickerResponseTo getStickerById(BigInteger id) throws EntityNotFoundException {
        Optional<Sticker> sticker = stickerRepository.findById(id);
        if (sticker.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        return stickerMapper.stickerToResponseTo(sticker.get());
    }

    @CacheEvict(cacheNames = "stickers", allEntries = true)
    public StickerResponseTo saveSticker(StickerRequestTo sticker) throws DuplicateEntityException {
        List<Issue> issues = new ArrayList<>();
        if (sticker.getIssueIds() != null) {
            issues = issueRepository.findAllById(sticker.getIssueIds());
        }
        Sticker entity = stickerMapper.dtoToEntity(sticker, issues);
        try {
            Sticker savedSticker = stickerRepository.save(entity);
            return stickerMapper.stickerToResponseTo(savedSticker);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }

    }

    @CacheEvict(cacheNames = "stickers", allEntries = true)
    public StickerResponseTo updateSticker(StickerRequestTo stickerRequestTo) throws EntityNotFoundException, DuplicateEntityException {
        Optional<Sticker> stickerEntity = stickerRepository.findById(stickerRequestTo.getId());
        if (stickerEntity.isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, stickerRequestTo.getId());
        }
        List<Issue> issues = new ArrayList<>();
        if (stickerRequestTo.getIssueIds() != null) {
            issues = issueRepository.findAllById(stickerRequestTo.getIssueIds());
        }
        try {
            Sticker sticker = stickerRepository.save(stickerMapper.dtoToEntity(stickerRequestTo, issues));
            return stickerMapper.stickerToResponseTo(sticker);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateEntityException(ENTITY_NAME, "");
        }
    }

    @Caching(evict = { @CacheEvict(cacheNames = "stickers", key = "#id"),
            @CacheEvict(cacheNames = "stickers", allEntries = true) })
    public void deleteStickerById(BigInteger id) throws EntityNotFoundException {
        if (stickerRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        stickerRepository.deleteById(id);
    }

}
