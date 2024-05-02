<?php
declare(strict_types = 1);

namespace App\Trait;

trait CassandraRepoTrait 
{

    protected string $table_name      = "tbl_comments";
    protected string $id_table_name   = "ids";
    protected string $id_field_name   = "comment_id";

    protected function getNextId() : int {

        $result = $this->cassandra->querySync("SELECT next_id FROM {$this->id_table_name} WHERE id_name = '{$this->id_field_name}'");
        $id = $result->fetchCol()[0];
        $next_id = $id + 1;
        $result = $this->cassandra->querySync("UPDATE {$this->id_table_name} SET next_id = {$next_id} WHERE id_name = '{$this->id_field_name}' IF next_id = {$id}");       
        return $id;
    }

    protected function RepositoryFind(int $id) : ?array {

        $result = $this->cassandra->querySync(
            "SELECT * FROM {$this->table_name} 
            WHERE id = ?", [$id]);
        return $result->fetchRow();
    }

    protected function RepositoryFindAll() : \SplFixedArray {

        $result  = $this->cassandra->querySync("SELECT * FROM {$this->table_name}");
        return $result->fetchAll();
    }   
}