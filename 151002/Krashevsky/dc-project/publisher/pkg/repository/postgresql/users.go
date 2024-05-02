package postgresql

import (
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"publisher/pkg/model"
)

type UsersPostgres struct {
	db *sqlx.DB
}

func NewUsersPostgres(db *sqlx.DB) *UsersPostgres {
	return &UsersPostgres{db: db}
}

func (r *UsersPostgres) CreateUser(user model.User) (model.User, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (u_login, u_password, firstname, lastname) values ($1, $2, $3, $4) RETURNING id", usersTable)
	row := r.db.QueryRow(query, user.Login, user.Password, user.Firstname, user.Lastname)
	if err := row.Scan(&id); err != nil {
		return user, err
	}
	user.Id = id
	return user, nil
}

func (r *UsersPostgres) GetAllUsers() ([]model.User, error) {
	users := make([]model.User, 0)
	query := fmt.Sprintf("SELECT * FROM %s", usersTable)
	err := r.db.Select(&users, query)
	return users, err
}

func (r *UsersPostgres) GetUserById(id int) (model.User, error) {
	user := make([]model.User, 0)
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", usersTable)
	err := r.db.Select(&user, query, id)
	if len(user) == 0 {
		return model.User{}, errors.New("user with specified id not found")
	}
	return user[0], err
}

func (r *UsersPostgres) DeleteUser(id int) (int, error) {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = $1", usersTable)
	res, err := r.db.Exec(query, id)
	if err != nil {
		return 0, err
	}
	num, err := res.RowsAffected()
	if err != nil {
		return 0, err
	}
	return int(num), err
}

func (r *UsersPostgres) UpdateUser(id int, user model.User) (model.User, error) {
	query := fmt.Sprintf(
		"UPDATE %s SET "+
			"id=$1, u_login=$2, u_password=$3, firstname=$4, lastname=$5 "+
			"WHERE id=$6",
		usersTable)
	_, err := r.db.Exec(query, user.Id, user.Login, user.Password, user.Firstname, user.Lastname, id)
	return user, err
}
