package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.dto.orderCart.OrderCartRegistrationRequestDto;
import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.repository.MenuRepository;
import com.coffeestore.coffeestore.repository.OrderCartRepository;
import com.coffeestore.coffeestore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderCartService {
    private final OrderCartRepository orderCartRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    //나의 주문하지않은 카트들
    public List<OrderCart> findByAll(){
        return orderCartRepository.findAll()
                .stream()
                .filter(OrderCart -> OrderCart.getState() == 1)
                .collect(Collectors.toList());
    }


    public OrderCart findByOrderCart(Long id){
        Optional<OrderCart> orderCart = orderCartRepository.findById(id);
        return orderCart.orElse(null);
    }

    //orderCart 생성하거나 찾아서 order 반환
    public Order findByOrder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        Order orderInfo = orderRepository.findAll()
                .stream()
                .filter(order -> order.getUser().equals(user)&&order.getState()==1)
                .findFirst()
                .orElse(null);
        if (orderInfo == null) {
            Order order = new Order();
            order.setUser(user);
            order.setState(1);
            return orderRepository.save(order);
        }
        else {
            return orderInfo;
        }
    }

    //OrderCart 생성하면서 order 주입함.
    public void createByOrderCart(HttpServletRequest request,Long id, OrderCartRegistrationRequestDto orderCartRegistrationRequestDto) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.get().getState() == 1) {
            OrderCart orderCart = orderCartRegistrationRequestDto.toEntity(findByOrder(request),menu);
            orderCartRepository.save(orderCart);
        }
    }

    //메뉴 수량 수정
    public void updateByOrderCartAmount(Long id,String amount){
        OrderCart orderCart = findByOrderCart(id);
        orderCart.setAmount(Integer.parseInt(amount));
        orderCartRepository.save(orderCart);
    }
    //주문내역에서 주문되지않은 메뉴 중 취소.
    //state == 1
    public void deleteByOrderCart(Long id) {
        OrderCart orderCart = findByOrderCart(id);
        if (orderCart.getState() == 1) {
            orderCartRepository.delete(orderCart);
        }
    }
}
