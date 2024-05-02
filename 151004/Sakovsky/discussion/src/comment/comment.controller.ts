import {
    Body,
    Controller,
    Delete,
    Get,
    HttpCode,
    NotFoundException,
    Param,
    ParseIntPipe,
    Post,
    Put,
    UsePipes,
    ValidationPipe,
} from '@nestjs/common';
import { MessagePattern, Payload } from '@nestjs/microservices';
import { ADD_NEW_COMMENT, DELETE_COMMENT_BY_ID, GET_COMMENT_BY_ID, GET_COMMENT_LIST, UPDATE_COMMENT } from 'src/constants/constants';
import { Comment } from 'src/entites/Comment/Comment';
import { CommentService } from './comment.service';
import { CreateCommentDto } from './dto/CreateCommentDto';
import { UpdateCommentDto } from './dto/UpdateCommentDto';
import { DeleteKafakaMessage } from './interfaces/deleteKafkaMessage.interface';
import { GetKafakaMessage } from './interfaces/getByIdKafkaMessage';
import { UpdateKafkaMessage } from './interfaces/updateKafkaMessage';

@Controller('comments')
export class CommentController {
    constructor(private readonly commentService: CommentService) {}

    
    @MessagePattern(GET_COMMENT_LIST)
    async getAll(): Promise<Comment[]> {
        const comments = await this.commentService.getAll();
        return comments;
    }
    @Get()
    async getAllRest(){
        return this.getAll()
    }

    
    @MessagePattern(GET_COMMENT_BY_ID)
    async getById(@Payload() message: GetKafakaMessage) {
        const comment = await this.commentService.getById(message.commentId);
        if (!comment) {
            return null;
        }
        return JSON.stringify(comment);
    }

    @Get(':id')
    async getByIdRest(@Param('id', ParseIntPipe) id: number){
        const comment = await this.commentService.getById(id);
        if (!comment) {
            
            throw new NotFoundException('Comment not found');
        }
        return comment;
    }
   
    @UsePipes(new ValidationPipe())
    @Post()
    async createComentRest(
      @Body() commentReqDto: CreateCommentDto,
    ): Promise<Comment> {
      const comment = await this.commentService.createComment(commentReqDto);
      return comment;
    }

    @MessagePattern(ADD_NEW_COMMENT)
    async createComment(@Payload() message: CreateCommentDto) {
        const comment = await this.commentService.createComment(message);
        return JSON.stringify(comment);
    }

    @MessagePattern(UPDATE_COMMENT)
    async updateComment(@Payload() message: UpdateKafkaMessage) {
        const comment = await this.commentService.updateComment(message);
        return JSON.stringify(comment);
    }

    @UsePipes(new ValidationPipe())
    @Put()
    async updateCommentRest(@Body() commentDto: UpdateCommentDto){
        const comment = await this.commentService.updateComment(commentDto);
        return comment;
    }
    
    @MessagePattern(DELETE_COMMENT_BY_ID)
    deleteComment(@Payload() message: DeleteKafakaMessage) {
        return this.commentService.deleteById(message.commentId);
    }

    @HttpCode(204)
    @Delete(':id')
    async deleteCommentRest (@Param('id', ParseIntPipe) id: number): Promise<void> {
        await this.commentService.deleteById(id);
        return;
    }
}
