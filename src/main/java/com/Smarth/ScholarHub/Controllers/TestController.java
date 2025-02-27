package com.Smarth.ScholarHub.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {

    @GetMapping
    @RequestMapping("")
    public String works() {
        return "Works";
    }

}