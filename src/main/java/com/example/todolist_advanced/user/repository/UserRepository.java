package com.example.todolist_advanced.user.repository;

import com.example.todolist_advanced.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }

    User findUserByUserName(String name);

    User findUserByEmail(String email);


    boolean existsByEmail(String email);

    void deleteUserById(Long userId);

    boolean existsByEmailAndSignStatusTrue(String email);
}
