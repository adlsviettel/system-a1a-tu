package com.allianceoneapparel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/demo")
public class DemoController {
    @GetMapping
    String helloWold() {
        return "Hello world";
    }
}
