package main

import (
	"context"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
	"github.com/spf13/viper"
	"publisher/pkg/broker/consumer"
	"publisher/pkg/broker/producer"
	"publisher/pkg/handler"
	httpserver "publisher/pkg/http-server"
	"publisher/pkg/repository"
	"publisher/pkg/repository/postgresql"
	"publisher/pkg/service"
)

func main() {
	logrus.SetFormatter(new(logrus.JSONFormatter))
	if err := initConfig(); err != nil {
		logrus.Fatalf("error initializing config: %s", err.Error())
	}

	if err := godotenv.Load(); err != nil {
		logrus.Fatalf("error loading env variables: %s", err.Error())
	}

	db, err := postgresql.New(postgresql.Config{
		Host:     viper.GetString("db.host"),
		Port:     viper.GetString("db.port"),
		Username: viper.GetString("db.username"),
		DBName:   viper.GetString("db.dbname"),
		Password: viper.GetString("db.password"),
		SSLMode:  viper.GetString("db.sslmode"),
	})
	if err != nil {
		logrus.Fatalf("failed to initialize db: %s", err.Error())
	}
	defer db.Close()

	redisDB := redis.NewClient(&redis.Options{
		Addr:     viper.GetString("redis.host") + ":" + viper.GetString("redis.port"),
		Password: "",
		DB:       0,
	})

	_, err = redisDB.Ping(context.Background()).Result()
	if err != nil {
		logrus.Fatalf("failed to initialize redis: %s", err.Error())
	}

	cons, err := consumer.New([]string{viper.GetString("kafka.host") + ":" + viper.GetString("kafka.port")})
	if err != nil {
		logrus.Fatalf("failed to connect kafak consumer: %s", err.Error())
	}
	defer cons.Close()

	prod, err := producer.New([]string{viper.GetString("kafka.host") + ":" + viper.GetString("kafka.port")})
	if err != nil {
		logrus.Fatalf("failed to connect kafak producer: %s", err.Error())
	}
	defer prod.Close()

	repos := repository.NewRepository(db)
	services := service.NewService(repos, redisDB, prod, cons)
	handlers := handler.NewHandler(services)

	srv := new(httpserver.Server)
	if err := srv.Run(viper.GetString("port"), handlers.InitRoutes()); err != nil {
		logrus.Fatalf("error occured while running http server: %s", err.Error())
	}
}

func initConfig() error {
	viper.AddConfigPath("./config")
	viper.SetConfigName("local")
	return viper.ReadInConfig()
}
