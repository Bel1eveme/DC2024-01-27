<?php

namespace App\Cache;

use Psr\Cache\InvalidArgumentException;
use Symfony\Component\Cache\CacheItem;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Contracts\Cache\CacheInterface;

class CacheService
{
    /**
     * @var CacheInterface
     */
    private CacheInterface $cachePool;
    
    /**
     * @param CacheInterface $cachePool
     */
    public function __construct(CacheInterface $cachePool)
    {
        $this->cachePool = $cachePool;
    }
    
    /**
     * @return CacheInterface
     */
    public function getCachePool(): CacheInterface
    {
        return $this->cachePool;
    }
    
    /**
     * @param CacheInterface $cachePool
     * @param string $key
     * @return CacheItem
     */
    public function getCachedItem(CacheInterface $cachePool, string $key): CacheItem
    {
        return $cachePool->getItem($key);
    }
    
    /**
     * @param CacheInterface $cachePool
     * @param CacheItem $item
     * @param array $data
     * @return bool
     */
    public function setCachedItemData(CacheInterface $cachePool, CacheItem $item, array $data): bool
    {
        $item->set(json_encode(['data' => $data, 'last_updated' => time()]));
        return $cachePool->save($item);
    }
    
    /**
     * @param Request $request
     * @return string
     * @throws InvalidArgumentException
     */
    public function deleteItemFromCachePool(Request $request): string
    {
        $keyName = hash( 'sha256', $request->query->get('key'));
        $item = $this->getCachedItem($this->getCachePool(), $keyName);
        if ($item->isHit()) {
            $this->getCachePool()->delete($keyName);
            $html = '<pre>' . $keyName . '</pre>  has been deleted';
        } else {
            $html = '<pre>' . $keyName . '</pre>  not found in cache pool';
        }
        
        return $html;
    }
    
    /**
     * @param Request $request
     * @return string
     */
    public function getHashedKeyName(Request $request): string
    {
        //dd($request->getPathInfo());
        return hash( 'sha256', $request->getPathInfo());
    }
    
}