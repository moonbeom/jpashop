package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) { em.persist(member); // 저장 끝.
    }


    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", //:name은 셋파라미터에 해당하는 네임을 집어넣을 것이다. 요녀석은 "name", name 밸류에 있는 녀석으로 넣어준다.
                Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
