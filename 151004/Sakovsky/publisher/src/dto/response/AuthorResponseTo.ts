import { IsNumber, IsString, Length } from "class-validator";

export class AuthorResponseTo {
    id: number;
    login: string;
    password: string;
    firstname: string;
    lastname: string;
}