<?php

namespace App\Kafka;

class MyConsumer {

    private ?\RdKafka\KafkaConsumer $kafkaConsumer = null;

    public function __construct(array $topics) {

        $conf = $this->GetConf();
        $this->kafkaConsumer = new \RdKafka\KafkaConsumer($conf);
        $this->kafkaConsumer->subscribe($topics);
    }

    public function consumem(int $timeout): \RdKafka\Message {

        return $this->kafkaConsumer->consume($timeout);
    }

    public function getResponse(): \RdKafka\Message {

        while (true) {
            $message = $this->kafkaConsumer->consume(1000);
            switch ($message->err) {
                case RD_KAFKA_RESP_ERR_NO_ERROR:
                    return $message;
                case RD_KAFKA_RESP_ERR__PARTITION_EOF:
                    echo "No more messages; will wait for more\n";
                    break;
                case RD_KAFKA_RESP_ERR__TIMED_OUT:
                    echo "Timed out\n";
                    break;
                default:
                    throw new \Exception($message->errstr(), $message->err);
            }
        }
    }

    public function consume(callable $business) {

        while (true) {
            $message = $this->kafkaConsumer->consume(1000);
            switch ($message->err) {
                case RD_KAFKA_RESP_ERR_NO_ERROR:
                    $business();
                    break;
                case RD_KAFKA_RESP_ERR__PARTITION_EOF:
                    echo "No more messages; will wait for more\n";
                    break;
                case RD_KAFKA_RESP_ERR__TIMED_OUT:
                    echo "Timed out\n";
                    break;
                default:
                    throw new \Exception($message->errstr(), $message->err);
            }
        }
    }

    private function GetConf(): \RdKafka\Conf {

        $conf = new \RdKafka\Conf();
        // Configure the group.id. All consumer with the same group.id will consume
        // different partitions.
        $conf->set('group.id', 'serviceGroup');

        // Initial list of Kafka brokers
        $conf->set('metadata.broker.list', 'kafka');

        // Set where to start consuming messages when there is no initial offset in
        // offset store or the desired offset is out of range.
        // 'earliest': start from the beginning
        $conf->set('auto.offset.reset', 'earliest');

        // Emit EOF event when reaching the end of a partition
        $conf->set('enable.partition.eof', 'true');

        // Set a rebalance callback to log partition assignments (optional)
        $conf->setRebalanceCb(function (\RdKafka\KafkaConsumer $kafka, $err, array $partitions = null) {
            switch ($err) {
                case RD_KAFKA_RESP_ERR__ASSIGN_PARTITIONS:
                    //echo "Assign: ";
                    //var_dump($partitions);
                    $kafka->assign($partitions);
                    break;

                case RD_KAFKA_RESP_ERR__REVOKE_PARTITIONS:
                    //echo "Revoke: ";
                    //var_dump($partitions);
                    $kafka->assign(NULL);
                    break;

                default:
                    throw new \Exception($err);
            }
        });

        return $conf;
    }
}