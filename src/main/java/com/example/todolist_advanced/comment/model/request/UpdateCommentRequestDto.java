package com.example.todolist_advanced.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequestDto(
        @Size(max=100, message = "댓글 내용은 100자를 넘을 수 없습니다.")
        @NotBlank(message = "댓글 내용은 비울 수 없습니다.")
        String contents
) {
}
