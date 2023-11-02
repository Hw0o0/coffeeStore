package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.Menu;
import com.coffeestore.coffeestore.entity.Order;
import com.coffeestore.coffeestore.entity.OrderCart;
import com.coffeestore.coffeestore.entity.User;
import com.coffeestore.coffeestore.repository.MenuRepository;
import com.coffeestore.coffeestore.repository.OrderCartRepository;
import com.coffeestore.coffeestore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCartService {
    private final OrderCartRepository orderCartRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public List<OrderCart> findByAll(){
        return orderCartRepository.findAll();
    }
    //나의 주문하지않은 카트들
    public List<OrderCart> findByUser(User user){
        return orderCartRepository.findAll()
                .stream()
                .filter(orderCart -> orderCart.getState()==1&&orderCart.getOrder().getUser().equals(user))
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

    public OrderCart createByOrderCart(HttpServletRequest request, Long menuId) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        Optional<Menu> menu = menuRepository.findById(menuId);
        Order order = findByOrder(request);

        // 이미 동일한 메뉴가 장바구니에 있는지 확인
        Optional<OrderCart> orderCartCheck = orderCartRepository.findByMenuAndOrder_UserAndState(menu.get(), user, 1);
        //장바구니 여러개 구매
        if (orderCartCheck.isPresent()) {
            // 동일한 메뉴가 이미 장바구니에 있으면 수량만 증가시킴
            OrderCart orderCartInfo = orderCartCheck.get();
            orderCartInfo.setAmount(orderCartInfo.getAmount() + 1);
            orderCartRepository.save(orderCartInfo);
            return orderCartInfo;
        } else {
            // 동일한 메뉴가 없으면 새로운 OrderCart을 생성함.
            OrderCart orderCart = new OrderCart();
            orderCart.setOrder(order);
            orderCart.setMenu(menu.get());
            orderCart.setState(1);
            orderCart.setAmount(1);
            orderCartRepository.save(orderCart);
            return orderCart;
        }
    }

    //메뉴 수량 수정
    public void updateByOrderCartAmount(Long cartId,Integer amount){
        OrderCart orderCart = findByOrderCart(cartId);
        orderCart.setAmount(amount);
        orderCartRepository.save(orderCart);
    }
    //주문내역에서 주문되지않은 메뉴 중 취소.
    //장바구니 삭제할떄 담긴게 없으면 order도 자동으로 삭제
    public void deleteByOrderCart(Long cartId) {
        OrderCart orderCart = findByOrderCart(cartId);
        if (orderCart.getState() == 1) {
            orderCartRepository.delete(orderCart);
        }
        OrderCart orderCartCheck = orderCartRepository.findAll()
                .stream()
                .filter(orderCartInfo -> orderCartInfo.getOrder().getId().equals(orderCart.getOrder().getId()))
                .findFirst()
                .orElse(null);
        if(orderCartCheck == null){
            orderRepository.delete(orderCart.getOrder());
        }
    }
}
