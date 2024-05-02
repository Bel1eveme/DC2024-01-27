package service

import (
	"github.com/IBM/sarama"
	"github.com/redis/go-redis/v9"
	"publisher/pkg/model"
	"publisher/pkg/repository"
	"publisher/pkg/service/services"
)

type User interface {
	CreateUser(user model.User) (model.User, error)
	GetAllUsers() ([]model.User, error)
	GetUserById(id int) (model.User, error)
	DeleteUser(id int) error
	UpdateUser(id int, user model.User) (model.User, error)
}

type Story interface {
	CreateStory(story model.Story) (model.Story, error)
	GetAllStories() ([]model.Story, error)
	GetStoryById(id int) (model.Story, error)
	DeleteStory(id int) error
	UpdateStory(id int, user model.Story) (model.Story, error)
}

type Comment interface {
	CreateComment(comment model.Comment) (model.Comment, error)
	GetAllComments() ([]model.Comment, error)
	GetCommentById(id int) (model.Comment, error)
	DeleteComment(id int) error
	UpdateComment(id int, comment model.Comment) (model.Comment, error)
}

type Sticker interface {
	CreateSticker(sticker model.Sticker) (model.Sticker, error)
	GetAllStickers() ([]model.Sticker, error)
	GetStickerById(id int) (model.Sticker, error)
	DeleteSticker(id int) error
	UpdateSticker(id int, user model.Sticker) (model.Sticker, error)
}

type Service struct {
	User
	Story
	Comment
	Sticker
}

func NewService(repository *repository.Repository, redisDb *redis.Client, prod sarama.SyncProducer, cons sarama.Consumer) *Service {
	return &Service{
		User:    services.NewUsersService(repository.User, redisDb),
		Story:   services.NewStoriesService(repository.Story, redisDb),
		Comment: services.NewCommentsService(redisDb, prod, cons),
		Sticker: services.NewStickersService(repository.Sticker, redisDb),
	}
}
