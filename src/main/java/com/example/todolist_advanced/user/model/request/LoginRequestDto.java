package com.example.todolist_advanced.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "아이디(이메일)을 입력해주세요.")
        @Email(message = "이메일 형식으로 입력해주세요.")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {

}
