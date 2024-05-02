import { IsNumber, IsString, Length } from "class-validator";

export class CreateCommentDto{
    @IsNumber()
    id: number;

    @IsNumber()
    tweetId: number;

    @IsString()
    @Length(2, 2048)
    content: string;

}