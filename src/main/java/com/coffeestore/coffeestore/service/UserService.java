package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.user.LoginReqDto;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.dto.user.UserRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.user.UserUpdateRequestDto;
import com.coffeestore.coffeestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findByAll(){
        return userRepository.findAll();
    }
    public User findByUser(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public Optional<User> findByNameAndPhoneNumber(LoginReqDto loginReqDto){
        return userRepository.findByNameAndPhoneNumber(loginReqDto.getName(), Integer.parseInt(loginReqDto.getPhoneNumber()));
    }
    public void createByUser(UserRegistrationRequestDto userRegistrationRequestDto){
        Optional<User> userCheck = userRepository.findByPhoneNumber(userRegistrationRequestDto.getPhoneNumber());
        if(userCheck.isEmpty()){
            User user = userRegistrationRequestDto.toEntity();
            userRepository.save(user);
        }
    }
    public void updateByUser(Long id,UserUpdateRequestDto userUpdateRequestDto){
        User user = findByUser(id);
        user.update(userUpdateRequestDto);
        userRepository.save(user);
    }

    public void deleteByUser(Long id){
        User user = findByUser(id);
        user.setState(0);
        userRepository.save(user);
    }
    public boolean login(LoginReqDto loginReqDto){
        Optional<User> user = userRepository.findByNameAndPhoneNumber(loginReqDto.getName(), Integer.parseInt(loginReqDto.getPhoneNumber()));
        return user.isPresent();
    }

    public void stateChangeByUser(Long userId) {
            User user = findByUser(userId);
            if(user.getState() == 0){
                user.setState(1);
            }else {
                user.setState(0);
            }
            userRepository.save(user);
        }

    public User findByName(String userName) {
        return userRepository.findByName(userName);
    }
}
