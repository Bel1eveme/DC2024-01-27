package model

import "time"

type User struct {
	Id        int    `json:"id" db:"id"`
	Login     string `json:"login" db:"u_login" binding:"required" validate:"min=2,max=64"`
	Password  string `json:"password" db:"u_password" binding:"required" validate:"min=8,max=128"`
	Firstname string `json:"firstname" db:"firstname" binding:"required" validate:"min=2,max=64"`
	Lastname  string `json:"lastname" db:"lastname" binding:"required" validate:"min=2,max=64"`
}

type Story struct {
	Id       int       `json:"id" db:"id"`
	UserId   int       `json:"userId" db:"user_id" binding:"required"`
	Title    string    `json:"title" db:"title" binding:"required" validate:"min=2,max=64"`
	Content  string    `json:"content" db:"s_content" binding:"required" validate:"min=4,max=2048"`
	Created  time.Time `json:"created" db:"created"`
	Modified time.Time `json:"modified" db:"modified"`
}

type Comment struct {
	Country string `json:"country"`
	Id      int    `json:"id" db:"id"`
	StoryId int    `json:"storyId" db:"story_id" binding:"required"`
	Content string `json:"content" db:"c_content" binding:"required" validate:"min=4,max=2048"`
}

type Sticker struct {
	Id   int    `json:"id" db:"id"`
	Name string `json:"name" db:"st_name" binding:"required" validate:"min=4,max=2048"`
}
