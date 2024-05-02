import { IsNumber, IsOptional, IsString, Length } from "class-validator";

export class CommentRequestToCreate{
    @IsOptional()
    id?: number;

    @IsNumber()
    tweetId: number;

    @IsString()
    @Length(2, 2048)
    content: string;
}