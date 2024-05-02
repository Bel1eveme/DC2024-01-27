package di

import com.datastax.driver.core.Cluster
import data.local.dao.PostOfflineSource
import data.repository.PostsRepository
import domain.repository.IPostsRepository
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.util.*

internal val postsRepositoryQualifier = StringQualifier("posts_repository")

internal val dataModule = module {

    //cqlsh
    //CREATE KEYSPACE distcomp WITH REPLICATION = {'class' : 'NetworkTopologyStrategy', 'datacenter1' : 3};
    //CREATE TABLE distcomp.tbl_post_by_country (country text, tweet_id bigint, id bigint, content text, PRIMARY KEY ((country), tweet_id, id));
    single<Cluster> {
        Cluster.builder()
            .withoutMetrics()
            .addContactPoints("127.0.0.1")
            .build()
    }

    single {
        val bootstrapServers = "localhost:9092"
        val topic = "TestTopic"

        val consumerProps = Properties()
        consumerProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        consumerProps[ConsumerConfig.GROUP_ID_CONFIG] = "app-id"

        KafkaConsumer<String, String>(consumerProps)
    }

    single<PostOfflineSource> {
        val cluster: Cluster = get()

        PostOfflineSource(cluster.connect("distcomp"))
    }
    
    single<IPostsRepository>(postsRepositoryQualifier) {
        val postOfflineSource: PostOfflineSource = get()

        PostsRepository(postOfflineSource)
    }

}