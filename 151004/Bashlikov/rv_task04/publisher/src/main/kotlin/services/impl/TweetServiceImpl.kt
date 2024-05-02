package services.impl

import by.bashlikovvv.api.dto.mapper.TweetEntityToTweetDtoMapper
import by.bashlikovvv.api.dto.request.CreateTweetDto
import by.bashlikovvv.api.dto.request.UpdateTweetDto
import by.bashlikovvv.api.dto.response.TweetDto
import data.mapper.CreateTweetDtoToTweetMapper
import data.mapper.UpdateTweetDtoToTweetMapper
import domain.exception.ApplicationExceptions
import domain.repository.ITweetsRepository
import services.TweetService

class TweetServiceImpl(
    private val tweetRepository: ITweetsRepository
) : TweetService {

    private val mapper = TweetEntityToTweetDtoMapper()

    override suspend  fun create(createTweetDto: CreateTweetDto): TweetDto {
        val tweetEntity = CreateTweetDtoToTweetMapper().mapFromEntity(createTweetDto)
        val id = tweetRepository.create(tweetEntity)

        return mapper.mapFromEntity(tweetEntity.copy(id = id))
    }

    override suspend  fun getAll(): List<TweetDto?> {
        return tweetRepository.readAll()
            .filterNotNull()
            .map { mapper.mapFromEntity(it) }
    }

    override suspend  fun getById(tweetId: Long): TweetDto? {
        val tweetEntity = tweetRepository.read(tweetId) ?: return null

        return mapper.mapFromEntity(tweetEntity)
    }

    override suspend  fun getByEditorId(editorId: Long): TweetDto? {
        val tweetEntity = tweetRepository.readBYEditorId(editorId).first() ?: return null

        return mapper.mapFromEntity(tweetEntity)
    }

    override suspend  fun update(tweetId: Long, updateTweetDto: UpdateTweetDto): TweetDto? {
        var tweetEntity = tweetRepository.read(tweetId) ?: return null
        tweetEntity = UpdateTweetDtoToTweetMapper(tweetEntity).mapFromEntity(updateTweetDto)
        if (!tweetRepository.update(tweetId, tweetEntity)) {
            throw ApplicationExceptions.UpdateException("Exception during tweet updating")
        }

        return mapper.mapFromEntity(tweetEntity)
    }

    override suspend  fun delete(tweetId: Long): Boolean {
        return tweetRepository.delete(tweetId)
    }

}