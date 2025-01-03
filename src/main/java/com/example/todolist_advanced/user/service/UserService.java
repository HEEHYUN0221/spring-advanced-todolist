package com.example.todolist_advanced.user.service;

import com.example.todolist_advanced.common.entity.User;
import com.example.todolist_advanced.common.enums.UserRoleEnum;
import com.example.todolist_advanced.common.exception.LoginException;
import com.example.todolist_advanced.common.utils.JwtUtil;
import com.example.todolist_advanced.common.utils.PasswordEncoder;
import com.example.todolist_advanced.user.model.UserDto;
import com.example.todolist_advanced.user.model.request.LoginRequestDto;
import com.example.todolist_advanced.user.model.request.SignUpRequestDto;
import com.example.todolist_advanced.user.model.request.UpdateUserRequestDto;
import com.example.todolist_advanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto createUser(SignUpRequestDto request) {
        User.validateDuplicateEmail(userRepository.existsByEmail(request.email()));
        User user = User.createUser(request, passwordEncoder);
        userRepository.save(user);
        return UserDto.convertDto(user);
    }

    public String getLogin(LoginRequestDto request) {

        if(!userRepository.existsByEmailAndSignStatusTrue(request.email())) {
            throw new LoginException("존재하지 않는 아이디 입니다.");
        }
        User user = userRepository.findUserByEmail(request.email());
        user.validatePassword(request.password(), passwordEncoder);

        return jwtUtil.generateAccessToken(user.getId(),user.getUserName(),user.getRole());
    }

    public UserDto findUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserDto.convertDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId,UpdateUserRequestDto request) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (request.userName() != null) {
            user.updateUserName(request.userName());
        }

        if (request.email() != null) {
            boolean emailExists = userRepository.existsByEmail(request.email());
            user.updateEmail(request.email(), emailExists);
        }
        return UserDto.convertDto(user);
    }

    @Transactional
    public void softDeleteUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        user.updateSignStatus(false);
        userRepository.save(user);
    }

    @Transactional
    public void userRoleChangeAdmin(Long userId) {
        User changeUser = userRepository.findByIdOrElseThrow(userId);
        changeUser.updateRole(UserRoleEnum.ADMIN);
        userRepository.save(changeUser);
    }
}
