import { IsNumber, IsString, Length } from "class-validator";

export class TweetRequestToCreate {
    @IsNumber()
    authorId: number;

    @IsString()
    @Length(2, 64)
    title: string;

    @IsString()
    @Length(4, 2048)
    content: string;

}