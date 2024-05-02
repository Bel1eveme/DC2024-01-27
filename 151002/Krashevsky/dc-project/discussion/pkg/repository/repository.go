package repository

import (
	"discussion/pkg/model"
	"discussion/pkg/repository/cassandra"
	"github.com/gocql/gocql"
)

type Comment interface {
	CreateComment(comment model.Comment) (model.Comment, error)
	GetAllComments() ([]model.Comment, error)
	GetCommentById(id int) (model.Comment, error)
	DeleteComment(id int) error
	UpdateComment(id int, user model.Comment) (model.Comment, error)
}
type Repository struct {
	Comment
}

func NewRepositoryCassandra(session *gocql.Session) *Repository {
	return &Repository{
		Comment: cassandra.NewCommentsCassandra(session),
	}
}
