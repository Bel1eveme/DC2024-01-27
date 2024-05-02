
import api.controller.configureRouting
import api.controller.configureSerialization
import di.appModule
import di.dataModule
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.time.Duration
import kotlin.concurrent.thread

fun Application.configureDiscussionModule() {
    install(DoubleReceive)
    configureKoin()
    configureSerialization()
    configureRouting()

    val consumer: KafkaConsumer<String, String> by inject()
    consumer.let {
        it.subscribe(listOf("TestTopic"))

        thread {
            while (true) {
                val records = it.poll(Duration.ofMillis(100))
                records.forEach { println(it.value()) }
            }
        }
    }
}

internal fun Application.configureKoin() {
    install(Koin) {
        modules(dataModule, appModule)
    }
}