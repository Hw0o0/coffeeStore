package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.user.LoginReqDto;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.dto.user.UserRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.user.UserUpdateRequestDto;
import com.coffeestore.coffeestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findByAll() {
        return userRepository.findAll();
    }

    public User findByUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByNameAndPhoneNumber(LoginReqDto loginReqDto) {
        return userRepository.findByNameAndPhoneNumber(loginReqDto.getName(), Integer.parseInt(loginReqDto.getPhoneNumber())).orElseThrow();
    }

    public void createByUser(UserRegistrationRequestDto userRegistrationRequestDto) {
        boolean exist = userRepository.existsByPhoneNumber(userRegistrationRequestDto.getPhoneNumber());
        if (!exist) {
            User user = userRegistrationRequestDto.toEntity();
            userRepository.save(user);
        }
    }

    public void updateByUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = findByUser(id);
        user.update(userUpdateRequestDto);
        userRepository.save(user);
    }

    public void deleteByUser(Long id) {
        User user = findByUser(id);
        user.setState(0);
        userRepository.save(user);
    }

    public boolean login(LoginReqDto loginReqDto) {
        Optional<User> user = userRepository.findByNameAndPhoneNumber(loginReqDto.getName(), Integer.parseInt(loginReqDto.getPhoneNumber()));
        return user.isPresent();
    }

    public void stateChangeByUser(Long userId) {
        User user = findByUser(userId);
        if (user.getState() == 0) {
            user.setState(1);
        } else {
            user.setState(0);
        }
        userRepository.save(user);
    }

    public List<User> searchByUser(String userName) {
        List<User> searchUsers = new ArrayList<>();
        //패턴을 컴파일한다.
        Pattern name = Pattern.compile(userName);
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (User user : findByAll()) {
            Matcher matcher = name.matcher(user.getName());
            if (matcher.find()) {
                searchUsers.add(user);
            }
        }
        return searchUsers;
    }
}
