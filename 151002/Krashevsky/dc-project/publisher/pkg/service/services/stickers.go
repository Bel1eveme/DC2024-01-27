package services

import (
	"encoding/json"
	"errors"
	"github.com/go-playground/validator/v10"
	"github.com/redis/go-redis/v9"
	"publisher/pkg/model"
	"publisher/pkg/repository"
	"strconv"
)

const (
	stickerName  = "sticker"
	stickersName = "stickers"
)

type StickersService struct {
	repo    repository.Sticker
	redisDb *redis.Client
}

func NewStickersService(repo repository.Sticker, redisDb *redis.Client) *StickersService {
	return &StickersService{
		repo:    repo,
		redisDb: redisDb,
	}
}

func (s *StickersService) CreateSticker(sticker model.Sticker) (model.Sticker, error) {
	validate := validator.New()
	if err := validate.Struct(sticker); err != nil {
		return model.Sticker{}, err
	}
	result, err := s.repo.CreateSticker(sticker)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, stickerName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, stickersName)
	}
	return result, err
}

func (s *StickersService) GetAllStickers() ([]model.Sticker, error) {
	stickers := make([]model.Sticker, 0)
	value, err := s.redisDb.Get(ctx, stickersName).Result()
	if err != nil {
		stickers, err = s.repo.GetAllStickers()
		stickersJSON, errJSON := json.Marshal(stickers)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, stickersName, stickersJSON, 0)
		}
		return stickers, err
	}
	err = json.Unmarshal([]byte(value), &stickers)
	return stickers, err
}

func (s *StickersService) GetStickerById(id int) (model.Sticker, error) {
	var sticker model.Sticker
	value, err := s.redisDb.Get(ctx, stickerName+strconv.Itoa(id)).Result()
	if err != nil {
		sticker, err = s.repo.GetStickerById(id)
		userJSON, errJSON := json.Marshal(sticker)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, stickerName+strconv.Itoa(id), userJSON, 0)
		}
		return sticker, err
	}
	err = json.Unmarshal([]byte(value), &sticker)
	return sticker, err
}

func (s *StickersService) DeleteSticker(id int) error {
	num, err := s.repo.DeleteSticker(id)
	if err == nil && num == 0 {
		return errors.New("sticker with specified id not found")
	}
	s.redisDb.Del(ctx, stickerName+strconv.Itoa(id))
	s.redisDb.Del(ctx, stickersName)
	return err
}

func (s *StickersService) UpdateSticker(id int, sticker model.Sticker) (model.Sticker, error) {
	validate := validator.New()
	if err := validate.Struct(sticker); err != nil {
		return model.Sticker{}, err
	}
	result, err := s.repo.UpdateSticker(id, sticker)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, stickerName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, stickersName)
	}
	return result, err
}
