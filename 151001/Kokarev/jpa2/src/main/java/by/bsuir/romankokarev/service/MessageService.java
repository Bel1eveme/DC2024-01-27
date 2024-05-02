package by.bsuir.romankokarev.service;

import by.bsuir.romankokarev.mapper.MessageMapper;
import by.bsuir.romankokarev.dto.MessageRequestTo;
import by.bsuir.romankokarev.dto.MessageResponseTo;
import by.bsuir.romankokarev.exception.NotFoundException;
import by.bsuir.romankokarev.mapper.MessageListMapper;
import by.bsuir.romankokarev.model.Message;
import by.bsuir.romankokarev.repository.NewsRepository;
import by.bsuir.romankokarev.repository.MessageRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    MessageListMapper messageListMapper;

    public MessageResponseTo create(@Valid MessageRequestTo messageRequestTo) {
        Message message = messageMapper.messageRequestToMessage(messageRequestTo);
        if (messageRequestTo.getNewsId() != 0) {
            message.setNews(newsRepository.findById(messageRequestTo.getNewsId()).orElseThrow(() -> new NotFoundException("News not found", 404)));
        }

        return messageMapper.messageToMessageResponse(messageRepository.save(message));
    }

    public MessageResponseTo read(@Min(0) int id) throws NotFoundException {
        if (messageRepository.existsById(id)) {
            MessageResponseTo messageResponseTo = messageMapper.messageToMessageResponse(messageRepository.getReferenceById(id));

            return messageResponseTo;
        }

        throw new NotFoundException("Message not found", 404);
    }

    public List<MessageResponseTo> readAll(int pageInd, int numOfElem, String sortedBy, String direction) {
        Pageable p;
        if (direction != null && direction.equals("asc")) {
            p = PageRequest.of(pageInd, numOfElem, Sort.by(sortedBy).ascending());
        } else {
            p = PageRequest.of(pageInd, numOfElem, Sort.by(sortedBy).descending());
        }
        Page<Message> res = messageRepository.findAll(p);

        return messageListMapper.toMessageResponseList(res.toList());
    }

    public MessageResponseTo update(@Valid MessageRequestTo messageRequestTo, @Min(0) int id) throws NotFoundException {
        if (messageRepository.existsById(id)) {
            Message message = messageMapper.messageRequestToMessage(messageRequestTo);
            message.setId(id);
            if (messageRequestTo.getNewsId() != 0) {
                message.setNews(newsRepository.findById(messageRequestTo.getNewsId()).orElseThrow(() -> new NotFoundException("News not found", 404)));
            }

            return messageMapper.messageToMessageResponse(messageRepository.save(message));
        }

        throw new NotFoundException("Message not found", 404);
    }

    public boolean delete(@Min(0) int id) throws NotFoundException {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);

            return true;
        }

        throw new NotFoundException("Message not found", 404);
    }
}
