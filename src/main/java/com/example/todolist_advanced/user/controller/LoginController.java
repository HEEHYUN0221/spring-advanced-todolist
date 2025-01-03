package com.example.todolist_advanced.user.controller;

import com.example.todolist_advanced.user.model.request.LoginRequestDto;
import com.example.todolist_advanced.user.model.response.LogoutResponseDto;
import com.example.todolist_advanced.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginBySession(@Valid @RequestBody LoginRequestDto request){

        String token = userService.getLogin(request);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);

        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    @PostMapping("/logout")
    public LogoutResponseDto logoutBySession(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return new LogoutResponseDto("로그아웃 성공");
    }
}
