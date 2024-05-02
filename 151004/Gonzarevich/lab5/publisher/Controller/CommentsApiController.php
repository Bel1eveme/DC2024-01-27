<?php

namespace App\Controller;

use RdKafka\Producer;
use RdKafka\ProducerTopic;
use RdKafka\Consumer;
use RdKafka\Queue;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\{JsonResponse, Response};
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Attribute\MapRequestPayload;
use Symfony\Contracts\HttpClient\Exception\ClientExceptionInterface;
use Symfony\Contracts\HttpClient\HttpClientInterface;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\RequestStack;

use App\Trait\JsonEndpointTrait;
use App\Validation\Requirement as ParamRules;
use App\Cache\CacheService;

#[Route('/comments', name: 'comments_', options: ['expose' => true])]
class CommentsApiController extends AbstractController
{
    use JsonEndpointTrait;

    private \App\Kafka\InOut $commentsServiceWindow;

    function __construct(
        private HttpClientInterface $client,
        private CacheService $cacheService,
        protected RequestStack $requestStack,
    ) 
    {

        $this->commentsServiceWindow = new \App\Kafka\InOut();
        $this->commentsServiceWindow->establish();
    }

    #[Pure]
    private function failureResponse(int $statusCode = JsonResponse::HTTP_NOT_FOUND): JsonResponse {

        $response = $this->cje(
            status: false,
        );
        return new JsonResponse($response, $statusCode);
    }

    #[Route('', name: 'show_all', methods: 'GET')]
    public function show_all(): JsonResponse
    {
        
        $request    = $this->requestStack->getCurrentRequest();
        $keyName    = $this->cacheService->getHashedKeyName($request);
        $item       = $this->cacheService->getCachedItem($this->cacheService->getCachePool(), $keyName);

        if (!$item->isHit()) {

            $outTopicMessage    = $this->commentsServiceWindow->resume(\App\Kafka\MyTopicMessage::createFromMe());
            $messageCode        = $outTopicMessage['code'];

            if ($messageCode != Response::HTTP_OK)
                return $this->failureResponse($messageCode);

            $ok = $this->cacheService->setCachedItemData($this->cacheService->getCachePool(), $item, $outTopicMessage->data);
        }

        $outTopicMessage = json_decode($this->cacheService->getCachedItem($this->cacheService->getCachePool(), $keyName)->get(), true)['data'];
        return new JsonResponse($outTopicMessage['content'], $outTopicMessage['code']);
    }

    #[Route('/{id}', name: 'show', methods: 'GET', requirements: ['id' => ParamRules::DIGITS])]
    public function show(int $id): JsonResponse 
    {

        $request    = $this->requestStack->getCurrentRequest();
        $keyName    = $this->cacheService->getHashedKeyName($request);
        $item       = $this->cacheService->getCachedItem($this->cacheService->getCachePool(), $keyName);

        if (!$item->isHit()) {
            
            $outTopicMessage    = $this->commentsServiceWindow->resume(\App\Kafka\MyTopicMessage::createFromMe(['id' => $id]));
            $messageCode        = $outTopicMessage['code'];

            if ($messageCode != Response::HTTP_OK)
                return $this->failureResponse($messageCode);

            $ok = $this->cacheService->setCachedItemData($this->cacheService->getCachePool(), $item, $outTopicMessage->data);
        }

        $outTopicMessage = json_decode($this->cacheService->getCachedItem($this->cacheService->getCachePool(), $keyName)->get(), true)['data'];
        return new JsonResponse($outTopicMessage['content'], $outTopicMessage['code']);
    }

    #[Route('', name: 'store', methods: 'POST')]
    public function store(Request $request): JsonResponse 
    {
        
        $outTopicMessage    = $this->commentsServiceWindow->resume(\App\Kafka\MyTopicMessage::createFromMe(['Body' => $request->getContent()]));
        $messageCode        = $outTopicMessage['code'];

        if ($messageCode != Response::HTTP_CREATED)
            return $this->failureResponse($messageCode);

        $showAllRequest = Request::create(uri: '', parameters: ['key' => $this->generateUrl('api.v1.0.comments_show_all')]);
        
        $this->cacheService->deleteItemFromCachePool($showAllRequest);
        
        return new JsonResponse($outTopicMessage['content'], $outTopicMessage['code']);
    }

    #[Route('', name: 'update', methods: ['PUT', 'PATCH'])]
    public function update(Request $request): JsonResponse 
    {

        $outTopicMessage    = $this->commentsServiceWindow->resume(\App\Kafka\MyTopicMessage::createFromMe(['Body' => $request->getContent()]));
        $messageCode        = $outTopicMessage['code'];

        if ($messageCode != Response::HTTP_OK)
            return $this->failureResponse($messageCode);

        $showAllRequest = Request::create(uri: '', parameters: ['key' => $this->generateUrl('api.v1.0.comments_show_all')]);
        $showRequest    = Request::create(uri: '', parameters: ['key' => $this->generateUrl('api.v1.0.comments_show', ['id' => $outTopicMessage['content']['id']])]);

        $this->cacheService->deleteItemFromCachePool($showAllRequest);
        $this->cacheService->deleteItemFromCachePool($showRequest);

        return new JsonResponse($outTopicMessage['content'], $outTopicMessage['code']);
    }

    #[Route('/{id}', name: 'destroy', methods: 'DELETE', requirements: ['id' => ParamRules::DIGITS])]
    public function destroy(int $id, Request $request): JsonResponse
    {

        $outTopicMessage    = $this->commentsServiceWindow->resume(\App\Kafka\MyTopicMessage::createFromMe(['id' => $id]));

        $showAllRequest = Request::create(uri: '', parameters: ['key' => $this->generateUrl('api.v1.0.comments_show_all')]);
        $showRequest    = Request::create(uri: '', parameters: ['key' => $this->generateUrl('api.v1.0.comments_show', ['id' => $id])]);

        $this->cacheService->deleteItemFromCachePool($showAllRequest);
        $this->cacheService->deleteItemFromCachePool($showRequest);

        return new JsonResponse($outTopicMessage['content'], $outTopicMessage['code']);
    }

}
