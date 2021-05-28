package org.launchcode.neighborgoods.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="index")
public class HomeController {
    @GetMapping("")
    public String homePage(){
        return "index";
    }
}
