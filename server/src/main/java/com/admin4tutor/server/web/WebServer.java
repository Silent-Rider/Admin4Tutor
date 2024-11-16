package com.admin4tutor.server.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebServer{
    
    @GetMapping
    public String displayStartPage(@RequestParam String name) {
        return String.format("Hello, %s!", name);
    }
}