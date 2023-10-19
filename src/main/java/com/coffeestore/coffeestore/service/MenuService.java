package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.menu.MenuRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    public Menu findByName(String menuName){
        Optional<Menu> menu = menuRepository.findByName(menuName);
        return menu.get();
    }

    public void createByMenu(MenuRegistrationRequestDto menuRegistrationRequestDto){
        Optional<Menu> menuCheck = menuRepository.findByName(menuRegistrationRequestDto.getName());
        if(menuCheck.isEmpty()){
            Menu menu = menuRegistrationRequestDto.toEntity();
            menuRepository.save(menu);
        }
    }
    public void stateChangeByMenu(Long menuId){
        Menu menu = findByMenu(menuId);
        if(menu.getState() == 0){
            menu.setState(1);
        }else {
            menu.setState(0);
        }
        menuRepository.save(menu);
    }
    public void updateByMenu(Long menuId, MenuUpdateRequestDto menuUpdateRequestDto){
        Menu menu = findByMenu(menuId);
        menu.update(menuUpdateRequestDto);
        menuRepository.save(menu);
    }

    public void deleteByMenu(Long id){
        Menu menu = findByMenu(id);
        menu.setState(0);
        menuRepository.save(menu);
    }
}
