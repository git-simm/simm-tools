package com.simm.common.utils.redis;

/**
 * 请求锁回调
 * @author simm
 */
public interface Callback {
    /**
     * 占有锁成功或失败时的回调
     * @param success
     * @return
     * @throws InterruptedException
     */
    Object callback(boolean success) throws InterruptedException;
}
