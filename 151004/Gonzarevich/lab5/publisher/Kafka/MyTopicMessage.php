<?php

namespace App\Kafka;

class MyTopicMessage implements \ArrayAccess {

    public ?string $action  = null;
    public ?array $data     = null;

    public function offsetExists($offset): bool {
        return isset($this->data[$offset]);
    }

    public function offsetGet($offset): mixed {
        return $this->data[$offset];
    }

    public function offsetSet($offset, $value): void {
        $this->data[$offset] = $value;
    }

    public function offsetUnset($offset): void {
        unset($this->data[$offset]);
    }
    
    public function __construct(?string $action, array $object) {

        $this->action = $action;
        $this->data = $object;
    }

    public static function createFromMe(array $args = []): static {

        $action = debug_backtrace(!DEBUG_BACKTRACE_PROVIDE_OBJECT|DEBUG_BACKTRACE_IGNORE_ARGS,2)[1]['function'];
        return new static($action, $args);
    }
}