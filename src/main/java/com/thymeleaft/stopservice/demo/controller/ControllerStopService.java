package com.thymeleaft.stopservice.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thymeleaft.stopservice.demo.service.MyService;

import jakarta.annotation.PreDestroy;

@Controller
@RequestMapping(value = "/my-service-test")// api thymeleaft
public class ControllerStopService {
	
	@Autowired
	private MyService myService;
	
	private Thread myTread;
		
	@GetMapping({"/index","/", ""}) // accept list of path to call this endpoint
	public String index(){
		return "index";
	}
	
	@GetMapping("/start-scraping")
	public String startScraping(Model model) throws InterruptedException {
		
		myTread = new Thread(() -> {
			try {
				myService.stratService();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		myTread.start();
		
		return "redirect:/my-service-test/index"; 
		
	}
	
	@GetMapping("/stop-scraping")
	public String stopScraping(Model model) throws InterruptedException {
		
		myService.stopService();
		
		return "redirect:/my-service-test/index"; // redirect index of application
		
	}
	
	@GetMapping("/exit-close")
	public void exit() {
		System.exit(1);
	}
	
	// add initer binder to convert trim input string
	// remove leading and trailing white space
	// resole issue for our validation
	
	@InitBinder
	private void initBinder(WebDataBinder webDataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true); // trasnforma le string vuote in null
		
		webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor); // setti la proprieta dal binding web
		
	}
	
	@PreDestroy
	public void testPreDestroyApp() { // work
		System.out.println("by by have a good day");
	}

}
