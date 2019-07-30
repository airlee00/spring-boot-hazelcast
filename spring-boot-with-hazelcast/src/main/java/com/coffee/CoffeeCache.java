package com.coffee;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"coffees"})
public class CoffeeCache {

    public static Map<String, Coffee> map = new HashMap<String, Coffee>();


    @Cacheable(key="#id")
    public Coffee getCoffeeByCache(String id){

        try {
            System.out.println("데이터 조회");
            Thread.sleep(3000);
        }
        catch (Exception e) {
        }

        return map.get(id);
    }

    @CachePut(key="#id")
    public Coffee createCoffeeOnCache(String id, String name,int price){

        Coffee coffee = new Coffee(id, name, price);

        return map.put(id, coffee);
    }

    @CacheEvict(key = "#id")
    public void evict(String id){
    }





}
