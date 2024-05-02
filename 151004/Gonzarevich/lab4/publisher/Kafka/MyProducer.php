<?php

namespace App\Kafka;

class MyProducer {

    private \RdKafka\Producer $producer;
    private \RdKafka\ProducerTopic $topic;

    public function __construct(string $topicName) {

        $conf = new \RdKafka\Conf();
        $conf->set('metadata.broker.list', 'kafka');
        $this->producer = new \RdKafka\Producer($conf);
        $this->topic = $this->producer->newTopic($topicName);
    }

    public function produce(string $inTopicMessage): void {

        $this->topic->produce(RD_KAFKA_PARTITION_UA, 0, $inTopicMessage);
    }
}