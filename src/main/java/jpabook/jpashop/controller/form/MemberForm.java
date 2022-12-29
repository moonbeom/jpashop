package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    @NotEmpty(message = "도시 이름은 필수입니다")
    private String city;
    private String street;
//    @Size(min = 4, max = 5) 이거 작성하면 글자수 제한됨.
    private String zipcode;
}//데이터 받을 용도로 폼을 만들어줌. Entity 영속성 문제 ㄸㅐ문에
