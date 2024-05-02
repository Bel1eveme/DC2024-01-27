package handler

import (
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	"strconv"
)

type errorResponse struct {
	Message string `json:"errorMessage"`
	Code    string `json:"errorCode"`
}

type statusResponse struct {
	Status string `json:"status"`
}

func newErrorResponse(c *gin.Context, statusCode int, message string) {
	logrus.Error(message)
	c.AbortWithStatusJSON(statusCode, errorResponse{message, strconv.Itoa(statusCode)})
}
