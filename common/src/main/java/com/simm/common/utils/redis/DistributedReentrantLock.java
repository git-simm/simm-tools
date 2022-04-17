package com.simm.common.utils.redis;

/**
 * @author simm
 * 分布式轻量级等待锁
 */
public interface DistributedReentrantLock {

    /**
     * 获取锁
     * @param waitTimeout 等待时间
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(int waitTimeout) throws InterruptedException;

    /**
     * 释放锁
     */
    public void unlock();
}
