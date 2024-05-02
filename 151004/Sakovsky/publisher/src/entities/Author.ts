import { Column, Entity, JoinColumn, JoinTable, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Tweet } from "./Tweet";

@Entity({name: 'tbl_authors'})
export class Author {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({unique: true})
    login: string;

    @Column()
    password: string;

    @Column()
    firstname: string;

    @Column()
    lastname: string;

    @OneToMany(()=>Tweet, tweet => tweet.author)
    tweets: Tweet[];
}