FROM golang:latest

COPY ./ ./
RUN go mod download

RUN go build -o discussion ./cmd/discussion/main.go

CMD [ "./discussion" ]