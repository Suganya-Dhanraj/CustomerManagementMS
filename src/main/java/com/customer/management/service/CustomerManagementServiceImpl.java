package com.customer.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.customer.management.dto.CustomerDto;
import com.customer.management.entity.Customer;
import com.customer.management.exception.ResourceNotFoundException;
import com.customer.management.repository.CustomerRepository;

/**
 * 
 * @author Suganya
 * @version 0.1
 * @since 0.1
 */
@Service
public class CustomerManagementServiceImpl implements CustomerManagementService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private static final Logger logger = LogManager.getLogger(CustomerManagementServiceImpl.class);

	/**
	 * Method to add Customer details in DB
	 * 
	 * @param customerDto
	 * @return String(success/failed)
	 */
	@Override
	public CustomerDto addCustomer(CustomerDto customerDto) {
		// Convert customerDto to Customer Entity
		Customer customer = convertCustomerDtoToCustomer(customerDto);
		
		try {
			//Save Customer details in DB
			Customer customerNew = customerRepository.save(customer);
			logger.info("Customer added successfully. Customer Id is "+customerNew.getCustomerId());;
			return convertCustomerToCustomerDto(customerNew);
		}catch(Exception ex) {
			//If exception occurred print exception message
			logger.error("Exception occurred in addCustomer. Exception message is "+ex.getMessage());
			return null;			
		}
	}
	
	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto) throws ResourceNotFoundException {
		Customer customerOld = customerRepository
				.findByCustomerId(Long.valueOf(customerDto.getId()));
		if(Objects.isNull(customerOld)) {
			throw new ResourceNotFoundException("Customer not found");
		}
		
		Customer customerNew = convertCustomerDtoToCustomer(customerDto);
		customerNew.setCustomerId(customerOld.getCustomerId());
		customerNew.setRecordCreationDate(customerOld.getRecordCreationDate());
		customerNew.setRecordUpdatedDate(LocalDateTime.now());
		
		customerNew = customerRepository.save(customerNew);
		return convertCustomerToCustomerDto(customerNew);
	}
	
	@Override
	public void deleteCustomer(Long id) throws ResourceNotFoundException {
		Customer customerOld = customerRepository.findByCustomerId(id);
		if(Objects.isNull(customerOld)) {
			throw new ResourceNotFoundException("Customer not found");
		}
		customerRepository.delete(customerOld);
	}
	
	@Override
	public CustomerDto getCustomerById(String id) throws ResourceNotFoundException{
		Customer customer = customerRepository.findByCustomerId(Long.valueOf(id));
		if(Objects.isNull(customer)) {
			throw new ResourceNotFoundException("Customer not found");
		}
		return convertCustomerToCustomerDto(customer);
	}
	
	@Override
	public List<CustomerDto> getAllCustomers() throws ResourceNotFoundException {
		List<Customer> customerList = customerRepository.findAll();
		if(Objects.isNull(customerList)){
			throw new ResourceNotFoundException("No Customer found");
		}
		List<CustomerDto> customerDtoList = customerList.stream()
				.map(customer -> convertCustomerToCustomerDto(customer))
				.collect(Collectors.toList());
		return customerDtoList;
	}	
	
	@Override
	public List<CustomerDto> searchCustomerByName(String key) throws ResourceNotFoundException {
		List<Customer> customerList = customerRepository.findByNameContaining(key);
		if(Objects.isNull(customerList)){
			throw new ResourceNotFoundException("No Customer found");
		}
		List<CustomerDto> customerDtoList = customerList.stream()
				.map(customer -> convertCustomerToCustomerDto(customer))
				.collect(Collectors.toList());
		return customerDtoList;
	}

	/***
	 * Method to convert Customer to customerDto 
	 * 
	 * @param customerNew
	 * @return CustomerDto
	 */
	private CustomerDto convertCustomerToCustomerDto(Customer customerNew) {
		final CustomerDto customerDto = new CustomerDto();
		customerDto.setAadhar(customerNew.getAadhar());
		customerDto.setAddress1(customerNew.getAddress1());
		customerDto.setAddress2(customerNew.getAddress2());
		customerDto.setDob(customerNew.getDob());
		customerDto.setEmailId(customerNew.getEmailId());
		customerDto.setId(customerNew.getCustomerId().toString());
		customerDto.setName(customerNew.getName());
		customerDto.setPanCard(customerNew.getPanCard());
		customerDto.setPhoneNumber(customerNew.getPhoneNumber());
		return customerDto;
	}

	/**
	 * Method to convert customerDto to Customer
	 * 
	 * @param customerDto
	 * @return Customer
	 */
	private Customer convertCustomerDtoToCustomer(CustomerDto customerDto) {
		final Customer customer = new Customer();
		customer.setAadhar(customerDto.getAadhar());
		customer.setAddress1(customerDto.getAddress1());
		customer.setAddress2(customerDto.getAddress2());
		customer.setDob(customerDto.getDob());
		customer.setEmailId(customerDto.getEmailId());
		customer.setGender(customerDto.getGender());
		customer.setName(customerDto.getName());
		customer.setPanCard(customerDto.getPanCard());
		customer.setPhoneNumber(customerDto.getPhoneNumber());
		return customer;
	}

}
