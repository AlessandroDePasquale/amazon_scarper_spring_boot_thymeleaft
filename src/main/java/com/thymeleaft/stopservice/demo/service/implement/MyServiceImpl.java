package com.thymeleaft.stopservice.demo.service.implement;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thymeleaft.stopservice.demo.service.MyService;
import com.thymeleaft.stopservice.demo.util.Utility;

@Service
public class MyServiceImpl implements MyService{
	
	@Autowired
	private Utility util;
	
	 private Boolean serviceRunning = true;

	@Override
	public void stratService() throws InterruptedException {
		serviceRunning = true;
		while(serviceRunning) {
			System.out.println(" ia m running now : " + LocalTime.now());
			Thread.sleep(1000);
			for(int i = 0; i<5; i++) {
				util.testMeServiceStoppedAutowired();
			}
		}
	}

	@Override
	public void stopService() {
		
		//try stop service method
		
		System.out.println("try stop service method");
		
		this.serviceRunning = false;
		
	}

}
