package com.controllers;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffee.Coffee;
import com.coffee.CoffeeService;
import com.hazelcast.core.HazelcastInstance;
import com.topic.HazelcastTemplate;

@RestController
@RequestMapping("/hazelcast")
public class HazelcastController {

    private final Logger logger = LoggerFactory.getLogger(HazelcastController.class);

    @Autowired
    private  HazelcastInstance hazelcastInstance;

    @Autowired
    private HazelcastTemplate hazelcastTemplate;

    @PostMapping(value = "/write-data")
    public String writeDataToHazelcast(@RequestParam String key, @RequestParam String value) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        hazelcastMap.put(key, value);
        return "Data is stored.";
    }

    @GetMapping(value = "/read-data")
    public String readDataFromHazelcast(@RequestParam String key) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        return hazelcastMap.get(key);
    }

    @GetMapping(value = "/read-all-data")
    public Map<String, String> readAllDataFromHazelcast() {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        return hazelcastInstance.getMap("my-map");
    }

    @PostMapping(value = "/public")
    public String publishTest(@RequestParam String key) {
    	final String randomKey = UUID.randomUUID().toString();
		final String randomValue = UUID.randomUUID().toString();
		hazelcastTemplate.send(randomKey, randomValue);
		return randomKey;
    }
    //-------------------------------------------------------------------------//
    @Autowired
    CoffeeService coffeeService;

    @GetMapping("/coffee")
    public Coffee findCoffee(@RequestParam("id")String id){
    	System.out.println("================44444:"+ hazelcastInstance.getConfig().toString());

        return coffeeService.getCoffeeByCache(id);
    }

    @RequestMapping( value="/coffee/{id}/{name}/{price}", method= {RequestMethod.PUT, RequestMethod.POST})
    public Coffee newCoffee(@PathVariable("id") String id, @PathVariable("name") String name, @PathVariable("price") int price){
        return coffeeService.createCoffeeOnCache(id, name, price);
    }

    @PostMapping("/coffee/{id}")
    public void updateCoffee(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("price") int price){
        coffeeService.updateCoffeeOnCache(id, name, price);
    }

    @DeleteMapping("/coffee/{id}")
    public void evictCoffee(@PathVariable("id")String id){
        coffeeService.evict(id);
    }
}
