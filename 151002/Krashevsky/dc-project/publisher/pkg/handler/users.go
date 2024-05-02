package handler

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"publisher/pkg/model"
	"strconv"
)

func (h *Handler) getAllUsers(c *gin.Context) {
	users, err := h.service.User.GetAllUsers()
	if err != nil {
		newErrorResponse(c, http.StatusInternalServerError, err.Error())
		return
	}

	c.JSON(http.StatusOK, users)
}

func (h *Handler) getUserById(c *gin.Context) {
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

	user, err := h.service.User.GetUserById(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, user)
}

func (h *Handler) createUser(c *gin.Context) {
	var input model.User

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	user, err := h.service.User.CreateUser(input)
	if err != nil {
		newErrorResponse(c, http.StatusForbidden, err.Error())
		return
	}
	c.JSON(http.StatusCreated, user)
}

func (h *Handler) deleteUser(c *gin.Context) {
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

	err = h.service.User.DeleteUser(id)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusNoContent, statusResponse{
		Status: "ok",
	})
}

func (h *Handler) updateUser(c *gin.Context) {
	var input model.User

	if err := c.BindJSON(&input); err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}

	user, err := h.service.User.UpdateUser(input.Id, input)
	if err != nil {
		newErrorResponse(c, http.StatusBadRequest, err.Error())
		return
	}
	c.JSON(http.StatusOK, user)
}
