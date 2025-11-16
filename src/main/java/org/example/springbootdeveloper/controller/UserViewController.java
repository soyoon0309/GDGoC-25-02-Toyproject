package org.example.springbootdeveloper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "oauthlogin"; //login경로로 접근하면 login.html로
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; //signup 경로로 접근하면 signup.html로

    }
}
