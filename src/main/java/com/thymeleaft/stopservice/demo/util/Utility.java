package com.thymeleaft.stopservice.demo.util;

import org.springframework.stereotype.Component;

import com.thymeleaft.stopservice.demo.costants.StringCostants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Utility {
	
	public static String getLinkWithAsin(String link) {
		log.info("start getLinkWithAsin");
		
		String searchAsin = link;
		
		searchAsin = searchAsin.substring(searchAsin.indexOf("/dp/"));
		
		searchAsin = searchAsin.replace("/dp/", "");
		
		searchAsin = searchAsin.contains("/") ? searchAsin.substring(0, searchAsin.indexOf("/")) : searchAsin;
		
		link = StringCostants.baseUrlAmazon + searchAsin;
		
		log.info("end getLinkWithAsin , link extraxt : {}", link);
		
		return link;
	}

}
