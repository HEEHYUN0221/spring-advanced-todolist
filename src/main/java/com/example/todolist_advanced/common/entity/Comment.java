package com.example.todolist_advanced.common.entity;

import com.example.todolist_advanced.common.exception.ValidateException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "todo_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Todo todo;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false, length = 50)
    private String contents;

    public Comment(Todo todo, User user, String contents) {
        this.todo = todo;
        this.user = user;
        this.contents = contents;
    }

    public static Comment create(Todo todo,User user, String content) {
        Comment comment = new Comment();
        comment.user = user;
        comment.todo = todo;
        comment.contents = content;
        return comment;
    }

    // 댓글 수정 메서드
    public void update(String content, Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new ValidateException("댓글 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        this.contents = content;
    }

    // 댓글 삭제 권한 확인 메서드
    public void validateOwnership(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new ValidateException("댓글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
    }

    public void validateTodoComment(Long todoId) {
        if(!todoId.equals(this.getTodo().getId())){
            throw new ValidateException("잘못된 접근입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
