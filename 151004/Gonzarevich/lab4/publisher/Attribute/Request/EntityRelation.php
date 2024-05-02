<?php

namespace App\Attribute\Request;

#[Attribute]
class EntityRelation {

    public function __construct(
        public string $entityClassName
    ){}
}