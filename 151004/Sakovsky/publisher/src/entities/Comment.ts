import { CommentRequestToCreate } from "src/dto/request/CommentRequestToCreate";
import { Column, Entity, ManyToMany, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { Tweet } from "./Tweet";

@Entity({name: 'tbl_comments'})
export class Comment {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    @ManyToOne(() => Tweet, (tweet) => tweet.id)
    tweetId: number;

    @Column()
    content: string;

    @Column()
    country: string;

    setComment(dto: CommentRequestToCreate){
        this.content = dto.content;
        this.tweetId = dto.tweetId;
        this.country = 'Belarus';
    }
}