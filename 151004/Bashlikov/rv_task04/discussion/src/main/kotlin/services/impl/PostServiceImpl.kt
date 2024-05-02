package services.impl

import api.dto.mapper.PostEntityToPostDtoMapper
import api.dto.request.CreatePostDto
import api.dto.request.UpdatePostDto
import api.dto.response.PostDto
import data.mapper.CreatePostDtoToPostMapper
import data.mapper.UpdatePostDtoToPostMapper
import domain.exception.ApplicationExceptions
import domain.repository.IPostsRepository
import services.PostService

class PostServiceImpl(
    private val postRepository: IPostsRepository
) : PostService {

    private val mapper = PostEntityToPostDtoMapper()

    override suspend fun create(createPostDto: CreatePostDto): PostDto {
        val postEntity = CreatePostDtoToPostMapper().mapFromEntity(createPostDto)
        val id = postRepository.create(postEntity)

        return mapper.mapFromEntity(postEntity.copy(id = id))
    }

    override suspend fun update(postId: Long, updatePostDto: UpdatePostDto): PostDto {
        val postEntity = UpdatePostDtoToPostMapper().mapFromEntity(updatePostDto)
        if (!postRepository.update(postId, postEntity)) {
            throw ApplicationExceptions.UpdateException("Exception during post updating")
        }

        return mapper.mapFromEntity(postEntity)
    }

    override suspend fun getById(postId: Long): PostDto? {
        val postEntity = postRepository.read(postId) ?: return null

        return mapper.mapFromEntity(postEntity)
    }

    override suspend fun getAll(): List<PostDto?> {
        return postRepository.readAll()
            .filterNotNull()
            .map { mapper.mapFromEntity(it) }
    }

    override suspend fun delete(postId: Long): Boolean {
        return postRepository.delete(postId)
    }

}