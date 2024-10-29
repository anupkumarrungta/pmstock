package org.rungta.pmstock.kiteapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // You can add custom error handling here
        return "An error occurred, please try again later."; // Name of the error view (e.g., a JSP or HTML file named `error.html`)
    }

    public String getErrorPath() {
        return "/error";
    }
}

