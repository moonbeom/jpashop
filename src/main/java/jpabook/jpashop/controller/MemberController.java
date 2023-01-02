package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); //키가 당연히"memberForm"이다. th:object가 memberForm이기 때문에
       return "members/createMemberForm";
    }
//    public Member join(@RequestParam("name") String name) { //view를 던져주자.
//        // 회원 만들고
//        // DB 저장해서
//        // 다시 DB에서 해당 회원 불러오고
//        // 그 결과를 클라이언트로 응답.
//        Member member = new Member(); //회원 만들고
//        member.setName(name);
//
//        member.setAddress(new Address("인천", "영종도", "10004"));// DB에 저장
        // 뷰 :members/createMemberForm
//        return memberService.findOne(memberService.join(member)); // DB에서 불러오고 그 결과를 리턴.
//    }
    @PostMapping("/members/new")
    public String join(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm"; //값을 재반환
        }
        // form에 클라이언트가 보낸 데이터를 받도록 수정
        System.out.println(form.getName());
        System.out.println(form.getCity());
        System.out.println(form.getStreet());
        System.out.println(form.getZipcode());
        // 저장하고 홈으로

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(
                form.getCity(),
                form.getStreet(),
                form.getZipcode()));

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); //view로 떤저주기 위해서
        return "members/memberList";
    }




}
