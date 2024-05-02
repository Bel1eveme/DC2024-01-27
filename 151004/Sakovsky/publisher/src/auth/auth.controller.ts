import {
    BadRequestException,
    Body,
    Controller,
    Get,
    NotFoundException,
    Post,
    Req,
    Res,
    UnauthorizedException,
    UsePipes,
    ValidationPipe,
} from "@nestjs/common";
import { JwtService } from "@nestjs/jwt";
import * as bcrypt from "bcrypt";
import { Request, Response } from "express";
import { AuthorService } from "src/author/author.service";
import { AuthorRequestToCreate } from "src/dto/request/AuthorRequestToCreate";
import { LoginDto } from "src/dto/request/LoginDto";
import { Author } from "src/entities/Author";
import { AuthService } from "./auth.service";

@Controller("auth")
export class AuthController {
    constructor(
        private jwtService: JwtService,
        private readonly authorService: AuthorService,
        private readonly authService: AuthService,
    ) {}

    @UsePipes(new ValidationPipe())
    @Post("register")
    async register(
        @Body() dto: AuthorRequestToCreate,
        @Res({ passthrough: true }) response: Response,
    ) {
        const jwt = await this.authService.register(dto);
        response.cookie("jwt", jwt, { httpOnly: true });
        return { accessToken: jwt };
    }

    @Post("login")
    async login(
        @Body() dto: LoginDto,
        @Res({ passthrough: true }) response: Response,
    ) {
        const jwt = await this.authService.login(dto);
        response.cookie("jwt", jwt, { httpOnly: true });

        return { accessToken: jwt };
    }

    @Post("logout")
    async logout(@Res({ passthrough: true }) response: Response) {
        response.clearCookie("jwt");
    }

    @Get("me")
    async me(@Req() request: Request) {
        const jwt = request.cookies["jwt"];
        const user = this.authService.me(jwt);

        return user;
    }
}
