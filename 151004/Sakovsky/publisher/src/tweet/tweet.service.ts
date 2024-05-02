import {
    forwardRef,
    HttpException,
    Inject,
    Injectable,
    NotFoundException
} from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { firstValueFrom } from "rxjs";
import { CommentService } from "src/comment/comment.service";
import { Comment } from "src/entities/Comment";
import { Repository } from "typeorm";
import { TweetRequestToCreate } from "../dto/request/TweetRequestToCreate";
import { TweetRequestToUpdate } from "../dto/request/TweetRequestToUpdate";
import { Tweet } from "../entities/Tweet";
import { GetAllQuery } from "./tweet.controller";

@Injectable()
export class TweetService {
    constructor(
        @InjectRepository(Tweet) private tweetRepository: Repository<Tweet>,
        @Inject(forwardRef(() => CommentService))
        private readonly comentServise: CommentService,
        // @Inject(CACHE_MANAGER) private cacheManager: Cache,
    ) {}

    async getAll(query?: GetAllQuery): Promise<[Tweet[], Comment[]]> {
        const limit = query?.limit ?? 5;
        const page = query?.page;
        let tweets: Tweet[] = [];
        if (limit && page) {
            const skip = (page - 1) * limit;
            tweets = await this.tweetRepository.find({
                relations: ["author"],
                take: limit,
                skip,
                order: {
                    created: "DESC",
                },
            });
        } else {
            tweets = await this.tweetRepository.find({
                relations: ["author"],
                order: {
                    created: "DESC",
                },
            });
        }
        const comments = await firstValueFrom(this.comentServise.getAll());
        return [tweets, comments];
    }

    async getCount() {
        return (await this.tweetRepository.find()).length;
    }

    async createTweet(tweetDto: TweetRequestToCreate): Promise<Tweet> {
        const tweetToCreate = new Tweet();
        tweetToCreate.setTweet(tweetDto);
        let tweet = new Tweet();
        try {
            tweet = await this.tweetRepository.save(tweetToCreate);

            // const allTweets = await this.getAll();

            // this.cacheManager.set(tweetCacheKeys.tweets, allTweets, TTL);
        } catch (error) {
            throw new HttpException(`Tweet с title "${tweetDto.title}"`, 403);
        }
        return tweet;
    }

    getById(id: number): Promise<Tweet> {
        return this.tweetRepository.findOneBy({ id });
    }

    async deleteById(id: number): Promise<void> {
        try {
            await this.tweetRepository.findOneByOrFail({ id });
            // let allTweets = await this.cacheManager.get<Tweet[]>(
            //     tweetCacheKeys.tweets,
            // );
            // if (allTweets) {
            //     allTweets = allTweets?.filter((tweet) => tweet.id !== id);
            //     this.cacheManager.set(tweetCacheKeys.tweets, allTweets, TTL);
            //     this.cacheManager.del(tweetCacheKeys.tweets + id);
            // }
        } catch (error) {
            throw new NotFoundException(`Tweet with id: ${id} not found`);
        }
        await this.tweetRepository.delete(id);
    }

    async updateTweet(tweetDto: TweetRequestToUpdate): Promise<Tweet> {
        const tweet = await this.tweetRepository.findOneBy({ id: tweetDto.id });
        if (!tweet) {
            throw new NotFoundException(
                `Tweet with id: ${tweetDto.id} not found`,
            );
        }
        tweet.content = tweetDto.content;
        tweet.authorId = tweetDto.authorId;
        tweet.title = tweetDto.title;
        tweet.modified = new Date();

        // let allTweets = await this.cacheManager.get<Tweet[]>(
        //     tweetCacheKeys.tweets,
        // );
        // allTweets = allTweets.map((tweet) => {
        //     if (tweet.id === tweetDto.id) {
        //         tweet.content = tweetDto.content;
        //         tweet.authorId = tweetDto.authorId;
        //         tweet.title = tweetDto.title;
        //     }
        //     return tweet;
        // });
        // this.cacheManager.set(tweetCacheKeys.tweets, allTweets, TTL);
        // this.cacheManager.set(tweetCacheKeys.tweets + tweet.id, tweet, TTL);

        return this.tweetRepository.save(tweet);
    }
}
