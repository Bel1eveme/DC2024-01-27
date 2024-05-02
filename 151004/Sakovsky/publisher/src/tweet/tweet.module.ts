import { forwardRef, Module } from '@nestjs/common';
import { TweetService } from './tweet.service';
import { TweetController } from './tweet.controller';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Tweet } from '../entities/Tweet';
import { Author } from 'src/entities/Author';
import { CommentModule } from 'src/comment/comment.module';

@Module({
  imports: [TypeOrmModule.forFeature([Tweet, Author]), forwardRef(() => CommentModule)],
  providers: [TweetService],
  controllers: [TweetController],
  exports: [TweetService]
})
export class TweetModule {}
