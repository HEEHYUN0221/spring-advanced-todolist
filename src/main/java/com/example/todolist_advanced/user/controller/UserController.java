package com.example.todolist_advanced.user.controller;

import com.example.todolist_advanced.user.model.UserDto;
import com.example.todolist_advanced.user.model.request.SignUpRequestDto;
import com.example.todolist_advanced.user.model.request.UpdateUserRequestDto;
import com.example.todolist_advanced.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //유저 가입
    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody SignUpRequestDto request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.OK);
    }

    //유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    //본인 정보 조회
    @GetMapping("/my-info")
    public ResponseEntity<UserDto> findUserMyInfo(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(userService.findUser(userId),HttpStatus.OK);
    }

    //유저 수정(이름, 이메일)
    @PatchMapping("/my-info/update")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequestDto request,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(userService.updateUser(userId,request), HttpStatus.OK);
    }

    //유저 탈퇴
    @PatchMapping("/my-info/deletion")
    public ResponseEntity<UserDto> softDeleteUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        userService.softDeleteUser(userId);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
