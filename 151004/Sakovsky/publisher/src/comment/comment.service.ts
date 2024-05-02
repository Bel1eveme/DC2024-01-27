import { CACHE_MANAGER } from "@nestjs/cache-manager";
import {
    forwardRef,
    HttpException,
    Inject,
    Injectable,
    NotFoundException,
} from "@nestjs/common";
import { Client, ClientKafka, Transport } from "@nestjs/microservices";
import { Cache } from "cache-manager";
import { plainToInstance } from "class-transformer";
import { firstValueFrom } from "rxjs";
import {
    ADD_NEW_COMMENT,
    DELETE_COMMENT_BY_ID,
    GET_COMMENT_BY_ID,
    GET_COMMENT_LIST,
    UPDATE_COMMENT,
} from "src/constants/constants";
import { TweetService } from "src/tweet/tweet.service";
import { commentCacheKeys, TTL } from "src/utils/redis/globalRedis";
import { CommentRequestToCreate } from "../dto/request/CommentRequestToCreate";
import { CommentRequestToUpdate } from "../dto/request/CommentRequestToUpdate";
import { Comment } from "../entities/Comment";

@Injectable()
export class CommentService {
    constructor(
        @Inject(CACHE_MANAGER) private cacheManager: Cache,
        @Inject(forwardRef(() => TweetService))
        private tweetService: TweetService,
    ) {}

    @Client({
        transport: Transport.KAFKA,
        options: {
            client: {
                clientId: "comment",
                brokers: ["localhost:9092"],
            },
            consumer: {
                groupId: "comment-consumer",
            },
        },
    })
    private client: ClientKafka;

    async onModuleInit() {
        this.client.subscribeToResponseOf(ADD_NEW_COMMENT);
        this.client.subscribeToResponseOf(GET_COMMENT_LIST);
        this.client.subscribeToResponseOf(GET_COMMENT_BY_ID);
        this.client.subscribeToResponseOf(UPDATE_COMMENT);
        this.client.subscribeToResponseOf(DELETE_COMMENT_BY_ID);

        await this.client.connect();
    }

    getAll() {
        const response = this.client.send<Comment[], any>(
            "get.comment.list",
            "",
        );
        return response;
    }

    async createComment(commentDto: CommentRequestToCreate) {
        let comment = new Comment();
        try {
            const tweet = await this.tweetService.getById(commentDto.tweetId);
            if (!tweet) {
                throw new NotFoundException(
                    `Tweet с id "${commentDto.tweetId} не существует" `,
                );
            }

            commentDto.id = Math.floor(Math.random() * (10000 - 100 + 1)) + 100;
            comment = await firstValueFrom(
                this.client.send<Comment, CommentRequestToCreate>(
                    "add.new.comment",
                    commentDto,
                ),
            );
            const allComments = await firstValueFrom(this.getAll());
            this.cacheManager.set(commentCacheKeys.comments, allComments, TTL);
        } catch (error) {
            throw new HttpException(
                `Tweet с id "${commentDto.tweetId} уже существует" `,
                403,
            );
        }
        return comment;
    }

    async getById(id: number) {
        try {
            return await firstValueFrom(
                this.client.send<Comment>("get.comment.byId", {
                    commentId: id,
                }),
            );
        } catch (error) {
            throw new HttpException(`Tweet с id "${id} не существует" `, 403);
        }
    }

    async deleteById(id: number) {
        try {
            const response = await firstValueFrom(
                this.client.send<boolean, any>("delete.comment.byId", {
                    commentId: id,
                }),
            );
            let allComments = await this.cacheManager.get<Comment[]>(
                commentCacheKeys.comments,
            );
            if (allComments) {
                allComments = allComments.filter(
                    (comment) => comment.id !== id,
                );
                this.cacheManager.set(
                    commentCacheKeys.comments,
                    allComments,
                    TTL,
                );
                this.cacheManager.del(commentCacheKeys.comments + id);
            }

            return response;
        } catch (error) {
            throw new NotFoundException(`Comment with id: ${id} not found`);
        }
    }

    async updateComment(commentDto: CommentRequestToUpdate): Promise<Comment> {
        try {
            const response = await firstValueFrom(
                this.client.send<Comment, CommentRequestToUpdate>(
                    "update.comment",
                    commentDto,
                ),
            );

            let allComments = await this.cacheManager.get<Comment[]>(
                commentCacheKeys.comments,
            );
            allComments = allComments.map((comment) => {
                if (comment.id === commentDto.id) {
                    comment.content = commentDto.content;
                    comment.tweetId = commentDto.tweetId;
                }
                return comment;
            });
            const comment = plainToInstance(Comment, commentDto);
            comment.country = "Belarus";
            this.cacheManager.set(commentCacheKeys.comments, allComments, TTL);
            this.cacheManager.set(
                commentCacheKeys.comments + commentDto.id,
                comment,
                TTL,
            );

            return response;
        } catch (error) {
            throw new NotFoundException(
                `Comment with id: ${commentDto.id} not found`,
            );
        }
    }
}
