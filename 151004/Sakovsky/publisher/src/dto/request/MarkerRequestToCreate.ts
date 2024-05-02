import { IsNumber, IsString, Length } from "class-validator";

export class MarkerRequestToCreate {
    @IsString()
    @Length(2, 32)
    name: string;

}