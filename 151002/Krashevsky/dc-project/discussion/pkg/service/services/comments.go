package services

import (
	"discussion/pkg/model"
	"discussion/pkg/repository"
	"github.com/go-playground/validator/v10"
)

type CommentsService struct {
	repo repository.Comment
}

func NewCommentsService(repo repository.Comment) *CommentsService {
	return &CommentsService{repo: repo}
}

func (s *CommentsService) CreateComment(comment model.Comment) (model.Comment, error) {
	validate := validator.New()
	if err := validate.Struct(comment); err != nil {
		return model.Comment{}, err
	}
	return s.repo.CreateComment(comment)
}

func (s *CommentsService) GetAllComments() ([]model.Comment, error) {
	return s.repo.GetAllComments()
}

func (s *CommentsService) GetCommentById(id int) (model.Comment, error) {
	return s.repo.GetCommentById(id)
}

func (s *CommentsService) DeleteComment(id int) error {
	return s.repo.DeleteComment(id)
}

func (s *CommentsService) UpdateComment(id int, comment model.Comment) (model.Comment, error) {
	validate := validator.New()
	if err := validate.Struct(comment); err != nil {
		return model.Comment{}, err
	}
	return s.repo.UpdateComment(id, comment)
}
