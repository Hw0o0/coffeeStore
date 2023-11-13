package com.coffeestore.coffeestore.service;

import com.coffeestore.coffeestore.entity.*;
import com.coffeestore.coffeestore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderCartRepository orderCartRepository;

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    private final IngredientRepository ingredientRepository;
    private final AddPointRepository addPointRepository;
    private final UsedPointRepository usedPointRepository;
    //session user 반환
    public User findByUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return (User) session.getAttribute("user");
    }
    // 자신의 주문만 볼수 있게끔 함.
    public List<Order> findAll(HttpServletRequest request) {
        User user = findByUser(request);
        String manager = "1";
        if (!manager.equals(user.getName())) {
            return orderRepository.findOrderByUserAndState(user,0);
        }else{ return orderRepository.findOrderByState(0);}
    }

    public Order findByOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    //orderView를 위한 메소드
    public List<OrderCart> createByOrderPageView(Order order) {
        List<OrderCart> orderCartList = orderCartRepository.findOrderCartByOrder(order)
                .stream()
                .filter(orderCart -> orderCart.getState()!=0)
                .collect(Collectors.toList());
        orderCartList.forEach(orderCart -> orderCart.setState(1));
        orderCartRepository.saveAll(orderCartList);
        order.setTotalPrice(totalPrice(orderCartList));
        orderRepository.save(order);
        return orderCartList;
    }

    // 주문 금액 계산
    public int totalPrice(List<OrderCart> orderCartList){
        return orderCartList.stream()
                .mapToInt(orderCart -> orderCart.getMenu().getPrice() * orderCart.getAmount())
                .sum();
    }
    // 메뉴에 사용된 재료로 인한 재료 수량 수정 메소드
    public void menuUsedRecipeMinus(OrderCart orderCart) {
        recipeRepository.findRecipeByMenu_Id(orderCart.getMenu().getId())
                .forEach(recipe -> {
                    int usedRecipe = recipe.getAmount() * orderCart.getAmount();
                    Ingredient ingredient = recipe.getIngredient();
                    ingredient.setAmount(recipe.getIngredient().getAmount() - usedRecipe);
                    ingredientRepository.save(ingredient);
                });
    }

    //내 현재 주문 찾기
    public Order findByOrder(HttpServletRequest request) {
        User user = findByUser(request);
        return orderRepository.findOrderByUser_IdAndState(user.getId(),1)
                .orElse(null);
    }

    // 주문 완료 시 시작하는 메소드
    public void orderOk(String payMethod, Long orderId,Integer usedPoint) { //사용금액 받아와야한다.
        Order orderInfo = findByOrder(orderId);
        User user = orderInfo.getUser();
        // 이미 위에서 state 1,2인지 다 거르고 올라와서 만약 2가 있으면 하나 구매이다.
        List<OrderCart> orderCartList = orderCartRepository.findOrderCartByOrder(orderInfo);
        OrderCart orderCart = orderCartList
                .stream()
                .filter(orderCart1 -> orderCart1.getState()==2)
                .findFirst()
                .orElse(null);

        // 메뉴 여러개 주문할 떄 상황
        if(orderCart == null) {
            //포인트 사용하는 구간
            if(usedPoint != 0){
                user.setPoint(user.getPoint()-usedPoint);
                user.setStamp(user.getStamp()-10);
                orderInfo.setTotalPrice(orderInfo.getTotalPrice()-usedPoint);
                usedPointRepository.save(UsedPoint.builder().user(orderInfo.getUser()).usedContent("사용내역").usedPrice(-usedPoint).createdAt(new Date()).build());
            }
            //포인트 적립 시키는 구간
            int addPoint = orderInfo.getTotalPrice() / 10;
            user.setStamp(user.getStamp() + 1);
            user.setPoint(user.getPoint() + addPoint);
            addPointRepository.save(AddPoint.builder().user(orderInfo.getUser()).addContent("적립 내용").addPrice(addPoint).createdAt(new Date()).build());
            userRepository.save(user);

            orderInfo.orderOk(payMethod);
            orderRepository.save(orderInfo);

            orderCartList.forEach(orderCart2 -> {
                menuUsedRecipeMinus(orderCart2);
                orderCart2.setState(0);
            });
            orderCartRepository.saveAll(orderCartList);


        // 메뉴 하나 주문할 떄 상황
        }else{
            orderCartRepository.delete(orderCart);
            Order order = Order.builder()
                    .user(orderInfo.getUser())
                    .paymentMethod(payMethod)
                    .totalPrice(orderCart.getAmount()*orderCart.getMenu().getPrice())
                    .state(0)
                    .createdDate(new Date())
                    .build();
            //포인트 사용 구간
            if(usedPoint != 0){
                user.setStamp(user.getStamp()-10);
                user.setPoint(user.getPoint()-usedPoint);
                order.setTotalPrice(order.getTotalPrice()-usedPoint);
                usedPointRepository.save(UsedPoint.builder().user(orderInfo.getUser()).usedContent("사용내역").usedPrice(-usedPoint).createdAt(new Date()).build());
            }
                //포인트 적립 시키는 구간
                int addPoint = orderCart.getAmount()*orderCart.getMenu().getPrice()/10;
                user.setPoint(user.getPoint()+addPoint);
                user.setStamp(user.getStamp()+1);
                addPointRepository.save(AddPoint.builder().user(orderInfo.getUser()).addContent("적립 내용").addPrice(addPoint).createdAt(new Date()).build());


            userRepository.save(user);
            orderRepository.save(order);

            orderCart.setOrder(order);
            orderCart.setState(0);
            orderCartRepository.save(orderCart);
        }
    }
    public Order findByOrderAndSetPrice(Long orderId, OrderCart orderCart) {
        Order order = findByOrder(orderId);
        order.setTotalPrice(orderCart.getAmount() * orderCart.getMenu().getPrice());
        orderRepository.save(order);
        orderCart.setState(2);
        orderCartRepository.save(orderCart);
        return order;
    }

    public List<Order> findByOrderUsers(String userName) {
        return orderRepository.findOrderByUserName(userName);
    }
}