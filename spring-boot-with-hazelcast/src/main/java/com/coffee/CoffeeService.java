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
public class CoffeeService {

    public static Map<String, Coffee> map = new HashMap<String, Coffee>();
    {
    	map.put("a1", new Coffee("a1","americano",4500));
    }


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

         map.put(id, coffee);

         return coffee;
    }


    @CachePut(key="#id")
    public Coffee updateCoffeeOnCache(String id, String name,int price){

        Coffee coffee = map.get(id);

        coffee.setName(name);
        coffee.setPrice(price);

        return map.put(id,  coffee);
    }

    @CacheEvict(key = "#id")
    public void evict(String id){
    }





}
