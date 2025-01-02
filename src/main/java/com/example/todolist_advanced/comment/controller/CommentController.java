package com.example.todolist_advanced.comment.controller;

import com.example.todolist_advanced.comment.model.CommentDto;
import com.example.todolist_advanced.comment.model.request.CreateCommentRequestDto;
import com.example.todolist_advanced.comment.model.request.UpdateCommentRequestDto;
import com.example.todolist_advanced.comment.model.response.DeleteCommentResponseDto;
import com.example.todolist_advanced.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todolist/{todoId}")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("todoId") Long todoId,
            @RequestBody CreateCommentRequestDto request,
            HttpServletRequest servletRequest) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(commentService.createComment(userId,request,todoId), HttpStatus.CREATED);
    }

    //게시글 기준, 댓글 전체 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> findAllComments(
            @PathVariable("todoId") Long todoId,
            @PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(commentService.findAllComments(todoId, pageable), HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("todoId") Long todoId,
            @PathVariable("commentId") Long commentId,
            @RequestBody UpdateCommentRequestDto request,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(commentService.updateComment(userId, request,todoId,commentId), HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<DeleteCommentResponseDto> deleteComment(
            @PathVariable("todoId") Long todoId,
            @PathVariable("commentId") Long commentId,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        return new ResponseEntity<>(commentService.deleteComment(userId, todoId,commentId), HttpStatus.OK);
    }
}
