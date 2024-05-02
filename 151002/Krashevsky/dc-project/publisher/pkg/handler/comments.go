package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"publisher/pkg/model"
	"strconv"
)

func (h *Handler) getAllComments(c *gin.Context) {
	comments, err := h.service.Comment.GetAllComments()
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	c.JSON(http.StatusOK, comments)
}

func (h *Handler) getCommentById(c *gin.Context) {
	param, isFound := c.Params.Get("id")
	if !isFound {
		newErrorResponse(c, http.StatusBadRequest, "id not found")
		return
	}

	id, err := strconv.Atoi(param)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid param id")
		return
	}

	comment, err := h.service.Comment.GetCommentById(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, comment)
}

func (h *Handler) createComment(c *gin.Context) {
	var input model.Comment

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	comment, err := h.service.Comment.CreateComment(input)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}
	c.JSON(http.StatusCreated, comment)
}

func (h *Handler) deleteComment(c *gin.Context) {
	param, isFound := c.Params.Get("id")
	if !isFound {
		newErrorResponse(c, http.StatusBadRequest, "id not found")
		return
	}

	id, err := strconv.Atoi(param)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, "invalid param id")
		return
	}

	err = h.service.Comment.DeleteComment(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusNoContent, statusResponse{
		Status: "ok",
	})
}

func (h *Handler) updateComment(c *gin.Context) {
	var input model.Comment

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	comment, err := h.service.Comment.UpdateComment(input.Id, input)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, comment)
}
