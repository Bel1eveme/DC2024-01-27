using Confluent.Kafka;
using Forum.Api.Kafka.Consumer;
using Forum.Api.Kafka.Producer;
using Microsoft.Extensions.Options;

namespace Forum.Api.Kafka
{
    public static class RegisterServiceExtensions
    {
        public static void AddKafkaMessageBus(this IServiceCollection serviceCollection)
        {
            serviceCollection.AddSingleton(typeof(IKafkaMessageBus<,>), typeof(KafkaMessageBus<,>));
        }

        public static void AddKafkaConsumer<Tk, Tv, THandler>(this IServiceCollection services,
            Action<KafkaConsumerConfig<Tk, Tv>> configAction) where THandler : class, IKafkaHandler<Tk, Tv>
        {
            services.AddScoped<IKafkaHandler<Tk, Tv>, THandler>();

            services.Configure(configAction);
            
            services.AddHostedService<BackGroundKafkaConsumer<Tk, Tv>>();
        }

        public static void AddKafkaProducer<Tk, Tv>(this IServiceCollection services,
            Action<KafkaProducerConfig<Tk, Tv>> configAction)
        {
            services.AddConfluentKafkaProducer<Tk, Tv>();

            services.AddSingleton<KafkaProducer<Tk, Tv>>();

            services.Configure(configAction);
        }

        private static void AddConfluentKafkaProducer<Tk, Tv>(this IServiceCollection services)
        {
            services.AddSingleton(
                sp =>
                {
                    var config = sp.GetRequiredService<IOptions<KafkaProducerConfig<Tk, Tv>>>();

                    var builder = new ProducerBuilder<Tk, Tv>(config.Value).SetValueSerializer(new KafkaSerializer<Tv>());

                    return builder.Build();
                });
        }
    }
}