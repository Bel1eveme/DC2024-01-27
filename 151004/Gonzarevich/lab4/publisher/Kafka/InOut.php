<?php

namespace App\Kafka;

class InOut {

    private ?\Fiber $fiber = null;

    public function __construct() {

        $this->fiber = new \Fiber(function () {

            $producer = new \App\Kafka\MyProducer('InTopic');
            $consumer = new \App\Kafka\MyConsumer(['OutTopic']);
            $inTopicMessage = \Fiber::suspend($consumer);
            while(true) {

                if (is_null($inTopicMessage))
                    return;
                $producer->produce(serialize($inTopicMessage));
                $response = $consumer->getResponse();
                $inTopicMessage = \Fiber::suspend($response);
            }
        });
    }

    public function establish(): \App\Kafka\MyConsumer {

        $connectionConfiguration = $this->fiber->start();
        return $connectionConfiguration;
    }

    /**
     * Send message to InTopic and return response from PotTopic
     * 
     * @param \App\Kafka\MyTopicMessage $inTopicMessage Message to send to InTopic
     * @return \App\Kafka\MyTopicMessage OutTopic message send to response.
     */
    public function resume(?\App\Kafka\MyTopicMessage $inTopicMessage): \App\Kafka\MyTopicMessage {

        /**
         * \RdKafka\Message
         */
        $message = $this->fiber->resume($inTopicMessage);

        $outTopicMessage = unserialize($message->payload, 
            [
                'allowed_classes' => [\App\Kafka\MyTopicMessage::class], 
                'max_depth' => 4,
            ]);

        return $outTopicMessage;
    }
}