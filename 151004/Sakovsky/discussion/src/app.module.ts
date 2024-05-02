import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { CassandraModule } from './cassandra/cassandra.module';
import { CommentModule } from './comment/comment.module';

@Module({
  imports: [CassandraModule, CommentModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
