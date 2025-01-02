package com.example.todolist_advanced.todo.model;

import com.example.todolist_advanced.common.entity.Todo;
import com.example.todolist_advanced.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
    private Long id;

    private UserDto user;

    private String title;

    private String contents;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static TodoDto convertDto(Todo todo) {
        return new TodoDto(
                todo.getId(),
                UserDto.convertDto(todo.getUser()),
                todo.getTitle(),
                todo.getContents(),
                todo.getCreatedAt(),
                todo.getLastModifiedAt()
        );
    }


}
