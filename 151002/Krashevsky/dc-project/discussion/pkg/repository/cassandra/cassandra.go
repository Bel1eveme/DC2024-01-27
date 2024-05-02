package cassandra

import (
	"github.com/gocql/gocql"
)

const commentsTable = "tbl_comments"

func New(cluster *gocql.ClusterConfig) (*gocql.Session, error) {
	session, err := cluster.CreateSession()
	if err != nil {
		return nil, err
	}

	return session, nil
}
