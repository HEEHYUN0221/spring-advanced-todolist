package com.example.todolist_advanced.user.service;

import com.example.todolist_advanced.common.enums.UserRoleEnum;
import com.example.todolist_advanced.common.utils.JwtUtil;
import com.example.todolist_advanced.common.utils.PasswordEncoder;
import com.example.todolist_advanced.common.entity.User;
import com.example.todolist_advanced.common.exception.ValidateException;
import com.example.todolist_advanced.user.model.UserDto;
import com.example.todolist_advanced.user.model.request.LoginRequestDto;
import com.example.todolist_advanced.user.model.request.SignUpRequestDto;
import com.example.todolist_advanced.user.model.request.UpdateUserRequestDto;
import com.example.todolist_advanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.todolist_advanced.common.exception.LoginException;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto createUser(SignUpRequestDto request) {
        User user = User.createUser(request, passwordEncoder);
        boolean validate = userRepository.existsByEmail(request.email());
        if(validate) {
            throw new ValidateException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
        userRepository.save(user);
        return UserDto.convertDto(user);
    }

    public String getLogin(LoginRequestDto request) {

        if(!userRepository.existsByEmailAndSignStatusTrue(request.email())) {
            throw new LoginException("존재하지 않는 아이디 입니다.");
        }

        User user = userRepository.findUserByEmail(request.email());

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            return jwtUtil.generateToken(user.getId(),user.getUserName(),user.getRole());
        } else {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public UserDto findUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserDto.convertDto(user);
    }

    @Transactional
    public UserDto updateUser(Long userId,UpdateUserRequestDto request) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if(request.userName()==null&&request.email()==null) {
            throw new ValidateException("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        if(request.userName()!=null) {
            user.setUserName(request.userName());
        }

        if(request.email()==null) {
            return UserDto.convertDto(user);
        }

        if(userRepository.existsByEmail(request.email())) {
            throw new ValidateException("중복된 이메일 입니다.",HttpStatus.CONFLICT);
        }

        user.setEmail(request.email());
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
