package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.menu.MenuRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> findByAll(){
        return menuRepository.findAll();
    }

    public Menu findByMenu(Long id){
        Optional<Menu> menu = menuRepository.findById(id);
        return menu.orElse(null);
    }

    public void createByMenu(MenuRegistrationRequestDto menuRegistrationRequestDto){
        Optional<Menu> menuCheck = menuRepository.findByName(menuRegistrationRequestDto.getName());
        if(menuCheck.isEmpty()){
            Menu menu = menuRegistrationRequestDto.toEntity();
            menuRepository.save(menu);
        }
    }
    public void updateByMenu(Long id, MenuUpdateRequestDto menuUpdateRequestDto){
        Menu menu = findByMenu(id);
        menu.update(menuUpdateRequestDto);
        menuRepository.save(menu);
    }

    public void deleteByMenu(Long id){
        Menu menu = findByMenu(id);
        menu.setState(0);
        menuRepository.save(menu);
    }
}
