package handler

import (
	"discussion/pkg/service"
	"github.com/gin-gonic/gin"
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
		comments := api.Group("/comments")
		{
			comments.POST("", h.createComment)
			comments.GET("", h.getAllComments)
			comments.PUT("", h.updateComment)
			comments.GET("/:id", h.getCommentById)
			comments.DELETE("/:id", h.deleteComment)
		}

	}

	return router
}
