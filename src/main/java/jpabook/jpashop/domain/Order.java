package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// "ORDER BY"
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    // 연관관계의 주인 : FK 있는 쪽 (Order)
    // 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // -> DB의 FK
    private Member member; //멤버 객체로 쓸 때 이렇게 필드 선언을 해줘야한다.

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // domain 내에 메소드를 선언
    // == 연관 관계 ==
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); //이게 이해가 안갈 수 있다. 개념적으로 중요함. 상대 테이블(객체)에서도 알아야한다. 상대 객체 안에도 오더가 있으니 셋팅을 해주는 게 좋다.
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //수도코드

//    public static void main(String[] args) {
//        Member member = new Member();
//        Order order = new Order();
//        order.setMember(member);
//
//        //
//        Member join order on order.memberId = memberId

    // 비즈니스 로직
    // 주문 등록
    public static Order createOrder(Member member, Delivery delivery,
                                    OrderItem... orderItems) { //...은 여러개 받을 수 있다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //주문 취소
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) { //맨 위 delivery를 뜻하는거.
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        // 주문상품에 대한 재고를 증가시켜야함
        // 지금 오더에 대한 주문상품들이 여러 개 있을 수 있다.
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 = totalPrice
    //짜장면 3000 = orderPrice 2그릇 =count
    //짬뽕 5000 3그릇
    //탕수육 10000 1그릇

}
