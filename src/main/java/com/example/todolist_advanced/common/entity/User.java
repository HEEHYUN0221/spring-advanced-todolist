package com.example.todolist_advanced.common.entity;

import com.example.todolist_advanced.common.enums.UserRoleEnum;
import com.example.todolist_advanced.common.exception.LoginException;
import com.example.todolist_advanced.common.exception.ValidateException;
import com.example.todolist_advanced.common.utils.PasswordEncoder;
import com.example.todolist_advanced.user.model.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //유저 식별자

    @Setter
    @Column(nullable = false)
    private String userName;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column
    private LocalDateTime lastModifyToDoList;

    @Column
    private boolean signStatus;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public static User createUser(SignUpRequestDto request, PasswordEncoder passwordEncoder) {
        String encodePassword = passwordEncoder.encode(request.password());
        return new User(null,
                request.userName(),
                request.email(),
                encodePassword,
                null,
                true,
                UserRoleEnum.USER);
    }

    public static void validateDuplicateEmail(boolean exists) {
        if (exists) {
            throw new ValidateException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
    }

    public void updateSignStatus(boolean signStatus) {
        this.signStatus=signStatus;
    }

    public void updateRole(UserRoleEnum role) {
        this.role = role;
    }


    public void validatePassword(String password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new ValidateException("유효하지 않은 사용자 이름입니다.", HttpStatus.BAD_REQUEST);
        }
        this.userName = userName;
    }

    public void updateEmail(String email, boolean emailExists) {
        if (email == null || email.isBlank()) {
            throw new ValidateException("유효하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        if (emailExists) {
            throw new ValidateException("중복된 이메일 입니다.", HttpStatus.CONFLICT);
        }
        this.email = email;
    }
}
