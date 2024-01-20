package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class Controller {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/")
    List<Item> all() {
        return cacheService.findByNo(10);
    }

}
