import { CacheInterceptor, CacheKey } from '@nestjs/cache-manager';
import { Body, Controller, Delete, Get, HttpCode, NotFoundException, Param, ParseIntPipe, Post, Put, Query, UseInterceptors, UsePipes, ValidationPipe } from '@nestjs/common';
import { Tweet } from 'src/entities/Tweet';
import { tweetCacheKeys } from 'src/utils/redis/globalRedis';
import { TweetRequestToCreate } from '../dto/request/TweetRequestToCreate';
import { TweetRequestToUpdate } from '../dto/request/TweetRequestToUpdate';
import { TweetResponseTo } from '../dto/response/TweetResponseTo';
import { TweetService } from './tweet.service';
export type GetAllQuery = {
    limit: number,
    page: number,
}

// @UseInterceptors(CacheInterceptor)
@Controller('tweets')
export class TweetController {
    constructor(private readonly tweetService: TweetService ){}

    @CacheKey(tweetCacheKeys.tweets)
    @Get()
    async getAll(@Query() query: GetAllQuery){
        const tweetsDto: TweetResponseTo[]= [];
        const [tweets, comments] = await this.tweetService.getAll(query);
        for (const tweet of tweets) {
            const tweetDto = new TweetResponseTo();
            tweetDto.id = tweet.id;
            const author = tweet.author;
            delete author.password;
            tweetDto.author = author;
            tweetDto.content = tweet.content;
            tweetDto.title = tweet.title;
            tweetDto.created = tweet.created;
            tweetDto.modified = tweet.modified;
            tweetDto.comments = [];
            for (const comment of comments) {
                if(comment.tweetId === tweet.id){
                    tweetDto.comments.push(comment);
                }
            }
            tweetsDto.push(tweetDto);
        }
        const total = await this.tweetService.getCount();
        return {tweets: tweetsDto,
            total: total,
        };
    }

    
    @Get(':id')
    async getById(@Param('id', ParseIntPipe) id: number): Promise<Tweet>{
        const tweet = await this.tweetService.getById(id);
        if(!tweet){
            throw new NotFoundException('Tweet not found')
        }
        return tweet;
    }

    @UsePipes(new ValidationPipe())
    @Post()
    async createTweet(@Body() tweetReqDto: TweetRequestToCreate): Promise<Tweet>{
        const tweet = await this.tweetService.createTweet(tweetReqDto);
        return tweet;
    }

    @UsePipes(new ValidationPipe())
    @Put()
    async updateTweet(@Body() tweetReqDto: TweetRequestToUpdate): Promise<Tweet>{
        const tweet = await this.tweetService.updateTweet( tweetReqDto);
        return tweet;
    }

    @HttpCode(204)
    @Delete(':id')
    deleteTweet(@Param('id', ParseIntPipe) id: number): Promise<void>{
        return this.tweetService.deleteById(id);
    }
}
