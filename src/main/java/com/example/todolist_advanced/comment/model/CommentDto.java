package com.example.todolist_advanced.comment.model;

import com.example.todolist_advanced.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long todoId;
    private Long userId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDto convertDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getTodo().getId(),
                comment.getUser().getId(),
                comment.getContents(),
                comment.getCreatedAt(),
                comment.getLastModifiedAt()
        );
    }

}
