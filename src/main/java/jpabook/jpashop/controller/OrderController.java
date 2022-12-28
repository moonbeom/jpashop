package jpabook.jpashop.controller;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/order")
    @ResponseBody
    public Order order() {
        //1. 멤버생성
        //2. 상품생성
        //3. 상품가격
        // 주문 !
        // 등록한 주문조회 ㅡ> 클라이언트한테 응답
        Member member = new Member();
        member.setName("서문법1");
        member.setAddress(new Address("구리12", "인창동31", "1192401"));
        Long memberId = memberService.join(member);

        Book book = new Book();
        book.setName("훼리포터1");
        book.setPrice(50000);
        book.setStockQuantity(1010);
        Long itemId = itemService.saveItem(book);

        Long orderId = orderService.order(memberId, itemId, 53);

//        return orderService.findOne(orderService.order(member.getId(), book.getId(), 2));
        return orderService.findOne(orderId);
    }
}


