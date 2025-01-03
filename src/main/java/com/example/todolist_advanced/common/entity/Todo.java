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
@Table(name = "todo")
public class Todo extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false, length = 200)
    private String contents;

    public Todo(User user, String title, String contents) {
        this.user=user;
        this.title=title;
        this.contents=contents;
    }

    public void update(String title, String contents) {

        if(contents==null&&title==null) {
            throw new ValidateException("잘못된 입력입니다.", HttpStatus.BAD_REQUEST);
        }

        if (title != null) {
            this.title = title;
        }
        if (contents != null) {
            this.contents = contents;
        }
    }

    public void validMine(Long userId, Todo todo) {

        if(!userId.equals(todo.getUser().getId())){
            throw new ValidateException("본인의 할일에서만 접근 가능한 기능입니다.",HttpStatus.BAD_REQUEST);
        }

    }
}
