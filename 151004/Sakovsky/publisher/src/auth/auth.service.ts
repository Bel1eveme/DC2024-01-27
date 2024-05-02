import { BadRequestException, Injectable, NotFoundException, UnauthorizedException } from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import { AuthorService } from "src/author/author.service";
import { AuthorRequestToCreate } from "src/dto/request/AuthorRequestToCreate";
import * as bcrypt from "bcrypt";
import { LoginDto } from "src/dto/request/LoginDto";

@Injectable()
export class AuthService {
    constructor(
        private readonly authorService: AuthorService,
        private readonly jwtService: JwtService,
    ) {}

    async register(dto: AuthorRequestToCreate){
        const { firstname, lastname, login, password } = dto;
        const hashedPassword = await bcrypt.hash(password, 12);
        try {
           const user = await this.authorService.createAuthor({
                login,
                firstname,
                lastname,
                password: hashedPassword,
            });
            const jwt = await this.jwtService.signAsync({
                id: user.id,
                login: user.login,
                firstName: user.firstname,
                lastName: user.lastname,
            });
            return jwt
        } catch (error) {
            throw new BadRequestException(
                `User with logn "${login} is already in use"`,
            );
        }
    }

    async login(loginDto: LoginDto){
        const { login, password } = loginDto;
        try {
            const user = await this.authorService.getByLogin(login);

            if (!(await bcrypt.compare(password, user.password))) {
                throw new BadRequestException("Неверный логин или пароль");
            }

            const jwt = await this.jwtService.signAsync({
                id: user.id,
                login: user.login,
                firstname: user.firstname,
                lastname: user.lastname,
            });

            return jwt;
        } catch (error) {
            throw new BadRequestException("Неверный логин или пароль");
        }
    }

    async me(jwt: string){
        let data: any;
        try {
            data = await this.jwtService.verifyAsync(jwt);
            if (!data) {
                throw new UnauthorizedException();
            }
        } catch (error) {
            throw new UnauthorizedException();
        }
        
        try {
            const user = await this.authorService.getById(data.id);
            const {password, ...result} = user;

            return result;
        } catch (error) {
            throw new NotFoundException(`Author whitd id ${data.id} not found`);
        }
    }
}
