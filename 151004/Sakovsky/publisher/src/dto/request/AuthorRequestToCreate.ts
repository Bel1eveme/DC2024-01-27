import { IsNumber, IsString, Length } from "class-validator";

export class AuthorRequestToCreate {
    @IsString()
    @Length(2, 64)
    login: string;

    @IsString()
    @Length(8, 128)
    password: string;

    @IsString()
    @Length(2, 64)
    firstname: string;

    @IsString()
    @Length(2, 64)
    lastname: string;
}