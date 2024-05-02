import { HttpException, Injectable, NotFoundException } from '@nestjs/common';
import { Client, mapping } from 'cassandra-driver';
import { plainToInstance } from 'class-transformer';
import { CassandraService } from 'src/cassandra/cassandra.service';
import { Comment } from 'src/entites/Comment/Comment';
import { CreateCommentDto } from './dto/CreateCommentDto';
import { UpdateCommentDto } from './dto/UpdateCommentDto';

@Injectable()
export class CommentService {
    private commentMapper: mapping.ModelMapper<Comment>;
    private client: Client;

    constructor(cassandraService: CassandraService) {
        this.commentMapper = cassandraService
            .createMapper({
                models: {
                    Comment: {
                        tables: ['tbl_comment'],
                    },
                },
            })
            .forModel('Comment');
        this.client = cassandraService.client;
    }

    async getAll(): Promise<Comment[]> {
        return (await this.commentMapper.findAll()).toArray();
    }

    async createComment(commentDto: CreateCommentDto): Promise<Comment> {
        let comment = plainToInstance(Comment, commentDto);
        try {
            comment.country = 'Belarus';
            await this.commentMapper.insert(comment);
        } catch (error) {
            throw new HttpException(
                `Tweet с id "${commentDto.tweetId} уже существует" `,
                403,
            );
        }
        return comment;
    }

    async getById(id: number): Promise<Comment> {
        const comment = (
            await this.client.execute(
                `SELECT * FROM tbl_comment WHERE id = ? ALLOW FILTERING`,
                [id],
                { prepare: true },
            )
        ).rows[0];
        return comment as unknown as Comment;
    }

    async deleteById(id: number) {
        try {
            const comment = await this.getById(id);
            if(!comment) throw new Error();
            await this.commentMapper.remove(comment);
        } catch (error) {
            return false
        }
        return true;
    }

    async updateComment(commentDto: UpdateCommentDto): Promise<Comment> {
        let comment: Comment;
        try {
            comment = await this.getById(commentDto.id);
            if (!comment) {
                throw new NotFoundException(
                    `Comment with id: ${commentDto.id} not found`,
                );
            }
            comment.content = commentDto.content;
            comment.tweetId = commentDto.tweetId;
            (await this.commentMapper.update(comment)).first();
        } catch (error) {
            throw new Error('Беда, откуда не ждали');
        }

        return comment as unknown as Comment;
    }
}
