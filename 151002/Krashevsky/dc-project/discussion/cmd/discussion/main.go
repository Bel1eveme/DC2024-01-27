package main

import (
	"discussion/pkg/handler"
	httpserver "discussion/pkg/http-server"
	"discussion/pkg/repository"
	"discussion/pkg/repository/cassandra"
	"discussion/pkg/service"
	"github.com/gocql/gocql"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"github.com/sirupsen/logrus"
	"github.com/spf13/viper"
)

func main() {
	logrus.SetFormatter(new(logrus.JSONFormatter))
	if err := initConfig(); err != nil {
		logrus.Fatalf("error initializing config: %s", err.Error())
	}

	if err := godotenv.Load(); err != nil {
		logrus.Fatalf("error loading env variables: %s", err.Error())
	}

	cluster := gocql.NewCluster(viper.GetString("cassandra.host") + ":" + viper.GetString("cassandra.port"))
	cluster.Keyspace = "distcomp"
	session, err := cassandra.New(cluster)
	if err != nil {
		logrus.Fatalf("failed to initialize db: %s", err.Error())
	}
	repos := repository.NewRepositoryCassandra(session)

	services := service.NewService(repos)
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
