package com.project.Payroll.Repositories;

import com.project.Payroll.Links.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
