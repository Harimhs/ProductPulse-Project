package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "industryDropdownCache")
    @GetMapping("/industries")
    public List<Map<String, String>> getIndustries() {
        System.out.println("CACHE: Fetching industries from source...");
        return Arrays.stream(Industry.values())
                .map(ind -> Map.of("value", ind.name(), "label", ind.getLabel()))
                .toList();
    }

    // Debug endpoint to inspect the cache
    @GetMapping("/cache")
    public Object checkCache() {
        var cache = cacheManager.getCache("industryDropdownCache");
        if (cache == null) return "Cache not found";

        // For a method without parameters, Spring uses SimpleKey.EMPTY
        var cachedValue = cache.get(SimpleKey.EMPTY);
        return cachedValue != null ? cachedValue.get() : "Cache is empty";
    }
}
