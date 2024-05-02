package postgresql

import (
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"publisher/pkg/model"
)

type CommentsPostgres struct {
	db *sqlx.DB
}

func NewCommentsPostgres(db *sqlx.DB) *CommentsPostgres {
	return &CommentsPostgres{db: db}
}

func (r *CommentsPostgres) CreateComment(comment model.Comment) (model.Comment, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (story_id, c_content) values ($1, $2) RETURNING id", commentsTable)
	row := r.db.QueryRow(query, comment.StoryId, comment.Content)
	if err := row.Scan(&id); err != nil {
		return comment, err
	}
	comment.Id = id
	return comment, nil
}

func (r *CommentsPostgres) GetAllComments() ([]model.Comment, error) {
	comments := make([]model.Comment, 0)
	query := fmt.Sprintf("SELECT * FROM %s", commentsTable)
	err := r.db.Select(&comments, query)
	return comments, err
}

func (r *CommentsPostgres) GetCommentById(id int) (model.Comment, error) {
	comment := make([]model.Comment, 0)
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", commentsTable)
	err := r.db.Select(&comment, query, id)
	if len(comment) == 0 {
		return model.Comment{}, errors.New("comment with specified id not found")
	}
	return comment[0], err
}

func (r *CommentsPostgres) DeleteComment(id int) (int, error) {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = $1", commentsTable)
	res, err := r.db.Exec(query, id)
	if err != nil {
		return 0, err
	}
	num, err := res.RowsAffected()
	if err != nil {
		return 0, err
	}
	return int(num), err
}

func (r *CommentsPostgres) UpdateComment(id int, comment model.Comment) (model.Comment, error) {
	query := fmt.Sprintf(
		"UPDATE %s SET "+
			"id=$1, story_id=$2, c_content=$3 "+
			"WHERE id=$4",
		commentsTable)
	_, err := r.db.Exec(query, comment.Id, comment.StoryId, comment.Content, id)
	return comment, err
}
