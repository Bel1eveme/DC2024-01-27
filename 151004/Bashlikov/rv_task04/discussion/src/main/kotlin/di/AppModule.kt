package di

import domain.repository.IPostsRepository
import org.koin.dsl.module
import services.PostService
import services.impl.PostServiceImpl

internal val appModule = module {

    single<PostService> {
        val repository: IPostsRepository = get(postsRepositoryQualifier)

        PostServiceImpl(repository)
    }

}