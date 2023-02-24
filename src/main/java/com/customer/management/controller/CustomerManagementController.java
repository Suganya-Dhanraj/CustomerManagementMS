package com.customer.management.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.websocket.server.PathParam;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.customer.management.dto.CustomerDto;
import com.customer.management.exception.ResourceNotFoundException;
import com.customer.management.service.CustomerManagementService;
import com.customer.management.util.CustomerManagementValidator;

import lombok.extern.log4j.Log4j;
/**
 * 
 * @author Suganya
 * @version 0.1
 * @since 0.1
 */
@Log4j
@Controller
public class CustomerManagementController {
	
	private static final Logger logger = LogManager.getLogger(CustomerManagementController.class);
	
	@Autowired
	private CustomerManagementService customerMS;
	
	/**
	 * 
	 * @param customerDto
	 * @return ResponseEntity<String>
	 */
	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody CustomerDto customerDto){
		
		logger.info("addCustomer - start");
		
		//Validate Input Date
		if(customerDto.getName().isEmpty() || 
				customerDto.getPanCard().isEmpty() ||
				customerDto.getAadhar().isEmpty() ||
				!CustomerManagementValidator.validateMail(customerDto.getEmailId())) {
			return new ResponseEntity<String>("Input data is not valid.", HttpStatus.BAD_REQUEST);			
		}
		//Invoke service method to addCustomer
		CustomerDto CustomerDtoNew = customerMS.addCustomer(customerDto);
		
		
		if(Objects.nonNull(CustomerDtoNew)) {
			//If customer added return 200
			logger.info("Customer added successfully");
			return new ResponseEntity<CustomerDto>(CustomerDtoNew, HttpStatus.OK);
		}else {
			//If customer add failed return 400
			logger.info("Customer add failed");
			return new ResponseEntity<String>("Customer not added in the system", HttpStatus.BAD_REQUEST);
		}
									
	}
	
	/**
	 * Method to update customer details
	 * 
	 * @param customerDto
	 * @return ResponseEntity<String>
	 */
	@PutMapping("/updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody CustomerDto customerDto){
		logger.info("updateCustomer - start");
		// Check customer id is available in request
		if(customerDto.getId().isEmpty()) {
			return new ResponseEntity<String>("Please provide customer id ", HttpStatus.BAD_REQUEST);			
		}
		
		//Update Customer Details
		try {
			CustomerDto customerDtoUpdated = customerMS.updateCustomer(customerDto);
			return new ResponseEntity<CustomerDto>(customerDtoUpdated,HttpStatus.OK);
		}catch(ResourceNotFoundException ex) {
			logger.error("Customer not found for given customer id");	
			return new ResponseEntity<String>("Customer not found for given id", HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id){
		logger.info("deleteCustomer - start");
		//Delete customer Details
		try {
			customerMS.deleteCustomer(id);
			return new ResponseEntity<String>("Customer details deleted successfully",HttpStatus.OK);
		}catch(ResourceNotFoundException ex) {
			logger.error("Customer not found for given customer id");	
			return new ResponseEntity<String>("Customer not found for given id", HttpStatus.NOT_FOUND);
		}	
	}
	
	@GetMapping("/getCustomer/byId/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable(value = "id") String id){
		logger.info("deleteCustomer - start");
		//Get customer Details by id
		try {
			CustomerDto customerDto = customerMS.getCustomerById(id);
			return new ResponseEntity<CustomerDto>(customerDto,HttpStatus.OK);
		}catch(ResourceNotFoundException ex) {
			logger.error("Customer not found for given customer id");	
			return new ResponseEntity<String>("Customer not found for given id", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/getAllCustomers")
	public ResponseEntity<?> getAllCustomers(){
		logger.info("getAllCustomers - start");
		//Get all customer Details
		try {
			List<CustomerDto> customerDtoList = customerMS.getAllCustomers();
			return new ResponseEntity<List<CustomerDto>>(customerDtoList,HttpStatus.OK);
		}catch(ResourceNotFoundException ex) {
			logger.error("Customer not found for given customer id");	
			return new ResponseEntity<String>("No Customer found", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/search/byName/{key}")
	public ResponseEntity<?> searchByName(@PathVariable(value="key")String key){
		logger.info("Search Customers by Name - start");
		//Get all customer Details
		try {
			List<CustomerDto> customerDtoList = customerMS.searchCustomerByName(key);
			return new ResponseEntity<List<CustomerDto>>(customerDtoList,HttpStatus.OK);
		}catch(ResourceNotFoundException ex) {
			logger.error("Customer not found for given customer id");	
			return new ResponseEntity<String>("No Customer found", HttpStatus.NOT_FOUND);
		}		
	}
	
	
	

}
