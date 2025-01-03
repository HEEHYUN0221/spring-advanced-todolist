package com.example.todolist_advanced.todo.service;

import com.example.todolist_advanced.common.entity.Todo;
import com.example.todolist_advanced.common.entity.User;
import com.example.todolist_advanced.common.utils.DateUtils;
import com.example.todolist_advanced.todo.model.TodoDto;
import com.example.todolist_advanced.todo.model.request.CreateTodoRequestDto;
import com.example.todolist_advanced.todo.model.request.UpdateTodoRequestDto;
import com.example.todolist_advanced.todo.model.response.DeleteTodoResponseDto;
import com.example.todolist_advanced.todo.repository.TodoRepository;
import com.example.todolist_advanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    //할일 생성
    public TodoDto createTodo(Long userId, CreateTodoRequestDto request) {
        User user = userRepository.findByIdOrElseThrow(userId);

        Todo saveTodo = new Todo(
                user,
                request.title(),
                request.contents()
        );

        todoRepository.save(saveTodo);

        return TodoDto.convertDto(saveTodo);
    }

    //할일 단건 조회
    public TodoDto findByIdTodo(Long todoId) {
        return TodoDto.convertDto(todoRepository.findByIdOrElseThrow(todoId));
    }

    //모든 할일 조회(탈퇴 유저 제외)
    public List<TodoDto> findAllTodo(Pageable pageable) {
        return todoRepository.findByUserSignStatusTrue(pageable)
                .stream()
                .map(TodoDto::convertDto)
                .toList();
    }

    public List<TodoDto> findTodoByUserNameOrUpdateDate(String name, LocalDate updateDate) {

        LocalDateTime startday = DateUtils.getStartOfDay(updateDate);
        LocalDateTime endday = DateUtils.getEndOfDay(updateDate);

        return todoRepository.findByUser_UserNameAndCreatedAtBetweenAndUser_SignStatusTrue(name,startday,endday)
                .stream()
                .map(TodoDto::convertDto)
                .toList();
    }

    @Transactional
    public TodoDto updateTodo(Long userId, UpdateTodoRequestDto requestDto, Long todoId) {
        Todo todo = todoRepository.findByIdOrElseThrow(todoId);
        todo.validMine(userId,todo);

        todo.update(requestDto.title(), requestDto.contents());

        User user = userRepository.findByIdOrElseThrow(userId);
        user.setLastModifyToDoList(todo.getLastModifiedAt());
        return TodoDto.convertDto(todo);
    }


    @Transactional
    public DeleteTodoResponseDto deleteTodo(Long userId, Long todoId) {

        Todo todo = todoRepository.findByIdOrElseThrow(todoId);
        todo.validMine(userId,todo);

        todoRepository.delete(todo);
        return new DeleteTodoResponseDto("게시물 삭제에 성공했습니다.");
    }



}
