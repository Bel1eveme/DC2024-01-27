import { CacheInterceptor, CacheKey } from "@nestjs/cache-manager";
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
    UseInterceptors,
    UsePipes,
    ValidationPipe,
} from "@nestjs/common";
import { Comment } from "src/entities/Comment";
import { CommentRequestToCreate } from "../dto/request/CommentRequestToCreate";
import { CommentRequestToUpdate } from "../dto/request/CommentRequestToUpdate";
import { CommentResponseTo } from "../dto/response/CommentResponseTo";
import { CommentService } from "./comment.service";
import { commentCacheKeys } from "src/utils/redis/globalRedis";

@UseInterceptors(CacheInterceptor)
@Controller("comments")
export class CommentController {
    constructor(private readonly commentService: CommentService) {}

    @CacheKey(commentCacheKeys.comments)
    @Get()
    getAll() {
        const comments = this.commentService.getAll();
        return comments;
    }

    @HttpCode(200)
    @Get(":id")
    async getById(
        @Param("id", ParseIntPipe) id: number,
    ): Promise<CommentResponseTo> {
        const comment = await this.commentService.getById(id);
        if (!comment) {
            throw new NotFoundException("Comment not found");
        }
        return comment;
    }

    @UsePipes(new ValidationPipe())
    @Post()
    async createComent(@Body() commentReqDto: CommentRequestToCreate) {
        const comment = await this.commentService.createComment(commentReqDto);
        return comment;
    }

    @UsePipes(new ValidationPipe())
    @Put()
    async updateComment(
        @Body() commentReqDto: CommentRequestToUpdate,
    ): Promise<Comment> {
        const comment = await this.commentService.updateComment(commentReqDto);
        return comment;
    }

    @HttpCode(204)
    @Delete(":id")
    async deleteComment(@Param("id", ParseIntPipe) id: number) {
        const response = await this.commentService.deleteById(id);
        if (String(response) === 'false') {
            throw new NotFoundException(`Comment with id: ${id} not found`);
        }
        return ;
    }
}
