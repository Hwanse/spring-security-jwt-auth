package me.hwanse.jwtdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/login")
  public String loginForm() {
    return "login";
  }

}
