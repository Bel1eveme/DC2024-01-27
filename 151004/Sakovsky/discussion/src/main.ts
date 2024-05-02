import { NestFactory } from '@nestjs/core';
import { MicroserviceOptions, Transport } from '@nestjs/microservices';
import { AppModule } from './app.module';

async function bootstrap() {
    const app = await NestFactory.create(AppModule);
    app.setGlobalPrefix('api/v1.0');
    await app.listen(24130);
    
    const kafka = await NestFactory.createMicroservice<MicroserviceOptions>(
        AppModule,
        {
            transport: Transport.KAFKA,
            options: {
                client: {
                    brokers: ['localhost:9092'],
                },
                consumer: {
                    groupId: 'comment-consumer',
                },
            },
        },
    );
    kafka.listen().then(() => console.log('Comment service is listening'));
}
bootstrap();
