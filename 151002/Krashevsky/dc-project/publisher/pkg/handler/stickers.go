package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"publisher/pkg/model"
	"strconv"
)

func (h *Handler) getAllStickers(c *gin.Context) {
	stickers, err := h.service.Sticker.GetAllStickers()
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	c.JSON(http.StatusOK, stickers)
}

func (h *Handler) getStickerById(c *gin.Context) {
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

	stickers, err := h.service.Sticker.GetStickerById(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, stickers)
}

func (h *Handler) createSticker(c *gin.Context) {
	var input model.Sticker

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	sticker, err := h.service.Sticker.CreateSticker(input)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}
	c.JSON(http.StatusCreated, sticker)
}

func (h *Handler) deleteSticker(c *gin.Context) {
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

	err = h.service.Sticker.DeleteSticker(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusNoContent, statusResponse{
		Status: "ok",
	})
}

func (h *Handler) updateSticker(c *gin.Context) {
	var input model.Sticker

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	sticker, err := h.service.Sticker.UpdateSticker(input.Id, input)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, sticker)
}
