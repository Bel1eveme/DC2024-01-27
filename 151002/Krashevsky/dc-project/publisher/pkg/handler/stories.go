package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"publisher/pkg/model"
	"strconv"
	"time"
)

func (h *Handler) getAllStories(c *gin.Context) {
	stories, err := h.service.Story.GetAllStories()
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	c.JSON(http.StatusOK, stories)
}

func (h *Handler) getStoryById(c *gin.Context) {
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

	story, err := h.service.Story.GetStoryById(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, story)
}

func (h *Handler) createStory(c *gin.Context) {
	var input model.Story

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	story, err := h.service.Story.CreateStory(input)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}
	c.JSON(http.StatusCreated, story)
}

func (h *Handler) deleteStory(c *gin.Context) {
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

	err = h.service.Story.DeleteStory(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusNoContent, statusResponse{
		Status: "ok",
	})
}

func (h *Handler) updateStory(c *gin.Context) {
	var input model.Story

	input.Created = time.Now()
	input.Modified = time.Now()
	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	story, err := h.service.Story.UpdateStory(input.Id, input)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, story)
}
