package handler

import (
	"github.com/gin-gonic/gin"
	"publisher/pkg/service"
)

type Handler struct {
	service *service.Service
}

func NewHandler(services *service.Service) *Handler {
	return &Handler{service: services}
}

func (h *Handler) InitRoutes() *gin.Engine {
	router := gin.New()

	api := router.Group("/api/v1.0")
	{
		users := api.Group("/users")
		{
			users.POST("", h.createUser)
			users.GET("", h.getAllUsers)
			users.PUT("", h.updateUser)
			users.GET("/:id", h.getUserById)
			users.DELETE("/:id", h.deleteUser)
		}
		stories := api.Group("/storys")
		{
			stories.POST("", h.createStory)
			stories.GET("", h.getAllStories)
			stories.PUT("", h.updateStory)
			stories.GET("/:id", h.getStoryById)
			stories.DELETE("/:id", h.deleteStory)
		}
		comments := api.Group("/comments")
		{
			comments.POST("", h.createComment)
			comments.GET("", h.getAllComments)
			comments.PUT("", h.updateComment)
			comments.GET("/:id", h.getCommentById)
			comments.DELETE("/:id", h.deleteComment)
		}
		stickers := api.Group("/stickers")
		{
			stickers.POST("", h.createSticker)
			stickers.GET("", h.getAllStickers)
			stickers.PUT("", h.updateSticker)
			stickers.GET("/:id", h.getStickerById)
			stickers.DELETE("/:id", h.deleteSticker)
		}
	}

	return router
}
