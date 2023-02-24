package com.customer.management.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.customer.management.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>{
	
	public Customer findByCustomerId(Long customerId);
	
	public List<Customer> findAll();

	public List<Customer> findByNameContaining(String key);

}
