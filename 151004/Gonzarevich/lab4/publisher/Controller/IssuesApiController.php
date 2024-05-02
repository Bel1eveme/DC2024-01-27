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

use App\Entity\Issue;
use App\DTO\{IssueRequestTo, IssueResponseTo};
use App\Repository\IssueRepository;
use App\Entity\User;

#[Route('/issues', name: 'issues_', options: ['expose' => true])]
class IssuesApiController extends AbstractController
{
    use JsonEndpointTrait;

    function __construct(
        private IssueRepository $issueRepository
    ) {}

    #[Route('', name: 'show_all', methods: 'GET')]
    public function show_all(): Response
    {

        $issues = $this->issueRepository->findAll();

        $response = $this->constructArrayJsonEndpoint(
            entities: $issues,
        );

        return new Response($response, Response::HTTP_OK);
    }

    #[Route('/{id}', name: 'show', methods: 'GET', requirements: ['id' => ParamRules::DIGITS])]
    public function show(int $id): Response 
    {

        $issue = $this->issueRepository->find($id);
        
        if($issue == null) 
        {

            $response = $this->cje(
                status: false,
            );
            return new Response($response, Response::HTTP_NOT_FOUND);
        }
        else
        {

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: $issue,
            );

            return new Response($response, Response::HTTP_OK);
        }
    }

    #[Route('', name: 'store', methods: 'POST')]
    public function store(
        #[MapRequestPayload] IssueRequestTo $issueReview,
        EntityManagerInterface $entityManager
    ): Response
    {

        $issue = $this->MapRequestDTOToEntity($issueReview, $entityManager);
        
        $entityManager->persist($issue);
        $entityManager->flush();

        $response = $this->constructJsonEndpoint(
            status: true,
            entity: $issue,
        );
        
        return new Response($response, Response::HTTP_CREATED);
    }

    #[Route('', name: 'update', methods: ['PUT', 'PATCH'])]
    public function update(
        #[MapRequestPayload] IssueRequestTo $issueReview,
        EntityManagerInterface $entityManager
    ): Response 
    {
        $response = null;

        $issue = $this->issueRepository->find($issueReview?->id);

        if($issue == null) 
        {

            $response = $this->cje(
                status: false,
            );
            $response = new Response($response, Response::HTTP_NOT_FOUND);
        }
        else
        {

            $issue->setUserId($entityManager->getRepository(User::class)->find($issueReview->userId));
            $issue->setTitle($issueReview->title);
            $issue->setContent($issueReview->content);               
            $issue->setCreated($issueReview->created);          
            $issue->setCreated($issueReview->modified);
            
            $entityManager->persist($issue);
            $entityManager->flush();

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: $issue,
            );
            $response = new Response($response, Response::HTTP_OK);
        }
        return $response;
    }

    #[Route('/{id}', name: 'destroy', methods: 'DELETE', requirements: ['id' => ParamRules::DIGITS])]
    public function destroy(int $id, EntityManagerInterface $entityManager): Response
    {

        $issue = $this->issueRepository->find($id);
        $response = null;

        if($issue == null) 
        {
            $response = $this->cje(
                status: false,
            );
            $response = new Response($response, Response::HTTP_NOT_FOUND);
        }
        else
        {

            $entityManager->remove($issue);
            $entityManager->flush();

            $response = $this->cje(
                status: true,
            );
            $response = new Response($response, Response::HTTP_NO_CONTENT);
        }
        
        return $response;
    }

}
