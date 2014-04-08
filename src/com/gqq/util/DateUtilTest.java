package com.gqq.util;

import static org.junit.Assert.*;

import org.junit.*;

public class DateUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetAfterDay() {
		String str = DateUtil.getAfterDay(2);
		assertEquals("星期四", str);
	}

}
