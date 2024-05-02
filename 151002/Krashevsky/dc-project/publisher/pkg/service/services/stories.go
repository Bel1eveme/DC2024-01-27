package services

import (
	"context"
	"encoding/json"
	"errors"
	"github.com/go-playground/validator/v10"
	"github.com/redis/go-redis/v9"
	"publisher/pkg/model"
	"publisher/pkg/repository"
	"strconv"
	"time"
)

const (
	storiesName = "stories"
	storyName   = "story"
)

type StoriesService struct {
	repo    repository.Story
	redisDb *redis.Client
}

var ctxStories = context.Background()

func NewStoriesService(repo repository.Story, redisDb *redis.Client) *StoriesService {
	return &StoriesService{
		repo:    repo,
		redisDb: redisDb,
	}
}

func (s *StoriesService) CreateStory(story model.Story) (model.Story, error) {
	validate := validator.New()
	if err := validate.Struct(story); err != nil {
		return model.Story{}, err
	}
	story.Created = time.Now()
	story.Modified = time.Now()

	result, err := s.repo.CreateStory(story)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctxStories, storyName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctxStories, storiesName)
	}
	return result, err
}

func (s *StoriesService) GetAllStories() ([]model.Story, error) {
	stories := make([]model.Story, 0)
	value, err := s.redisDb.Get(ctxStories, storiesName).Result()
	if err != nil {
		stories, err = s.repo.GetAllStories()
		storiesJSON, errJSON := json.Marshal(stories)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctxStories, storiesName, storiesJSON, 0)
		}
		return stories, err
	}
	err = json.Unmarshal([]byte(value), &stories)
	return stories, err
}

func (s *StoriesService) GetStoryById(id int) (model.Story, error) {
	var story model.Story
	value, err := s.redisDb.Get(ctxStories, storyName+strconv.Itoa(id)).Result()
	if err != nil {
		story, err = s.repo.GetStoryById(id)
		userJSON, errJSON := json.Marshal(story)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctxStories, storyName+strconv.Itoa(id), userJSON, 0)
		}
		return story, err
	}
	err = json.Unmarshal([]byte(value), &story)
	return story, err
}

func (s *StoriesService) DeleteStory(id int) error {
	num, err := s.repo.DeleteStory(id)
	if err == nil && num == 0 {
		return errors.New("story with specified id not found")
	}
	s.redisDb.Del(ctxStories, storyName+strconv.Itoa(id))
	s.redisDb.Del(ctxStories, storiesName)
	return err
}

func (s *StoriesService) UpdateStory(id int, story model.Story) (model.Story, error) {
	validate := validator.New()
	if err := validate.Struct(story); err != nil {
		return model.Story{}, err
	}
	story.Modified = time.Now()
	result, err := s.repo.UpdateStory(id, story)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctxStories, storyName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctxStories, storiesName)
	}
	return result, err
}
