<?php

namespace App\EventListener;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Event\ExceptionEvent;
use Doctrine\DBAL\Exception\NotNullConstraintViolationException;
use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;


class NotNullConstraintViolationExceptionListener implements EventSubscriberInterface
{

    public static function getSubscribedEvents() {

        return [
            KernelEvents::EXCEPTION => 'onKernelException',
        ];
    }

    public function onKernelException(ExceptionEvent $event)
    {
  
        $exception = $event->getThrowable();
        if ($exception instanceof NotNullConstraintViolationException) {

            $response = new JsonResponse(['errors' => $exception->getMessage()], JsonResponse::HTTP_BAD_REQUEST);
            $event->setResponse($response);
        }
    }
}