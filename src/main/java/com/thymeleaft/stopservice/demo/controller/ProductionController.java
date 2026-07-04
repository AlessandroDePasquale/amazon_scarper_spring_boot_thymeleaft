package com.thymeleaft.stopservice.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thymeleaft.stopservice.demo.model.ProductionModel;
import com.thymeleaft.stopservice.demo.service.ConnectionService;
import com.thymeleaft.stopservice.demo.service.ProductionService;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/production")// api thymeleaft
public class ProductionController {
	
	@Autowired
    private ProductionService productionService;
	
	@Autowired
	private ConnectionService connectionService;
	
//	private Thread myThread;
		
	@GetMapping({"/index","/", ""}) // accept list of path to call this endpoint
	public String index(){
		return "index";
	}
	
	// get list productions 
	
	@GetMapping("/show-list-prodution") // ommision value work any way
	public String listproductions(Model model) {
		
		// get all productions from db
		List<ProductionModel> listproduction = productionService.findAll();
		
		log.info(listproduction.toString());
		
		// add list to model spring boot to seend to html page thymeleaft ?
		model.addAttribute("productions", listproduction); // frist param name of obj into html will be macth
		
		return "production/list-productions";
	}
	
	// get html form add new production
	
	@GetMapping("/form-add-production")
	public String showFormAddproduction(Model model) {
		
		model.addAttribute("production", new ProductionModel()); // set empty obj other ways null pointer into html on th:object="${production}" :  Neither BindingResult nor plain target object for bean name 'production' available as request attribute
		
		return "production/form-add-production";
	}
	
	// save production
	
	@PostMapping("/save-production")
	public String addproduction(@ModelAttribute ProductionModel production, Model model) {
		
		productionService.checkFieldObj(production);
		
		productionService.save(Arrays.asList(production));
		
		return "redirect:/production/show-list-prodution"; // call endpoint to show table list of productions work fine
	}
	
	@PostMapping("/update-production")
	public String updateProduction(@ModelAttribute ProductionModel production, Model model) {
				
		productionService.updateProdution(production);
		
		return "redirect:/production/show-list-prodution"; // call endpoint to show table list of productions work fine
	}
	
	// get html modify production
	
//	@GetMapping("/modify")
//	public String showModifyproduction(@ModelAttribute production production, Model model) {
//		
//		model.addAttribute("production", production);
//		
//		return "productions/modify";
//	}
	
	@GetMapping("/form-update-production")
	public String showFormUpdateproduction(@RequestParam("id") Long id, Model model) {
		
		log.info("id production to update : " + id);
		
		ProductionModel production = productionService.findById(id);
		
		model.addAttribute("production", production);
		
		return "production/form-update-production"; // re-caal form insert but seend obj found on db for update
	}
	
	// delete production
	
	@GetMapping("/delete") // metodh put and delete sembra che non riesco a chiamalri da thymeleft
	public String deleteproduction(@RequestParam Long id, Model model) throws Exception {
		
		log.info("id to delete : " + id);
		
		productionService.deleteById(id);
		
		return "redirect:/production/show-list-prodution";
		
	}
	
	@GetMapping("/start-scraping")
	public String startScraping(Model model) {

		connectionService.startExecution();

		return "redirect:/production/index"; 

	}
	
	@GetMapping("/stop-scraping")
	public String stopScraping(Model model) {
		
		connectionService.stopScraper();
		
		return "redirect:/production/index"; 
		
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
		log.info("by by have a good day");
	}
	
//	private void stopThread() {
//		ThreadGroup group = Thread.currentThread().getThreadGroup();
//	    Thread[] threads = new Thread[group.activeCount()];
//	    group.enumerate(threads);
//
//	    for(Thread t : threads) {
//	        if(t.getName().equals("myScraper")) {
//	            /* Thread.stop() is a horrible thing to use. 
//	               Use Thread.interrupt() instead if you have 
//	               any control over the running thread */
//	            t.stop();
//	        }
//	    }
//	}

}
