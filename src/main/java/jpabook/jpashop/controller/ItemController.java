package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //    public Item newItem(@RequestParam String name) {
//        Book book = new Book();
//        book.setName(name);
//
//        return itemService.findOne(itemService.saveItem(book));
//
//    }
    @GetMapping("items/new")
    public String newItem(Model model) { //모델은 뷰에 데이터를 전달해주는 매개체이다.

        model.addAttribute("form", new BookForm()); // 모델을 북폼을 생성해서 담는 걸
        // itemForm이 아닌 form으로 되어있기 때문에 form으로 돌려줘야한다.

        return "items/createItemForm"; //여기로 간다. 뷰 호출
    }

    @PostMapping("items/new") //@PostMapping 어노테이션이있는 메소드는 주어진 URI 표현식과 일치하는 HTTP POST 요청을 처리합니다.
    //HTTP POST 메서드는 서버로 데이터를 전송한다.
    //- POST 요청은 보통 HTML 양식을 통해 서버에 전송하며, 서버에 변경사항을 만든다.
    public String Item(BookForm form) { //
        Book book = new Book();

        book.setName(form.getName());
        book.setAuthor(form.getAuthor());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());
        book.setId(form.getId());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("items")
    public String List(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        // 아이템 조회
        Item item = itemService.findOne(id);
        BookForm form = new BookForm();//생성
        form.setName(item.getName());
        form.setAuthor(item.getAuthor());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());
        form.setId(item.getId());

        model.addAttribute("form", form);



        return"items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")  //db에서 가지고 와서 id 가져와서
    public String update(@ModelAttribute("form") BookForm form) {
        // 클라이언트에게 받은 데이터로 수정해주고
        itemService.update(form.getId(),form.getName(),form.getPrice(),form.getStockQuantity());
        //        itemService.update(item);
        //다시 db로 보내준다.
        return "redirect:/items";
    }



}
