<?php

namespace App\Controller;

use Cassandra\Connection;
use Psr\Log\LoggerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Attribute\MapRequestPayload;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Contracts\HttpClient\Exception\TransportExceptionInterface;
use Symfony\Contracts\HttpClient\HttpClientInterface;

use App\Entity\Comment;
use App\DTO\{CommentRequestTo, CommentResponseTo};
use App\Trait\{JsonEndpointTrait, CassandraRepoTrait};
use App\Validation\Requirement as ParamRules;
use App\Service\CassandraConnectionService;

#[Route('/comments', name: 'comments_', options: ['expose' => true])]
class CommentsApiController extends AbstractController
{
    use JsonEndpointTrait, CassandraRepoTrait;

    private HttpClientInterface $client;
    private Connection $cassandra;

    function __construct(
        HttpClientInterface $client,
        CassandraConnectionService $cassandraConnectionService
    ) {

        $this->client = $client;
        $this->cassandra = $cassandraConnectionService->connection();
    }

    #[Pure]
    private function failureResponse(int $statusCode = JsonResponse::HTTP_NOT_FOUND): JsonResponse {

        $response = $this->cje(
            status: false,
        );
        return new JsonResponse($response, $statusCode);
    }

    private const INTERNAL_API_BASE_ADDRESS = 'http://host.docker.internal:24130/api/v1.0/comments';

    #[Route('/doGood', name: 'do_good')]
    public function doGood(Request $request): Response {

        /**
         * InTopicMessage
         */
        $inTopicMessage = unserialize($request->getContent(), ['allowed_classes' => [\App\Kafka\MyTopicMessage::class]]);

        $jsonResponse = match($inTopicMessage->action) {
            "show_all"  => $this->client->request('GET',    CommentsApiController::INTERNAL_API_BASE_ADDRESS),
            "show"      => $this->client->request('GET',    CommentsApiController::INTERNAL_API_BASE_ADDRESS . '/' . $inTopicMessage['id']),
            "store"     => $this->client->request('POST',   CommentsApiController::INTERNAL_API_BASE_ADDRESS, [
                'headers'   => [ 'Content-Type' =>  'application/json' ], 
                'body'      => $inTopicMessage['Body']
            ]),
            "update"    => $this->client->request('PUT',    CommentsApiController::INTERNAL_API_BASE_ADDRESS, [
                'headers'   => [ 'Content-Type' =>  'application/json' ], 
                'body'      => $inTopicMessage['Body']
            ]),
            "destroy"   => $this->client->request('DELETE', CommentsApiController::INTERNAL_API_BASE_ADDRESS . '/' . $inTopicMessage['id']),
        };

        $code       = $jsonResponse->getStatusCode();
        $content    = [];

        try {

            $content = json_decode($jsonResponse->getContent(), true);
        } catch(\Exception $e) {}
       
        /**
         * OutTopicMessage
         */
        $outTopicMessage = \App\Kafka\MyTopicMessage::createFromMe(['content' => $content, 'code' => $code]);

        return new Response(serialize($outTopicMessage), Response::HTTP_OK);
    }

    #[Route('', name: 'show_all', methods: 'GET')]
    public function show_all(): JsonResponse
    {

        $result  = $this->cassandra->querySync("SELECT * FROM {$this->table_name}");

        $comments = array();
        foreach($result as $rowNo => $rowContent)
        {

            $comments[] = new Comment($rowContent);
        }

        $response = $this->constructArrayJsonEndpoint(
            entities: $comments,
        );

        return new JsonResponse($response, JsonResponse::HTTP_OK);
    }

    #[Route('/{id}', name: 'show', methods: 'GET', requirements: ['id' => ParamRules::DIGITS])]
    public function show(int $id): JsonResponse 
    {
        
        $comment = $this->RepositoryFind($id);

        if ($comment != null) 
        {

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: new Comment($comment),
            );
    
            return new JsonResponse($response, JsonResponse::HTTP_OK);
        }
        else 
        {

            return $this->failureResponse();
        } 
    }

    #[Route('', name: 'store', methods: 'POST')]
    public function store(#[MapRequestPayload] CommentRequestTo $commentReview): JsonResponse
    {
        try {

            $response = $this->client->request('GET', "http://dcServer/api/v1.0/issues/{$commentReview->issueId}");
            $statusCode = $response->getStatusCode();

            if ($statusCode == Response::HTTP_OK) {
                
                $comment = $this->MapRequestDTOToEntity($commentReview);
                $comment->setId($this->getNextId());
                $result = $this->cassandra->querySync(
                    "INSERT INTO {$this->table_name} (id, issue_id, content) 
                    VALUES (?, ?, ?)", 
                    [$comment->getId(), $comment->getIssueId(), $comment->getContent()]
                );
        
                $response = $this->constructJsonEndpoint(
                    status: true,
                    entity: $comment,
                );
                
                return new JsonResponse($response, Response::HTTP_CREATED);
            } 
            else 
            {
                return $this->failureResponse($statusCode);
            }

        } catch (TransportExceptionInterface $exception) {
           
            return $this->failureResponse();
        }
        
    }

    #[Route('', name: 'update', methods: ['PUT', 'PATCH'])]
    public function update(#[MapRequestPayload] CommentRequestTo $commentReview): JsonResponse 
    {
        $comment = $this->RepositoryFind($commentReview?->id);

        if($comment != null) 
        {

            $comment['issue_id'] = $commentReview->issueId;
            $comment['content'] = $commentReview->content;

            $result = $this->cassandra->querySync(
                "UPDATE {$this->table_name} SET 
                issue_id = {$comment['issue_id']}, 
                content = '{$comment['content']}' 
                WHERE id = {$comment['id']}");  

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: new Comment($comment),
            );

            return new JsonResponse($response, JsonResponse::HTTP_OK);
        }
        else
        {
            
            return $this->failureResponse();
        }
    }

    #[Route('/{id}', name: 'destroy', methods: 'DELETE', requirements: ['id' => ParamRules::DIGITS])]
    public function destroy(int $id): JsonResponse
    {

        $comment = $this->RepositoryFind($id);

        if($comment != null) 
        {
           
            $result = $this->cassandra->querySync("DELETE FROM {$this->table_name} WHERE id = {$id};");

            $response = $this->cje(
                status: true,
            );
            return new JsonResponse($response, JsonResponse::HTTP_NO_CONTENT);
        }
        else
        {

            return $this->failureResponse();
        }  
    }

}
