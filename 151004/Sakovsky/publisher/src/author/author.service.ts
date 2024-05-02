import { CACHE_MANAGER } from "@nestjs/cache-manager";
import {
    HttpException,
    Inject,
    Injectable,
    NotFoundException,
} from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Cache } from "cache-manager";
import { AuthorRequestToCreate } from "src/dto/request/AuthorRequestToCreate";
import { authorCacheKeys, TTL } from "src/utils/redis/globalRedis";
import { Repository } from "typeorm";
import { AuthorRequestToUpdate } from "../dto/request/AuthorRequestToUpdate";
import { Author } from "../entities/Author";

@Injectable()
export class AuthorService {
    constructor(
        @InjectRepository(Author)
        private authorRepositoty: Repository<Author>,
        @Inject(CACHE_MANAGER) private cacheManager: Cache,
    ) {}

    getAll(): Promise<Author[]> {
        return this.authorRepositoty.find();
    }

    async createAuthor(authorDto: AuthorRequestToCreate): Promise<Author> {
        let author = new Author();
        try {
            author = await this.authorRepositoty.save(authorDto);

            const allAuthors = await this.getAll();
            this.cacheManager.set(authorCacheKeys.authors, allAuthors, TTL);
        } catch (error) {
            throw new HttpException(
                `Пользователь с login "${authorDto.login}" уже существует`,
                403,
            );
        }
        return author;
    }

    async getById(id: number): Promise<Author> {
        const response = await this.authorRepositoty.find({where: {id}, relations: ['tweets']});
        return response[0];
    }

    getByLogin(login: string){
        return this.authorRepositoty.findOneByOrFail({login})
    }

    async deleteById(id: number): Promise<void> {
        try {
            await this.authorRepositoty.findOneByOrFail({ id });
        } catch (error) {
            throw new NotFoundException(`Author with id: ${id} not found`);
        }
        try {
            await this.authorRepositoty.delete(id);

            let allAuthors = await this.cacheManager.get<Author[]>(
                authorCacheKeys.authors,
            );
            allAuthors = allAuthors.filter((author) => author.id !== id);
            this.cacheManager.set(authorCacheKeys.authors, allAuthors, TTL);
            this.cacheManager.del(authorCacheKeys.authors + id);
        } catch (error) {
            throw new Error("zz");
        }
    }

    async updateAuthor(authorDto: AuthorRequestToUpdate): Promise<Author> {
        const author = await this.authorRepositoty.findOneBy({
            id: authorDto.id,
        });
        if (!author) {
            throw new NotFoundException(
                `Author with id: ${authorDto.id} not found`,
            );
        }
        author.firstname = authorDto.firstname;
        author.lastname = authorDto.lastname;
        author.login = authorDto.login;
        author.password = authorDto.password;

        try {
            this.authorRepositoty.save(author);

            let allAuthors = await this.cacheManager.get<Author[]>(
                authorCacheKeys.authors,
            );
            allAuthors = allAuthors.map((author) => {
                if (author.id === authorDto.id) {
                    author.firstname = authorDto.firstname;
                    author.lastname = authorDto.lastname;
                    author.login = authorDto.login;
                    author.password = author.password;
                }
                return author;
            });
            this.cacheManager.set(authorCacheKeys.authors, allAuthors, TTL);
            this.cacheManager.set(
                authorCacheKeys.authors + author.id,
                author,
                TTL,
            );
        } catch (error) {
            throw new Error();
        }

        return author;
    }
}
