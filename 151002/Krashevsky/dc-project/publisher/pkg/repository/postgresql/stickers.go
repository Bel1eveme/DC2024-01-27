package postgresql

import (
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"publisher/pkg/model"
)

type StickersPostgres struct {
	db *sqlx.DB
}

func NewStickersPostgres(db *sqlx.DB) *StickersPostgres {
	return &StickersPostgres{db: db}
}

func (r *StickersPostgres) CreateSticker(sticker model.Sticker) (model.Sticker, error) {
	var id int
	query := fmt.Sprintf("INSERT INTO %s (st_name) values ($1) RETURNING id", stickersTable)
	row := r.db.QueryRow(query, sticker.Name)
	if err := row.Scan(&id); err != nil {
		return sticker, err
	}
	sticker.Id = id
	return sticker, nil
}

func (r *StickersPostgres) GetAllStickers() ([]model.Sticker, error) {
	stickers := make([]model.Sticker, 0)
	query := fmt.Sprintf("SELECT * FROM %s", stickersTable)
	err := r.db.Select(&stickers, query)
	return stickers, err
}

func (r *StickersPostgres) GetStickerById(id int) (model.Sticker, error) {
	sticker := make([]model.Sticker, 0)
	query := fmt.Sprintf("SELECT * FROM %s WHERE id = $1", stickersTable)
	err := r.db.Select(&sticker, query, id)
	if len(sticker) == 0 {
		return model.Sticker{}, errors.New("sticker with specified id not found")
	}
	return sticker[0], err
}

func (r *StickersPostgres) DeleteSticker(id int) (int, error) {
	query := fmt.Sprintf("DELETE FROM %s WHERE id = $1", stickersTable)
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

func (r *StickersPostgres) UpdateSticker(id int, sticker model.Sticker) (model.Sticker, error) {
	query := fmt.Sprintf(
		"UPDATE %s SET "+
			"id=$1, st_name=$2 "+
			"WHERE id=$3",
		stickersTable)
	_, err := r.db.Exec(query, sticker.Id, sticker.Name, id)
	return sticker, err
}
