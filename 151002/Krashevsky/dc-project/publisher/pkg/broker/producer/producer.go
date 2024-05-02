package producer

import "github.com/IBM/sarama"

const inTopic = "InTopic"

func New(urls []string) (sarama.SyncProducer, error) {
	config := sarama.NewConfig()
	config.Producer.Return.Successes = true
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Retry.Max = 5

	producer, err := sarama.NewSyncProducer(urls, config)
	if err != nil {
		return nil, err
	}
	return producer, nil
}

func CreateMessage(partition int, bytes []byte) *sarama.ProducerMessage {
	return &sarama.ProducerMessage{
		Topic: inTopic,
		Key:   sarama.StringEncoder(rune(partition)),
		Value: sarama.ByteEncoder(bytes),
	}
}
