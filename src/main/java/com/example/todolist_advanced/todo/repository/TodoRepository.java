package com.example.todolist_advanced.todo.repository;

import com.example.todolist_advanced.common.entity.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    default Todo findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }

    List<Todo> findByUserSignStatusTrue(Pageable pageable);

    //내것만 찾기(유저아이디) -> 유저 객체 생성 후 만들기.
    List<Todo> findAllByUserId(Long userId);

    List<Todo> findByUser_UserNameAndCreatedAtBetweenAndUser_SignStatusTrue(String userName, LocalDateTime createdAtStart, LocalDateTime createdAtEnd);


}
