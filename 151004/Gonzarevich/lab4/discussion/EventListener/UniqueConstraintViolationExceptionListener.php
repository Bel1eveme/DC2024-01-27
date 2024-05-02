<?php

namespace App\EventListener;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Event\ExceptionEvent;
use Doctrine\DBAL\Exception\UniqueConstraintViolationException;
use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;


class UniqueConstraintViolationExceptionListener implements EventSubscriberInterface
{

    public static function getSubscribedEvents() {

        return [
            KernelEvents::EXCEPTION => 'onKernelException',
        ];
    }

    public function onKernelException(ExceptionEvent $event)
    {
  
        $exception = $event->getThrowable();
        if ($exception instanceof UniqueConstraintViolationException) {

            $response = new JsonResponse(['errors' => $exception->getMessage()], JsonResponse::HTTP_FORBIDDEN);
            $event->setResponse($response);
        }
    }
}