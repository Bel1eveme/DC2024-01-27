package repository

import (
	"github.com/jmoiron/sqlx"
	"publisher/pkg/model"
	"publisher/pkg/repository/postgresql"
)

type User interface {
	CreateUser(user model.User) (model.User, error)
	GetAllUsers() ([]model.User, error)
	GetUserById(id int) (model.User, error)
	DeleteUser(id int) (int, error)
	UpdateUser(id int, user model.User) (model.User, error)
}

type Story interface {
	CreateStory(story model.Story) (model.Story, error)
	GetAllStories() ([]model.Story, error)
	GetStoryById(id int) (model.Story, error)
	DeleteStory(id int) (int, error)
	UpdateStory(id int, user model.Story) (model.Story, error)
}

type Sticker interface {
	CreateSticker(sticker model.Sticker) (model.Sticker, error)
	GetAllStickers() ([]model.Sticker, error)
	GetStickerById(id int) (model.Sticker, error)
	DeleteSticker(id int) (int, error)
	UpdateSticker(id int, user model.Sticker) (model.Sticker, error)
}

type Repository struct {
	User
	Story
	Sticker
}

func NewRepository(db *sqlx.DB) *Repository {
	return &Repository{
		User:    postgresql.NewUsersPostgres(db),
		Story:   postgresql.NewStoriesPostgres(db),
		Sticker: postgresql.NewStickersPostgres(db),
	}
}
