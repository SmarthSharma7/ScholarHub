
package com.Smarth.ScholarHub;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("")
    public String greet() {
        return "Welcome to ScholarHub!!";
    }

}