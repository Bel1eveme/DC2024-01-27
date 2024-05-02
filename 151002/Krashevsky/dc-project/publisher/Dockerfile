FROM golang:latest

COPY ./ ./
RUN go mod download

RUN go build -o publisher ./cmd/publisher/main.go

CMD [ "./publisher" ]