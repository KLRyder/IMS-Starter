package com.qa.ims.persistence.domain;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class CustomerTest {

	@Test
	public void testEquals() {
		EqualsVerifier.simple().forClass(Customer.class).verify();
	}

	@Test
	public void testCompare(){
		Customer c1 = new Customer(1L, "cust", "one");
		Customer c2 = new Customer(2L, "custom", "two");
		Customer c3 = new Customer(3L, "customer", "three");

		ArrayList<Customer> customersUnsorted = new ArrayList<>();
		customersUnsorted.add(c3);
		customersUnsorted.add(c1);
		customersUnsorted.add(c2);

		ArrayList<Customer> customers = new ArrayList<>();
		customers.add(c1);
		customers.add(c2);
		customers.add(c3);

		Collections.sort(customersUnsorted);

		assertEquals(customersUnsorted, customers);
	}

}
