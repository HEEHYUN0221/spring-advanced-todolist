package com.example.todolist_advanced.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @Size(max = 20, message = "이름은 20자 이내여야 합니다.")
        @NotBlank(message = "이름은 비울 수 없습니다.")
        String userName,
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "유효하지 않은 이메일 형식입니다.")
        @NotBlank(message = "이메일은 비울 수 없습니다.")
        String email,
        @NotBlank(message = "비밀번호는 비울 수 없습니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?!@$%^&*-]).{8,}$",
        message = "유효하지 않은 비밀번호 형식입니다. 최소 한 개의 대문자, 소문자, 숫자, 특수문자를 포함한 8자 이상의 문자여야 합니다.")
        String password
        ){
}
