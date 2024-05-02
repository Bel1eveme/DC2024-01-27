package service

import (
	"discussion/pkg/model"
	"discussion/pkg/repository"
	"discussion/pkg/service/services"
)

type Comment interface {
	CreateComment(comment model.Comment) (model.Comment, error)
	GetAllComments() ([]model.Comment, error)
	GetCommentById(id int) (model.Comment, error)
	DeleteComment(id int) error
	UpdateComment(id int, comment model.Comment) (model.Comment, error)
}

type Service struct {
	Comment
}

func NewService(repository *repository.Repository) *Service {
	return &Service{
		Comment: services.NewCommentsService(repository.Comment),
	}
}
