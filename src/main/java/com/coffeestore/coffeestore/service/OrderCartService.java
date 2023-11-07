package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
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


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCartService {
    private final OrderCartRepository orderCartRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public List<OrderCart> findByAll() {
        return orderCartRepository.findAll();
    }

    public User findByUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }

    // 나의 주문하지않은 카트들
    public List<OrderCart> findByUsers(User user) {
        return orderCartRepository.findByOrder_UserAndState(user,1);
    }

    public OrderCart findByOrderCart(Long id) {
        return orderCartRepository.findById(id).orElseThrow();
    }

    // orderCart 생성하거나 찾아서 order 반환
    public Order findByOrder(HttpServletRequest request) {
        User user = findByUser(request);
        return orderRepository.findOrderByUserAndState(user,1)
                .stream()
                .findFirst()
                .orElseGet(() -> orderRepository.save(Order.builder().user(user).state(1).build()));
    }
    public OrderCart createByOneOrderCart(HttpServletRequest request, Long menuId, int amount) {
        User user = findByUser(request);
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        Order order = findByOrder(request);

        // 이미 동일한 메뉴가 장바구니에 있는지 확인
        Optional<OrderCart> orderCartCheck = orderCartRepository.findByMenuAndOrder_UserAndState(menu, user, 1);

        //장바구니 여러개 구매
        if (orderCartCheck.isPresent()) {
            // 동일한 메뉴가 이미 장바구니에 있으면 수량만 증가시킴
            OrderCart orderCartInfo = orderCartCheck.get();
            orderCartInfo.setAmount(amount);
            orderCartRepository.save(orderCartInfo);
            return orderCartInfo;
        }
        // 동일한 메뉴가 없으면 새로운 OrderCart을 생성함.
        return orderCartRepository.save(OrderCart.builder().order(order).menu(menu).state(1).amount(amount).build());
    }

    public void createByOrderCart(HttpServletRequest request, Long menuId) {
        User user = findByUser(request);
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        Order order = findByOrder(request);

        // 이미 동일한 메뉴가 장바구니에 있는지 확인
        Optional<OrderCart> orderCartCheck = orderCartRepository.findByMenuAndOrder_UserAndState(menu, user, 1);

        //장바구니 여러개 구매
        if (orderCartCheck.isPresent()) {
            // 동일한 메뉴가 이미 장바구니에 있으면 수량만 증가시킴
            OrderCart orderCartInfo = orderCartCheck.get();
            orderCartInfo.setAmount(orderCartInfo.getAmount() + 1);
            orderCartRepository.save(orderCartInfo);
            return;
        }
            // 동일한 메뉴가 없으면 새로운 OrderCart을 생성함.
        orderCartRepository.save(OrderCart.builder().order(order).menu(menu).state(1).amount(1).build());
    }

    // 메뉴 수량 수정
    public void updateByOrderCartAmount(Long cartId, Integer amount) {
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
        List<OrderCart> orderCartCheck = orderCartRepository.findByOrder(orderCart.getOrder());
        if (orderCartCheck.isEmpty()) {
            orderRepository.delete(orderCart.getOrder());
        }
    }
}
