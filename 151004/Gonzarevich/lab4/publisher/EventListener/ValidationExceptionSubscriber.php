<?php

namespace App\EventListener;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Event\ExceptionEvent;
use Symfony\Component\HttpKernel\Exception\HttpException;
use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;


class ValidationExceptionSubscriber implements EventSubscriberInterface
{

    public static function getSubscribedEvents() {

        return [
            KernelEvents::EXCEPTION => 'onKernelException',
        ];
    }

    public function onKernelException(ExceptionEvent $event)
    {

        $exception = $event->getThrowable();
        if ($exception instanceof HttpException) {

            $response = new JsonResponse(['errors' => $exception->getMessage()], 422);
            $event->setResponse($response);
        }
    }
}