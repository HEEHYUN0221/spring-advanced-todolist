package com.example.todolist_advanced.todo.controller;

import com.example.todolist_advanced.todo.model.TodoDto;
import com.example.todolist_advanced.todo.model.request.CreateTodoRequestDto;
import com.example.todolist_advanced.todo.model.request.UpdateTodoRequestDto;
import com.example.todolist_advanced.todo.model.response.DeleteTodoResponseDto;
import com.example.todolist_advanced.todo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todolist")
public class TodoController {

    private final TodoService todoService;

    //투두 생성
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(
            @RequestBody @Valid CreateTodoRequestDto request,
            HttpServletRequest servletRequest) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(todoService.createTodo(userId, request), HttpStatus.CREATED);
    }

    //투두 단건 조회
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoDto> findByIdTodo(@PathVariable Long todoId) {
        return new ResponseEntity<>(todoService.findByIdTodo(todoId), HttpStatus.OK);
    }

    //투두 페이징 조회(전체)
    @GetMapping("/all")
    public ResponseEntity<List<TodoDto>> findAllTodo(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(todoService.findAllTodo(pageable), HttpStatus.OK);
    }

    //투두 페이징 조회(이름, 수정일 기준 조회)
    @GetMapping
    public ResponseEntity<List<TodoDto>> findTodoByUserNameOrUpdateDate(
            @RequestParam(required = false) String name,
            @RequestParam(required = false)LocalDate updateDate) {
        return new ResponseEntity<>(todoService.findTodoByUserNameOrUpdateDate(name, updateDate), HttpStatus.OK);
    }

    //투두 수정
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoDto> updateTodo(
            @RequestBody UpdateTodoRequestDto request,
            @PathVariable Long todoId,
            HttpServletRequest servletRequest
            ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(todoService.updateTodo(userId,request,todoId),HttpStatus.OK);
    }

    //투두 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<DeleteTodoResponseDto> deleteTodo(
            @PathVariable Long todoId,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(todoService.deleteTodo(userId,todoId),HttpStatus.OK);
    }


}
