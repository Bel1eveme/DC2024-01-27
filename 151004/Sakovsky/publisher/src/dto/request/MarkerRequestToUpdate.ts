import { IsNumber, IsString, Length } from "class-validator";

export class MarkerRequestToUpdate {
    @IsNumber()
    id: number;
    
    @IsString()
    @Length(2, 32)
    name: string;

}