package di

import by.bashlikovvv.domain.repository.ITagsRepository
import by.bashlikovvv.services.TagService
import by.bashlikovvv.services.impl.TagServiceImpl
import domain.repository.IEditorsRepository
import domain.repository.ITweetsRepository
import org.koin.dsl.module
import services.EditorService
import services.TweetService
import services.impl.EditorServiceImpl
import services.impl.TweetServiceImpl

val appModule = module {

    single<TweetService> {
        val repository: ITweetsRepository = get(tweetsRepositoryQualifier)

        TweetServiceImpl(repository)
    }

    single<EditorService> {
        val repository: IEditorsRepository = get(editorsRepositoryQualifier)

        EditorServiceImpl(repository)
    }

    single<TagService> {
        val repository: ITagsRepository = get(tagsRepositoryQualifier)

        TagServiceImpl(repository)
    }

}