package com.example.todolist_advanced.common.entity;

import com.example.todolist_advanced.common.enums.UserRoleEnum;
import com.example.todolist_advanced.common.utils.PasswordEncoder;
import com.example.todolist_advanced.user.model.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void updateSignStatus(boolean signStatus) {
        this.signStatus=signStatus;
    }

    public void updateRole(UserRoleEnum role) {
        this.role = role;
    }


}
