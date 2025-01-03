package com.example.todolist_advanced.user.controller;

import com.example.todolist_advanced.user.model.UserDto;
import com.example.todolist_advanced.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUserController {

    private final UserService userService;

    //어드민 권한 변경
    @PatchMapping("/user-to-admin/{userId}")
    public ResponseEntity<UserDto> userRoleChangeAdmin(@PathVariable Long userId) {
        userService.userRoleChangeAdmin(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
