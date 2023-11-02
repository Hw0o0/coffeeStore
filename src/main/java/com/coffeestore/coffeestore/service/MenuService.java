package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.menu.MenuRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.menu.MenuUpdateRequestDto;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> findByAll() {
        return menuRepository.findAll();
    }

    public Menu findByMenu(Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    public void createByMenu(MenuRegistrationRequestDto menuRegistrationRequestDto) {
       menuRepository.findByName(menuRegistrationRequestDto.getName()).
               orElseGet(()->menuRepository.save(menuRegistrationRequestDto.toEntity()));
    }

    public void stateChangeByMenu(Long menuId) {
        Menu menu = findByMenu(menuId);
        if (menu.getState() == 0) {
            menu.setState(1);
        } else {
            menu.setState(0);
        }
        menuRepository.save(menu);
    }

    public void updateByMenu(Long menuId, MenuUpdateRequestDto menuUpdateRequestDto) {
        Menu menu = findByMenu(menuId);
        menu.update(menuUpdateRequestDto);
        menuRepository.save(menu);
    }

    public void deleteByMenu(Long id) {
        Menu menu = findByMenu(id);
        menu.setState(0);
        menuRepository.save(menu);
    }

    public List<Menu> searchByMenu(String menuName) {
        List<Menu> menus = new ArrayList<>();
        //패턴을 컴파일한다.
        Pattern name = Pattern.compile(menuName);
        //문자열에서 패턴을 찾아내는 Matcher 를 통해 찾는다.
        for (Menu menu : findByAll()) {
            Matcher matcher = name.matcher(menu.getName());
            if (matcher.find()) {
                menus.add(menu);
            }
        }
        return menus;
    }

    public List<Menu> homeView() {
        return findByAll()
                .stream().filter(menu -> menu.getState() == 1)
                .collect(Collectors.toList());
    }

    public void sessionInvalidate(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
