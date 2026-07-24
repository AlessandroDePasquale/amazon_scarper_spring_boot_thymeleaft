package com.thymeleaft.stopservice.demo.service;

import java.util.List;

import com.thymeleaft.stopservice.demo.model.ProductionModel;

public interface ConnectionService {
	
	void scaperStart(List<ProductionModel> production) throws Exception;
	
	void startExecution();
	
	void stopScraper();
	
}
