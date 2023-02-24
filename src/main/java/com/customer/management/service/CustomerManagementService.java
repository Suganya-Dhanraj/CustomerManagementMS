package com.customer.management.service;

import java.util.List;

import com.customer.management.dto.CustomerDto;
import com.customer.management.entity.Customer;
import com.customer.management.exception.ResourceNotFoundException;

public interface CustomerManagementService {
	
	/**
	 * 
	 * @param customerDto
	 * @return String(success/failed)
	 */
	public CustomerDto addCustomer(CustomerDto customerDto);

	/**
	 * 
	 * @param id
	 * @return Customer
	 */
	public CustomerDto getCustomerById(String id) throws ResourceNotFoundException;

	/**
	 * 
	 * @param customerDto
	 * @return CustomerDto - with updated details
	 */
	public CustomerDto updateCustomer(CustomerDto customerDto) throws ResourceNotFoundException;

	/**
	 * 
	 * @param id
	 * @throws ResourceNotFoundException
	 */
	public void deleteCustomer(Long id) throws ResourceNotFoundException;
	
	/***
	 * 
	 * @return List<CustomerDto>
	 * @throws ResourceNotFoundException
	 */
	public List<CustomerDto> getAllCustomers() throws ResourceNotFoundException;

	/***
	 * 
	 * @return List<CustomerDto>
	 * @throws ResourceNotFoundException
	 */
	public List<CustomerDto> searchCustomerByName(String key) throws ResourceNotFoundException;;

}
