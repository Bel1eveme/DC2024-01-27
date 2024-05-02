<?php

namespace App\Controller;

use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\JsonResponse as Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpKernel\Attribute\MapRequestPayload;
use App\Trait\JsonEndpointTrait;
use App\Validation\Requirement as ParamRules;

use App\Entity\Tag;
use App\DTO\{TagRequestTo, TagResponseTo};
use App\Repository\TagRepository;

#[Route('/tags', name: 'tags_', options: ['expose' => true])]
class TagsApiController extends AbstractController
{
    use JsonEndpointTrait;

    function __construct(
        private TagRepository $tagRepository
    ) {}

    #[Route('', name: 'show_all', methods: 'GET')]
    public function show_all(): Response
    {

        $tags = $this->tagRepository->findAll();

        $response = $this->constructArrayJsonEndpoint(
            entities: $tags,
        );

        return new Response($response, Response::HTTP_OK);
    }

    #[Route('/{id}', name: 'show', methods: 'GET', requirements: ['id' => ParamRules::DIGITS])]
    public function show(int $id): Response 
    {
        
        $tag = $this->tagRepository->find($id);

        $response = $this->constructJsonEndpoint(
            status: true,
            entity: $tag,
        );

        return new Response($response, Response::HTTP_OK);
       
    }

    #[Route('', name: 'store', methods: 'POST')]
    public function store(
        #[MapRequestPayload] TagRequestTo $tagReview,
        EntityManagerInterface $entityManager
    ): Response
    {

        $tag = $this->MapRequestDTOToEntity($tagReview, $entityManager);
        
        $entityManager->persist($tag);
        $entityManager->flush();

        $response = $this->constructJsonEndpoint(
            status: true,
            entity: $tag,
        );
        
        return new Response($response, Response::HTTP_CREATED);
    }

    #[Route('', name: 'update', methods: ['PUT', 'PATCH'])]
    public function update(
        #[MapRequestPayload] TagRequestTo $tagReview,
        EntityManagerInterface $entityManager
    ): Response 
    {

        $tag = $this->tagRepository->find($tagReview?->id);

        $response = null;
        if($tag == null) 
        {

            $response = $this->cje(
                status: false,
            );
            $response = new Response($response, Response::HTTP_NOT_FOUND);
        }
        else
        {

            $tag->setName($tagReview->name);
            
            $entityManager->persist($tag);
            $entityManager->flush();

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: $tag,
            );
            $response = new Response($response, Response::HTTP_OK);
        }
        return $response;
    }

    #[Route('/{id}', name: 'destroy', methods: 'DELETE', requirements: ['id' => ParamRules::DIGITS])]
    public function destroy(int $id, EntityManagerInterface $entityManager): Response
    {

        $tag = $this->tagRepository->find($id);
        $response = null;

        if($tag == null) 
        {
            $response = $this->cje(
                status: false,
            );
            $response = new Response($response, Response::HTTP_NOT_FOUND);
        }
        else
        {

            $entityManager->remove($tag);
            $entityManager->flush();

            $response = $this->cje(
                status: true,
            );
            $response = new Response($response, Response::HTTP_NO_CONTENT);
        }
        
        return $response;
    }

}
