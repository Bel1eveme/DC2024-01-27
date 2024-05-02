import { Column, Entity, JoinTable, ManyToMany, PrimaryGeneratedColumn } from "typeorm";
import { Tweet } from "./Tweet";

@Entity({name: 'tbl_markers'})
export class Marker {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @ManyToMany(() => Tweet)
    @JoinTable({ name: 'tbl_tweets_markers' })
    tweets: Tweet[];
}