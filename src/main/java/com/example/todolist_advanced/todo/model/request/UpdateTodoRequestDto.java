package com.example.todolist_advanced.todo.model.request;

import jakarta.validation.constraints.Size;

public record UpdateTodoRequestDto(
        @Size(max=30, message = "제목은 30자를 넘을 수 없습니다.")
        String title,
        @Size(max=200, message = "내용은 200자를 넘을 수 없습니다.")
        String contents
) {
}
