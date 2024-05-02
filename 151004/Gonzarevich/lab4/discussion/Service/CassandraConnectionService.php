<?php

declare(strict_types = 1);

namespace App\Service;

use \Cassandra\Connection;
use \Cassandra\Exception;

class CassandraConnectionService 
{

    /**
     * cqlsh> CREATE KEYSPACE distcomp
     *           ... with REPLICATION = {
     *           ...  'class'  : 'SimpleStrategy',
     *           ...  'replication_factor' : 1
     *           ... };
     */

     /**
      * cqlsh:distcomp> create table tbl_comments (
      *      ... id int PRIMARY KEY,
      *      ... issue_id int,
      *      ... content TEXT
      *      ... );
      */

      /**
       * cqlsh> DROP KEYSPACE IF EXISTS mykeyspace;
       * cqlsh> DESCRIBE KEYSPACES;
       */

      /**
       * select table_name from system_schema.tables where keyspace_name = 'distcomp';
       */

       /**
        * INSERT INTO tbl_comments (id, issue_id, content) VALUES (1, 123, 'Sample comment');
        */

        /**
         * https://stackoverflow.com/questions/3935915/how-to-create-auto-increment-ids-in-cassandra
         */

    /**
     * Connection configuration
     */
    private array $conn_conf = [
        'dcMicroserviceDatabase'
    ];

    /**
     * Database name
     */
    private string $keyspace_name = 'distcomp';

    private ?Connection $connection = null;

    function __construct() {

        $this->connection = new Connection($this->conn_conf, $this->keyspace_name);
    }

    public function connection(): ?Connection {

        try
        {
            $this->connection->connect();
        }
        catch (Exception $e)
        {
            echo 'Caught exception: ',  $e->getMessage(), "\n";
        }

        return $this->connection;
    }
}