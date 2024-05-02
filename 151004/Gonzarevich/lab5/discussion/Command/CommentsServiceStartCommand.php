<?php

namespace App\Command;

use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Contracts\HttpClient\HttpClientInterface;

use App\Controller\CommentsApiController;

// the name of the command is what users type after "php bin/console"
#[AsCommand(name: 'kafka:service:comments', 
            description: 'Starts comments cassandra service',
            aliases: ['ksc'])]
class CommentsServiceStartCommand extends Command
{

    private HttpClientInterface $client;

    function __construct(HttpClientInterface $client) {

        $this->client = $client;

        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
 
        $conf = new \RdKafka\Conf();
        $conf->set('metadata.broker.list', 'kafka');
        $producer = new \RdKafka\Producer($conf);
        $topic = $producer->newTopic('OutTopic');

        $consumer = new \App\Kafka\MyConsumer(['InTopic']);

        $business = function (\RdKafka\Message $message) use ($output, $topic) {

            $output->writeln('start responding to lab1...');

            $output->writeln('From request: ' . $message->payload);
            
            /**
             * Response
             */
            $apiManagerResponse = $this->client->request('GET', 'http://host.docker.internal:24130/api/v1.0/comments/doGood', [

                'body'  => $message->payload,
            ]);
            
            $output->writeln('From api manager: ' . $apiManagerResponse->getContent());
            
            /**
             * Produce to OutTopic
             */
            $topic->produce(RD_KAFKA_PARTITION_UA, 0, $apiManagerResponse->getContent());
            
            $output->writeln('end responding to lab1...');
        };
        $business->bindTo($this);

        $consumer->consume($business);

        return Command::SUCCESS;
    }
}