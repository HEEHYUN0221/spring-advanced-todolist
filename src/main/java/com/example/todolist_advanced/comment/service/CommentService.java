package com.example.todolist_advanced.comment.service;

import com.example.todolist_advanced.comment.model.CommentDto;
import com.example.todolist_advanced.comment.model.request.CreateCommentRequestDto;
import com.example.todolist_advanced.comment.model.request.UpdateCommentRequestDto;
import com.example.todolist_advanced.comment.model.response.DeleteCommentResponseDto;
import com.example.todolist_advanced.comment.repository.CommentRepository;
import com.example.todolist_advanced.common.entity.Comment;
import com.example.todolist_advanced.common.entity.Todo;
import com.example.todolist_advanced.common.entity.User;
import com.example.todolist_advanced.todo.repository.TodoRepository;
import com.example.todolist_advanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;


    public CommentDto createComment(Long userId, CreateCommentRequestDto request, Long todoId) {
        Todo todo = todoRepository.findByIdOrElseThrow(todoId);
        User user = userRepository.findByIdOrElseThrow(userId);

        Comment saveComment = Comment.create(
                todo,
                user,
                request.contents()
        );

        commentRepository.save(saveComment);

        return CommentDto.convertDto(saveComment);
    }

    public List<CommentDto> findAllComments(Long todoId, Pageable pageable) {
        return commentRepository.findByTodoId(todoId, pageable)
                .stream()
                .map(CommentDto::convertDto)
                .toList();
    }

    @Transactional
    public CommentDto updateComment(Long userId, UpdateCommentRequestDto request, Long todoId, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        comment.validateTodoComment(todoId);
        comment.update(request.contents(), userId);
        return CommentDto.convertDto(comment);
    }

    public DeleteCommentResponseDto deleteComment(Long userId, Long todoId, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        comment.validateTodoComment(todoId);
        comment.validateOwnership(userId);
        return new DeleteCommentResponseDto("댓글 삭제에 성공했습니다.");
    }


}

