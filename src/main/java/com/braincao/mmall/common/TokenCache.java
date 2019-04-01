package com.braincao.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 用户忘记密码，回答问题后，应缓存一个token，以便后续调用
 */
public class TokenCache {

    //日志
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    //定义了初始1000，最大10000的缓存loadingCache，采用LRU算法，过期时长为12小时
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>(){
        //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
        @Override
        public String load(String key) throws Exception {
            return "null";
        }
    });

    public static void setKey(String key, String value){
        loadingCache.put(key, value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = loadingCache.get(key);
            if(value.equals("null")){
                value = null;
            }
        }catch (Exception e){
            logger.error("loadingCache get error");
        }
        return value;
    }
}
