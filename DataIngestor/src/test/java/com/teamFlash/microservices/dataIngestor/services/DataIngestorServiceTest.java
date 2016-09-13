package com.teamFlash.microservices.dataIngestor.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DataIngestorServiceTest {

	@Test
	public void test()
	{
		String url=new DataIngestorService().getData();
		assertEquals(String.class,url.getClass());
	}
}
