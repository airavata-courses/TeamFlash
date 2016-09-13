package com.teamFlash.microservices.dataIngestor.services;

public class DataIngestorService {
	
	public String getData()
	{
		System.out.println("in DataIngestorService.getData ---");
		String url="https://noaa-nexrad-level2.s3.amazonaws.com/1999/04/05/KAMX/KAMX19990405_000408.gz";
		return url;
	}

}
