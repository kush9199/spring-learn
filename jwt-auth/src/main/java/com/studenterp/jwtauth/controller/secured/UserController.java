package com.studenterp.jwtauth.controller.secured;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user/")
public class UserController {
    @GetMapping("portal")
    public ResponseEntity<String> getPortal() {
        return ResponseEntity.ok("portal");
    }
}
