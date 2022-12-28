package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired //얘내는 의존성 주입을 받기 위해서 하는거다.
    OrderRepository orderRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;

    @Test
    public void 주문취소() {
        //먼저 주문을 한다음에 주문을 취소해야 한다.
        //given
        Member member = new Member();
        member.setName("서문범");
//        em.persist(member); //이건 DB에 저장하는 거
        Long memberId = memberService.join(member);

        Item item = new Book();
        item.setName("해리포터");
        item.setPrice(10000);
        item.setStockQuantity(50);
        Long itemId = itemService.saveItem(item);

        Long orderId = orderService.order(memberId, itemId, 1);
        System.out.println("해리포터 재고" + item.getStockQuantity());

        //when 주문취소
        orderService.cancelOrder(orderId);

        //then

        Order order = orderRepository.findOne(orderId);

        //검증
        // 1. OrderStatus가 CANCEL로 변경
        // 2. 주문 취소된 상품은 그만큼 재고가 다시 증가해야 한다.
        assertEquals(order.getStatus(), OrderStatus.CANCEL); //1
        assertEquals(item.getStockQuantity(), 50); //2

    }

}