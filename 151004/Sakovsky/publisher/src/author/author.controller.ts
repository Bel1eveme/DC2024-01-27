import { Body, Controller, Delete, Get, HttpCode, NotFoundException, Param, ParseIntPipe, Post, Put, UseInterceptors, UsePipes, ValidationPipe } from '@nestjs/common';
import { AuthorRequestToCreate } from '../dto/request/AuthorRequestToCreate';
import { AuthorResponseTo } from '../dto/response/AuthorResponseTo';
import { AuthorService } from './author.service';
import { AuthorRequestToUpdate } from '../dto/request/AuthorRequestToUpdate';
import { CacheInterceptor, CacheKey } from '@nestjs/cache-manager';
import { Author } from 'src/entities/Author';
import { authorCacheKeys } from 'src/utils/redis/globalRedis';

@UseInterceptors(CacheInterceptor)
@Controller('authors')
export class AuthorController {
    constructor(private readonly authorService: AuthorService ){}

    @CacheKey(authorCacheKeys.authors)
    @Get()
    async getAll(): Promise<AuthorResponseTo[]>{
        const authorsDto: AuthorResponseTo[]= [];
        const authors = await this.authorService.getAll();
        for (const author of authors) {
            const authorDto = new AuthorResponseTo();
            authorDto.id = author.id;
            authorDto.firstname = author.firstname;
            authorDto.lastname = author.lastname;
            authorDto.login = author.login;
            authorDto.password = author.password;
            authorsDto.push(authorDto);
        }

        return authorsDto;
    }

    @Get(':id')
    async getById(@Param('id', ParseIntPipe) id: number): Promise<Author>{
        const author = await this.authorService.getById(id);
        if(!author){
            throw new NotFoundException('User not found')
        }
        return author;
    }

    @UsePipes(new ValidationPipe())
    @Post()
    async createAuthor(@Body() dto: AuthorRequestToCreate): Promise<Author>{
        const author = await this.authorService.createAuthor(dto);
        return author;
    }

    @UsePipes(new ValidationPipe())
    @Put()
    async updateAuthor( @Body() authorReqDto: AuthorRequestToUpdate): Promise<Author>{
        const author = await this.authorService.updateAuthor(authorReqDto);
        return author;
    }

    @HttpCode(204)
    @Delete(':id')
    deleteAuthor(@Param('id', ParseIntPipe) id: number): Promise<void>{
        return this.authorService.deleteById(id);
    }
}
