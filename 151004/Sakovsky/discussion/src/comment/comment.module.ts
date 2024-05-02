import { Module } from '@nestjs/common';
import { CommentController } from './comment.controller';
import { CommentService } from './comment.service';
import { CassandraModule } from 'src/cassandra/cassandra.module';

@Module({
  imports: [CassandraModule],
  controllers: [CommentController],
  providers: [CommentService]
})
export class CommentModule {}
