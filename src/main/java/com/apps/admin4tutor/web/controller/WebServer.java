package com.apps.admin4tutor.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebServer{
    
    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return "Hello, World!";
    }
}