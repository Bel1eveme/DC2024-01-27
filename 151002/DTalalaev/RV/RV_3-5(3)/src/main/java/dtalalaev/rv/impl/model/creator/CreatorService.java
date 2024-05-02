package dtalalaev.rv.impl.model.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorService {


    @Autowired
    private CreatorRepository creatorRepository;

    public CreatorResponseTo findOne(BigInteger id) throws ResponseStatusException{
        if(!creatorRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator Not Found");
        }
        Optional<Creator> creator = creatorRepository.findById(id);
        return new CreatorResponseTo(creator.get().getId(), creator.get().getLogin(), creator.get().getPassword(), creator.get().getFirstname(), creator.get().getLastname());
    }


    public List<Creator> findAll() {
        return (List<Creator>) creatorRepository.findAll();
    }

    public CreatorResponseTo create(CreatorRequestTo dto) {
        Creator creator = new Creator();
        creator.setLogin(dto.getLogin());
        creator.setPassword(dto.getPassword());
        creator.setFirstname(dto.getFirstname());
        creator.setLastname(dto.getLastname());
        try {
            creatorRepository.save(creator);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Creator with this login already exists");
        }
        Creator creator1 = creatorRepository.findById(creator.getId()).get();
        CreatorResponseTo creatorResponseTo = new CreatorResponseTo(creator1.getId(), creator1.getLogin(), creator1.getPassword(), creator1.getFirstname(), creator1.getLastname());
        return creatorResponseTo;
    }
    public CreatorResponseTo update(CreatorRequestTo dto) throws ResponseStatusException {
        if(!creatorRepository.existsById(dto.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator Not Found");
        }
        Creator creatorWas = creatorRepository.findById(dto.getId()).get();
        Creator creator = new Creator();
        creator.setId(dto.getId());
        creator.setLogin(dto.getLogin() == null? creatorWas.getLogin(): dto.getLogin());
        creator.setPassword(dto.getPassword() == null? creatorWas.getPassword(): dto.getPassword());
        creator.setFirstname(dto.getFirstname() == null? creatorWas.getFirstname(): dto.getFirstname());
        creator.setLastname(dto.getLastname() == null? creatorWas.getLastname(): dto.getLastname());
        creatorRepository.save(creator);
        Creator creator1 = creatorRepository.findById(creator.getId()).get();
        CreatorResponseTo creatorResponseTo = new CreatorResponseTo(creator1.getId(), creator1.getLogin(), creator1.getPassword(), creator1.getFirstname(), creator1.getLastname());
        return creatorResponseTo;
    }

    public void delete(BigInteger id) throws ResponseStatusException {
        if( !creatorRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator Not Found");
        }
        creatorRepository.deleteById(id);
    }
}
