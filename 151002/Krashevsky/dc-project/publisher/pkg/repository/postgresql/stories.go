package postgresql

import (
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"publisher/pkg/model"
)

type StoriesPostgres struct {
	db *sqlx.DB
}

func NewStoriesPostgres(db *sqlx.DB) *StoriesPostgres {
	return &StoriesPostgres{db: db}
}

func (r *StoriesPostgres) CreateStory(story model.Story) (model.Story, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (user_id, title, s_content, created, modified) values ($1, $2, $3, $4, $5) RETURNING id", storiesTable)
	row := r.db.QueryRow(query, story.UserId, story.Title, story.Content, story.Created, story.Modified)
	if err := row.Scan(&id); err != nil {
		return story, err
	}
	story.Id = id
	return story, nil
}

func (r *StoriesPostgres) GetAllStories() ([]model.Story, error) {
	stories := make([]model.Story, 0)
	query := fmt.Sprintf("SELECT * FROM %s", storiesTable)
	err := r.db.Select(&stories, query)
	return stories, err
}

func (r *StoriesPostgres) GetStoryById(id int) (model.Story, error) {
	story := make([]model.Story, 0)
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", storiesTable)
	err := r.db.Select(&story, query, id)
	if len(story) == 0 {
		return model.Story{}, errors.New("story with specified id not found")
	}
	return story[0], err
}

func (r *StoriesPostgres) DeleteStory(id int) (int, error) {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = $1", storiesTable)
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

func (r *StoriesPostgres) UpdateStory(id int, story model.Story) (model.Story, error) {
	query := fmt.Sprintf(
		"UPDATE %s SET "+
			"id=$1, user_id=$2, title=$3, s_content=$4, created=$5, modified=$6 "+
			"WHERE id=$7",
		storiesTable)
	_, err := r.db.Exec(query, story.Id, story.UserId, story.Title, story.Content, story.Created, story.Modified, id)
	return story, err
}
