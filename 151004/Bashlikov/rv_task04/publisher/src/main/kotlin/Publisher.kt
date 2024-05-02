
import api.controller.configureRouting
import api.controller.configureSerialization
import di.appModule
import di.dataModule
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.ktor.plugin.Koin
import util.connectToPostgres
import java.sql.Connection

fun Application.configurePublisherModule() {
    install(DoubleReceive)
    configureKoin()
    configureSerialization()
    configureRouting()
}

internal fun Application.configureKoin() {
    install(Koin) {
        dataModule.single<Connection> { connectToPostgres(embedded = true) }
        modules(dataModule, appModule)
    }
}

fun sendViaKafka(producer: KafkaProducer<String, String>, message: String) {
    val topic = "TestTopic"
    val record = ProducerRecord<String, String>(topic, message)
    producer.send(record)
}