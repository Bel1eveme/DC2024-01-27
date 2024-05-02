package model

type Comment struct {
	Id      int    `json:"id" db:"id"`
	StoryId int    `json:"storyId" db:"story_id" binding:"required"`
	Country string `json:"country" db:"country"`
	Content string `json:"content" db:"c_content" binding:"required" validate:"min=4,max=2048"`
}
