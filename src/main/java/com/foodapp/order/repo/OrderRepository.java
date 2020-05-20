package com.foodapp.order.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodapp.order.dto.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

	@Query("{'customerId':?0, 'orderStatus':?1}")
	public List<Order> getAllOrdersForCustomerId(String customerId, String orderStatus);
	
}
