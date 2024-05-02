package cassandra

import (
	"discussion/pkg/model"
	"fmt"
	"github.com/gocql/gocql"
	"time"
)

type CommentsCassandra struct {
	session *gocql.Session
}

func NewCommentsCassandra(session *gocql.Session) *CommentsCassandra {
	return &CommentsCassandra{session: session}
}

func (r *CommentsCassandra) CreateComment(comment model.Comment) (model.Comment, error) {
	id := int(time.Now().Unix())
	query := fmt.Sprintf("INSERT INTO %s (id, story_id, country, c_content) values (?, ?, ?, ?)", commentsTable)
	if err := r.session.Query(query, id, comment.StoryId, comment.Country, comment.Content).Exec(); err != nil {
		return comment, err
	}
	comment.Id = id
	return comment, nil
}

func (r *CommentsCassandra) GetAllComments() ([]model.Comment, error) {
	comments := make([]model.Comment, 0)
	query := fmt.Sprintf("SELECT * FROM %s", commentsTable)
	iter := r.session.Query(query).Iter()

	comment := model.Comment{}
	for iter.Scan(&comment.Id, &comment.Content, &comment.Country, &comment.StoryId) {
		comments = append(comments, comment)
		comment = model.Comment{}
	}

	if err := iter.Close(); err != nil {
		return nil, err
	}

	return comments, nil
}

func (r *CommentsCassandra) GetCommentById(id int) (model.Comment, error) {
	comment := model.Comment{}
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = ?", commentsTable)
	err := r.session.Query(query, id).Scan(&comment.Id, &comment.Content, &comment.Country, &comment.StoryId)
	return comment, err
}

func (r *CommentsCassandra) DeleteComment(id int) error {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = ?", commentsTable)
	return r.session.Query(query, id).Exec()
}

func (r *CommentsCassandra) UpdateComment(id int, comment model.Comment) (model.Comment, error) {
	query := fmt.Sprintf(
		"UPDATE %s SET "+
			"story_id=?, c_content=?, country=? "+
			"WHERE id=?",
		commentsTable)
	err := r.session.Query(query, comment.StoryId, comment.Content, comment.Country, id).Exec()
	return comment, err
}
