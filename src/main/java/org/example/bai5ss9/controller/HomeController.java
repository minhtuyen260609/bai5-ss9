package org.example.bai5ss9.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Locale locale, Model model) {
        model.addAttribute("currentLocale", locale.toLanguageTag());
        return "index";
    }


}
