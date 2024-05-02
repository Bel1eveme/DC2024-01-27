<?php

namespace App\Controller;

use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpKernel\Attribute\MapRequestPayload;
use App\Trait\JsonEndpointTrait;
use App\Validation\Requirement as ParamRules;

use App\Entity\User;
use App\DTO\{UserRequestTo, UserResponseTo};
use App\Repository\UserRepository;

#[Route('/users', name: 'users_', options: ['expose' => true])]
class UsersApiController extends AbstractController
{

    use JsonEndpointTrait;

    function __construct(
        private UserRepository $userRepository
    ) {}

    #[Route('', name: 'show_all', methods: 'GET')]
    public function show_all(): JsonResponse
    {

        $users = $this->userRepository->findAll();
       
        $response = $this->constructArrayJsonEndpoint(
            entities: $users,
        );

        return new JsonResponse($response, JsonResponse::HTTP_OK);
    }

    #[Route('/{id}', name: 'show', methods: 'GET', requirements: ['id' => ParamRules::DIGITS])]
    public function show(int $id): JsonResponse 
    {
        
        $user = $this->userRepository->find($id);

        $response = $this->constructJsonEndpoint(
            status: true,
            entity: $user,
        );

        return new JsonResponse($response, JsonResponse::HTTP_OK);
       
    }

    #[Route('', name: 'store', methods: 'POST')]
    public function store(
        #[MapRequestPayload] UserRequestTo $userReview,
        EntityManagerInterface $entityManager
    ): JsonResponse
    {
        
        $user = $this->MapRequestDTOToEntity($userReview, $entityManager);

        $entityManager->persist($user);
        $entityManager->flush();
        
        $response = $this->constructJsonEndpoint(
            status: true,
            entity: $user,
        );
        
        return new JsonResponse($response, JsonResponse::HTTP_CREATED);
    }

    #[Route('', name: 'update', methods: ['PUT', 'PATCH'])]
    public function update(
        #[MapRequestPayload] UserRequestTo $userReview,
        EntityManagerInterface $entityManager
    ): JsonResponse 
    {
        $jsonResponse = null;

        $user = $this->userRepository->find($userReview?->id);
        if($user == null) 
        {

            $response = $this->cje(false);
            $jsonResponse = new JsonResponse($response, JsonResponse::HTTP_NOT_FOUND);
        }
        else
        {

            $user->setLogin($userReview->login);
            $user->setPassword($userReview->password);
            $user->setFirstname($userReview->firstname);
            $user->setLastname($userReview->lastname);

            $entityManager->persist($user);
            $entityManager->flush();

            $response = $this->constructJsonEndpoint(
                status: true,
                entity: $user,
            );
            $jsonResponse = new JsonResponse($response, JsonResponse::HTTP_OK);
        }

        return $jsonResponse;
    }


    #[Route('/{id}', name: 'destroy', methods: 'DELETE', requirements: ['id' => ParamRules::DIGITS])]
    public function destroy(int $id, EntityManagerInterface $entityManager): JsonResponse
    {
        $jsonResponse = null;

        $user = $this->userRepository->find($id);

        if($user == null) 
        {
            $response = $this->cje(false);
            $jsonResponse = new JsonResponse($response, JsonResponse::HTTP_NOT_FOUND);
        }
        else
        {

            $entityManager->remove($user);
            $entityManager->flush();

            $response = $this->cje(true);
            $jsonResponse = new JsonResponse($response, JsonResponse::HTTP_NO_CONTENT);
        }
        
        return $jsonResponse;
    }

}
