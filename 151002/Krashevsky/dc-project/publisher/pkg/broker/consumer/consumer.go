package consumer

import "github.com/IBM/sarama"

const outTopic = "OutTopic"

func New(urls []string) (sarama.Consumer, error) {
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true

	consumer, err := sarama.NewConsumer(urls, config)
	if err != nil {
		return nil, err
	}
	return consumer, nil
}
