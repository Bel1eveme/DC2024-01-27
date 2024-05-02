package services

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"github.com/IBM/sarama"
	"github.com/go-playground/validator/v10"
	"github.com/redis/go-redis/v9"
	"net/http"
	"publisher/pkg/broker/producer"
	"publisher/pkg/model"
	"strconv"
)

const (
	commentsName = "comments"
	commentName  = "comment"
)

type CommentsService struct {
	prod    sarama.SyncProducer
	cons    sarama.Consumer
	redisDb *redis.Client
}

func NewCommentsService(redisDb *redis.Client, prod sarama.SyncProducer, cons sarama.Consumer) *CommentsService {
	return &CommentsService{
		redisDb: redisDb,
		prod:    prod,
		cons:    cons,
	}
}

func (s *CommentsService) CreateComment(comment model.Comment) (model.Comment, error) {
	validate := validator.New()
	if err := validate.Struct(comment); err != nil {
		return comment, err
	}

	data, err := json.Marshal(comment)
	if err != nil {
		return comment, err
	}

	resp, err := http.Post("http://localhost:24130/api/v1.0/comments", "application/json", bytes.NewReader(data))
	if err != nil {
		return comment, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusCreated {
		return comment, errors.New("request failed")
	}

	var result model.Comment
	err = json.NewDecoder(resp.Body).Decode(&result)
	if err != nil {
		return comment, err
	}
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, commentName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, commentsName)
	}

	producer.CreateMessage(comment.StoryId, data)
	return result, err

	/*
		paerCons, err := s.cons.ConsumePartition("OutTopic", int32(comment.StoryId), sarama.OffsetNewest)
		if err != nil {
			log.Fatalf("Failed to consume partition: %v", err)
		}
		defer paerCons.Close()

		paerCons.Messages()*/
}

func (s *CommentsService) GetAllComments() ([]model.Comment, error) {
	comments := make([]model.Comment, 0)
	value, err := s.redisDb.Get(ctx, commentsName).Result()
	if err != nil {
		resp, errResp := http.Get("http://localhost:24130/api/v1.0/comments")
		if errResp != nil {
			return make([]model.Comment, 0), errResp
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusOK {
			return make([]model.Comment, 0), errors.New("request failed")
		}

		err = json.NewDecoder(resp.Body).Decode(&comments)
		if err != nil {
			return make([]model.Comment, 0), err
		}
		commentsJSON, errJSON := json.Marshal(comments)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, commentsName, commentsJSON, 0)
		}
		return comments, err
	}
	err = json.Unmarshal([]byte(value), &comments)
	return comments, err
}

func (s *CommentsService) GetCommentById(id int) (model.Comment, error) {
	var comment model.Comment
	value, err := s.redisDb.Get(ctx, commentName+strconv.Itoa(id)).Result()
	if err != nil {
		url := fmt.Sprintf("http://localhost:24130/api/v1.0/comments/%d", id)
		resp, errResp := http.Get(url)
		if errResp != nil {
			return model.Comment{}, errResp
		}
		defer resp.Body.Close()

		if resp.StatusCode != http.StatusOK {
			return model.Comment{}, errors.New("request failed")
		}

		err = json.NewDecoder(resp.Body).Decode(&comment)
		if err != nil {
			return model.Comment{}, err
		}
		userJSON, errJSON := json.Marshal(comment)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, commentName+strconv.Itoa(id), userJSON, 0)
		}
		return comment, err
	}
	err = json.Unmarshal([]byte(value), &comment)
	return comment, err
}

func (s *CommentsService) DeleteComment(id int) error {
	url := fmt.Sprintf("http://localhost:24130/api/v1.0/comments/%d", id)
	req, err := http.NewRequest(http.MethodDelete, url, nil)
	if err != nil {
		return err
	}

	client := &http.Client{}
	resp, err := client.Do(req)

	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusNoContent {
		return errors.New("request failed")
	}
	s.redisDb.Del(ctx, commentName+strconv.Itoa(id))
	s.redisDb.Del(ctx, commentsName)
	return nil
}

func (s *CommentsService) UpdateComment(id int, comment model.Comment) (model.Comment, error) {
	validate := validator.New()
	if err := validate.Struct(comment); err != nil {
		return model.Comment{}, err
	}

	data, err := json.Marshal(comment)
	if err != nil {
		return comment, err
	}
	url := "http://localhost:24130/api/v1.0/comments"

	req, err := http.NewRequest(http.MethodPut, url, bytes.NewReader(data))
	if err != nil {
		return comment, err
	}

	client := &http.Client{}
	resp, err := client.Do(req)

	if err != nil {
		return comment, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return comment, errors.New("request failed")
	}

	var result model.Comment
	err = json.NewDecoder(resp.Body).Decode(&result)
	if err != nil {
		return comment, err
	}
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, commentName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, commentsName)
	}
	return result, nil
}
