package com.example.project001a.repository;
import com.example.project001a.entity.Order;
import com.example.project001a.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);


}
