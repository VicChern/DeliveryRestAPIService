package com.softserve.itacademy.kek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for frontend
 */
@Controller
@RequestMapping("/")
public class MainController {

    /**
     * Entry point to index.html
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

}
