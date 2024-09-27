package com.digitaldark.ChambeaPe_Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerREST {
    @GetMapping("/")
    public String start(){
        return "Hello World";
    }
}