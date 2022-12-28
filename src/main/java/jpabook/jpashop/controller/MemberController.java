package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    @ResponseBody
    public Member join(@RequestParam("name") String name) {
        // 회원 만들고
        // DB 저장해서
        // 다시 DB에서 해당 회원 불러오고
        // 그 결과를 클라이언트로 응답.
        Member member = new Member(); //회원 만들고
        member.setName(name);

        member.setAddress(new Address("인천", "영종도", "10004"));// DB에 저장

        return memberService.findOne(memberService.join(member)); // DB에서 불러오고 그 결과를 리턴.
    }
}
