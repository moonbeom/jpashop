package jpabook.jpashop.controller;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@Slf4j 요고를 통해서 알아보기 가능 console.log 같은 역할을 한다.
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/orders/new")
    public String orderForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems(); //상품 전체 저회를 하고 모델에다가 집에 넣어줘야함.
        //모델이 제일 중요한 아이임. 제대로 숙지하고 있어야 한다.
        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "/orders/orderForm";
    }

    //db에 데이터가 없으니 추가를 해보자. 주문회원이랑 상품명을 하나 선택하고 카운트를 만들어보자.
    @PostMapping("/orders/new") //포스트 한다는 것은 데이터를 까먹음. 주문회원 상품명 카운트 받아줌.
    public String order(
            @RequestParam("memberId") Long memberId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("count") int count){ //매개변수로 만들어야함. 하나하나씩 테스트를 해줘야함. 그때마다 테스트해야함.
//        log.info(memberId.toString()); 명확하게 주기 위해서 RequestParam을 주는 거임. 굳이 안줘두 됨.
//        log.info(itemId.toString()); 필요한 재료를 다 갖고 있으니까 이제 오더해주면 끝임.
//        log.info(String.valueof(count)); 매개변수만 있으면 된다. 왜냐면 서비스에서 다 만들어놨기 때문에. 조립만 하면 됨.
        //이걸 db를 통해서도 확인할 수 있음.
        orderService.order(memberId, itemId, count);

        return "redirect:/";
    }
    //지금 orderSearch랑 orders가 필요하다. 두개를 넣어주면 됨. 리스트에다가
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) { //"orderSearch"가 key고 orderSearch가 밸류임
        // orderSearch , orders 상태에 따른 리스트가 다르기 때문에 ModelAttribute를 만든거다.
        List<Order> orders = orderService.searchOrders(orderSearch); //전부다 null일 때는 전부 가져온다. 얘내가 다 null이다보니
//        model.addAttribute("orderSearch", new OrderSearch()); //만들 수는 있지만 검색이 안된다. 얘가 있으면 그래서 뷰에 th:object에 {orderSearch}가 등록된거임.
        model.addAttribute("orders", orders); // 조건이 충족할 때는 내가 선택한 요 녀석만 가져온다.
        return "orders/orderList";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id){
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }



}


//    @PostMapping("/order")
//    public String order() {
//
//    }
//}

        //1. 멤버생성
        //2. 상품생성
        //3. 상품가격
        // 주문 !
        // 등록한 주문조회 ㅡ> 클라이언트한테 응답
//        Member member = new Member();
//        member.setName("서문법1");
//        member.setAddress(new Address("구리12", "인창동31", "1192401"));
//        Long memberId = memberService.join(member);
//
//        Book book = new Book();
//        book.setName("훼리포터1");
//        book.setPrice(50000);
//        book.setStockQuantity(1010);
//        Long itemId = itemService.saveItem(book);
//
//        Long orderId = orderService.order(memberId, itemId, 53);
//
////        return orderService.findOne(orderService.order(member.getId(), book.getId(), 2));
//        return orderService.findOne(orderId);



