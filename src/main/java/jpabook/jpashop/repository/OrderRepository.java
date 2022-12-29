package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order); //주문 저장
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //주문 조회(동적)
    // jpql이 쿼리다. java Persistence Query Language 인 JPQL은 객체지향 쿼리로 JPA가 지원하는 다양한 쿼리 방법 중 하나
    // SQL
    //테이블을 대상으로 쿼리
    //JPQL
    //엔티티 객체를 대상으로 쿼리를 하게 된다. query는 JpaRepository 를 상속하는 인터페이스에서 사용한다.
    public List<Order> findAll(OrderSearch orderSearch) {
        // jpql에 아무것도 없을 때는 "select o From Order o join o.member m"이고 추가될 때마다 여기서 붙여넣기 하면 됨.
        String jpql = "select o From Order o join o.member m"; // where o.status = :status and m.name like : name가 붙음. 둘 다 성공이면
        boolean isFirstCondition = true;                       // 주문상태만 있을 때 where o.status = :status
        // 회원만 있을 때 where m.name like : name
        // 주문상태가 있으면 if문 블록 실행
        if (orderSearch.getOrderStatus() != null) {   //ordersearch의 상태를 확인하기 위해 사용
            if (isFirstCondition) {
                jpql += " where"; //조건이 있다. where로
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        //회원과 주문상태를 파악해서
        //동적 쿼리를 생성
        //쿼리는 DB에 전달 ( -> JPA)
        //JPQL 쿼리 생성 -> JPA에 전달.
        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);
        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
}



