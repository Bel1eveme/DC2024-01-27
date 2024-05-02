import { forwardRef, Module } from '@nestjs/common';
import { CommentService } from './comment.service';
import { CommentController } from './comment.controller';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Comment } from '../entities/Comment';
import { TweetModule } from 'src/tweet/tweet.module';

@Module({
  imports: [TypeOrmModule.forFeature([Comment]), forwardRef(() => TweetModule)],
  providers: [CommentService],
  controllers: [CommentController],
  exports: [CommentService],
})
export class CommentModule {}
