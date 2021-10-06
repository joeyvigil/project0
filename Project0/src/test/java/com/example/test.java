package com.example;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.example.DAO;

public class test {
	
//	@BeforeEach
//	public void method throws Exception() {
//		
//	}
	
	
	DAO dao1=new DAO();


	@Test
	public void testisactive() {
		assertEquals(true, dao1.isactive(1));
	}

	@Test
	public void testAWord() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 10000 + 1);
		String fields="fields"+randomNum;
		dao1.createuser(fields, fields, "customer", fields, fields);
		assertEquals("customer", dao1.usertype("hi"));
	}

	@Test
	public void owntest() {
		assertEquals(true, dao1.ownershipcheck("monkey", 1));
	}

	@Test
	public void passtest() {
		assertEquals(false, dao1.passwordcheck("monkey", "banana"));
	}

	@Test
	public void userexisttest() {
		assertEquals(false, dao1.userexists("mosdfasdf"));
	}

}
