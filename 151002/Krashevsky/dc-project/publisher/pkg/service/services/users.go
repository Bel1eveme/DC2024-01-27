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
)

const (
	userName  = "user"
	usersName = "users"
)

var ctx = context.Background()

type UsersService struct {
	repo    repository.User
	redisDb *redis.Client
}

func NewUsersService(repo repository.User, redisDb *redis.Client) *UsersService {
	return &UsersService{
		repo:    repo,
		redisDb: redisDb,
	}
}

func (s *UsersService) CreateUser(user model.User) (model.User, error) {
	validate := validator.New()
	if err := validate.Struct(user); err != nil {
		return model.User{}, err
	}
	result, err := s.repo.CreateUser(user)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, userName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, usersName)
	}
	return result, err
}

func (s *UsersService) GetAllUsers() ([]model.User, error) {
	users := make([]model.User, 0)
	value, err := s.redisDb.Get(ctx, usersName).Result()
	if err != nil {
		users, err = s.repo.GetAllUsers()
		usersJSON, errJSON := json.Marshal(users)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, usersName, usersJSON, 0)
		}
		return users, err
	}
	err = json.Unmarshal([]byte(value), &users)
	return users, err
}

func (s *UsersService) GetUserById(id int) (model.User, error) {
	var user model.User
	value, err := s.redisDb.Get(ctx, userName+strconv.Itoa(id)).Result()
	if err != nil {
		user, err = s.repo.GetUserById(id)
		userJSON, errJSON := json.Marshal(user)
		if err == nil && errJSON == nil {
			s.redisDb.Set(ctx, userName+strconv.Itoa(id), userJSON, 0)
		}
		return user, err
	}
	err = json.Unmarshal([]byte(value), &user)
	return user, err
}

func (s *UsersService) DeleteUser(id int) error {
	num, err := s.repo.DeleteUser(id)
	if err == nil && num == 0 {
		return errors.New("user with specified id not found")
	}
	s.redisDb.Del(ctx, userName+strconv.Itoa(id))
	s.redisDb.Del(ctx, usersName)
	return err
}

func (s *UsersService) UpdateUser(id int, user model.User) (model.User, error) {
	validate := validator.New()
	if err := validate.Struct(user); err != nil {
		return model.User{}, err
	}
	result, err := s.repo.UpdateUser(id, user)
	resultJSON, errJSON := json.Marshal(result)
	if err == nil && errJSON == nil {
		s.redisDb.Set(ctx, userName+strconv.Itoa(result.Id), resultJSON, 0)
		s.redisDb.Del(ctx, usersName)
	}
	return result, err
}
