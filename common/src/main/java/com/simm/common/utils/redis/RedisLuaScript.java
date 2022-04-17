package com.simm.common.utils.redis;

import java.util.List;

/**
 * @author simm
 * redis lua脚本原子操作
 */
public interface RedisLuaScript {
    /**
     * redis lua 脚本执行
     * @param script
     * @param keys
     * @param args
     * @return
     */
    Object eval(String script, List<String> keys, List<String> args);
}
