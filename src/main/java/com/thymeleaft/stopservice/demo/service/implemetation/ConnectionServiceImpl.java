package com.thymeleaft.stopservice.demo.service.implemetation;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import com.thymeleaft.stopservice.demo.model.ProductionModel;
import com.thymeleaft.stopservice.demo.service.ConnectionService;
import com.thymeleaft.stopservice.demo.service.ProductionService;
import com.thymeleaft.stopservice.demo.util.Tool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
	
	private List<ProductionModel> production;
	private Boolean serviceRunning = true;
	
	@Autowired
	private Tool tool;
	
	@Autowired
	private ProductionService productionService;

	
	@Override
	public void startExecution() {

		log.info("ciao stopScraper is : " + serviceRunning);

		serviceRunning = true;

		production = productionService.findAll();

		while(serviceRunning) {
			try {
				
				scaperStart(production);

			} catch (Exception e) {
				serviceRunning = false;
				Thread.currentThread().interrupt();
				log.error("error in startExecution : {}", e);
				break;
			}
		}
	}
	

	@Override
	public void scaperStart(List<ProductionModel> production) throws Exception{

		for(ProductionModel p : production) {
			if(!serviceRunning) {
				break;
			}
			if(tool.unlockItem(p.getNotificatedDate())) {
				scraperBody(p);
			} else {
				log.info("production id : {}, skipped notificated today", p.getId());
			}
		}
		// on finish one cilce reload file json for eventuale update
		production = productionService.findAll();
	}

	public boolean scraperBody(ProductionModel production) throws Exception {

		StopWatch timeExecute = new StopWatch();

		boolean error = false;

		String productTitle, priceString, ratingString, sellerName, expedition = "";

			try {

				timeExecute.start();

				// Fetch the HTML content of the Amazon product page
				Document document = Jsoup.connect(production.getLink()).ignoreContentType(true).get();

				// Extract the product title
				productTitle = document.getElementsByClass("a-size-large product-title-word-break").text();

				if(!StringUtils.isEmpty(productTitle)) {

					production.setTitleProdotto(productTitle.length()> 65 ? productTitle.substring(0, 65) : productTitle);//update 24/07/2026);

					log.info("Titolo prodotto: " + productTitle);

					// Extract the current price
					priceString = document.getElementsByClass("aok-offscreen").text();
					
					priceString = priceString.replace("&nbsp;", "");//update 20/04/2026
					
					
					if(!StringUtils.isEmpty(priceString)) {

						double priceNow = parsePrice(priceString);

						production.setPriceNow(String.valueOf(priceNow) + "€");

						log.info("Prezzo corrente: " + priceNow + "€");

						// Extract the product rating 
						ratingString = document.getElementsByClass("a-icon-alt").first().text();
						// double rating = parseRating(ratingString);
						production.setRating(ratingString);
//						production.setRating(ratingString + "/5");

						log.info("Valutazione: " + ratingString.substring(0, 3) + "/5");

						// Extract the seller information
						sellerName = document.getElementsByClass("a-size-small offer-display-feature-text-message").get(0).text();
						expedition = document.getElementsByClass("a-size-small offer-display-feature-text-message").get(1).text();

						production.setSeller(sellerName);
						production.setShippedBy(expedition);
						
						timeExecute.stop();

						log.info("second to retieve all data from the page : " + (Double) (timeExecute.getLastTaskTimeMillis() / 1000.0)); // / 1000.0 (insert comma to devide to double and then will be cast it in double) to get secod 
						production.setTimeToRetrive(String.valueOf(timeExecute.getLastTaskTimeMillis() / 1000.0) + " secondi");

						// Print the scraped data

						log.info("Venditore: " + sellerName);
						log.info("Spedizione: " + expedition);

						if(priceNow <= Double.parseDouble(production.getPriceTarget())) {
							log.info("Try send noitification");
							production.setNotificatedDate(tool.getDateString());
							production.setNotificated(true);
							tool.openBrowser(production.getLink());
						}
						
						// update production
						productionService.updateProdution(production);
						
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				log.error("Errore durante lo scraping: " + e.getMessage());
				error = true;
				throw new Exception("Exception into scaper : " + e.getMessage());
			}
			
			if(!serviceRunning) {
				log.warn("interppunt scarping");
			}

		return error;
	}
	
	private double parsePrice(String priceString) {
		
		String tempPrezzo = priceString;
		
		Double centesimi = 0.00;
		
		Double result = 0.00;
		
		// Remove non-numeric characters and convert to double
		tempPrezzo = tempPrezzo.substring(0 , tempPrezzo.indexOf("€") -1);
		tempPrezzo = tempPrezzo.substring(0, tempPrezzo.length() - 3); // remove comma and two cifre afre comma 
		String test = priceString.substring(priceString.indexOf(",") , priceString.indexOf(",") + 3);
		centesimi =  Double.valueOf(("0" + test).replace(",", "."));// remove eventuale point of one thousen price
//		priceString.replace(",", "."); // replace comma with point
		tempPrezzo.replaceAll("[^\\d.]", "");
		result = Double.parseDouble(tempPrezzo) + centesimi;
		return result;
	}

	@Override
	public void stopScraper() {
		
		log.info("try to stop service");
		
		this.serviceRunning = false;
	}
	
//	public static void main(String[] args) throws Exception {
//		Production p = new Production();
//		p.setLink(StringCostants.baseUrlAmazon + "B09B8X9RGM");
//		p.setTitleProdotto("test");
//		p.setPriceNow(22.2);
//		p.setSeller("non lo so");
//		scraperBody(p);
//	}

}
