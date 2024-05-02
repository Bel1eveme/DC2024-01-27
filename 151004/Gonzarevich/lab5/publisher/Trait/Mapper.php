<?php

namespace App\Trait;

use Doctrine\ORM\EntityManagerInterface;
use App\Attribute\Request\EntityRelation;

if (!function_exists("getNestedRelation")) {

    function getNestedRelation(object $responseTo): array {

        $result = array();
        $reflector = new \ReflectionClass($responseTo);
        $reflectorProps = $reflector->getProperties();
  
        foreach($reflectorProps as $prop) {

            if (count($reflectorAttrs = $prop->getAttributes())) {


                foreach($reflectorAttrs as $attr) {

                    if ($attr->getName() === EntityRelation::class) {


                        $result[$prop->getName()] = $attr->getArguments()[0];
                    }
                }
            }
        }

        return $result;
    }
}

trait Mapper {

    private static function MapRequestDTOToEntity(object $responseTo, EntityManagerInterface $entityManager) : object {

        $relations = getNestedRelation($responseTo);

        $entityClassName = str_replace("DTO", "Entity", strstr(get_class($responseTo), "RequestTo", true));
        $result = new $entityClassName();

        foreach(get_object_vars($responseTo) as $key => $value) {
            
            $method = 'set'.ucfirst($key);
            if (isset($value)) {

                if (array_key_exists($key, $relations)) {

                    $value = $entityManager->getRepository($relations[$key])->find($value);
                }
                $result->$method($value);
            }
        }
        return $result;
    }

    private static function MapEntityToResponseDTO(Object $entity) : object {
        
        $entityResponseToClassName = str_replace("Entity", "DTO", get_class($entity)."ResponseTo");
        return new $entityResponseToClassName($entity);
    }
}