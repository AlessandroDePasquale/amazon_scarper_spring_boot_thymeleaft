package com.thymeleaft.stopservice.demo.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.thymeleaft.stopservice.demo.model.ProductionModel;
import com.thymeleaft.stopservice.demo.model.enumeration.EAsin;
import com.thymeleaft.stopservice.demo.repo.RepoJson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Tool {

	@Lazy
	@Autowired
	private RepoJson repoJson;

	public void print(String msg, boolean error) {
		if (!error)
			log.info(msg);
		else
			System.err.println(msg);
	}

	public ArrayList<ProductionModel> arrayToList(ProductionModel[] arrToList) {
		ArrayList<ProductionModel> array_list = new ArrayList<ProductionModel>();
		// Using Collections.addAll() method
		Collections.addAll(array_list, arrToList);
		return array_list;
	}

	private boolean isURLValid(String url) {

		log.info("is url valid");

		if (url.contains("amazon") || url.contains("amzn.to")) {
			log.info("if is url valid");
			UrlValidator urlValidator = new UrlValidator();
			return urlValidator.isValid(url);
		} else {
			return false;
		}
	}
	
	public ArrayList<ProductionModel> removeDuplicates(ArrayList<ProductionModel> list) {
		log.info("start remuove dupplicates in list txt");
		log.info("count before : " + list.size());
	    Set<String> set = new HashSet<>();
	    ArrayList<ProductionModel> newList = new ArrayList<>();
	    for (ProductionModel model : list) {
	        if (set.add(model.getLink())) {
	            newList.add(model);
	        }
	    }
	    log.info("count after : " + newList.size());
	    return newList;
	}


//	public void prprprrr() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
//		File audioFile = new File("C:\\test\\sound\\Windows Message Nudge.wav");
//		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
//		Clip clip = AudioSystem.getClip();
//		clip.open(audioStream);
//		clip.start();
//
//	}

//	public int setPriceDropByAndTypeOffer(ProductionModel model, boolean setPriceDropBy) { // work
//
//		log.info("start setPriceDropByAndTypeOffer percentage : " + model.getPriceDropBy());
//
//		double result = 0;
//
//		if (setPriceDropBy) {
//			result = ((double) (Integer.parseInt(model.getPriceAtTimeZero()) - Integer.parseInt(model.getLastPrice()))
//					/ Integer.parseInt(model.getPriceAtTimeZero())) * 100;
//
//			model.setPriceDropBy(((int) result) + "%");
//		} else {
//			result = Double.parseDouble(model.getPriceDropBy().replace("%", ""));
//		}
//
//		log.info("result : " + result);
//
////		model.setTypeOffer("empty");
//
//		if (result >= 70 && model.getTypeOffer() == null) {
//			model.setTypeOffer("errore");
//		}
//
//		if (result >= 60 && model.getTypeOffer() == null) {
//			model.setTypeOffer("affarissimo");
//		}
//
//		if (result >= 50 && model.getTypeOffer() == null) {
//			model.setTypeOffer("affare");
//		}
//
//		if (result >= 40 && model.getTypeOffer() == null) {
//			model.setTypeOffer("offertaImperdibile");
//		}
//
//		if (result >= 30 && model.getTypeOffer() == null) {
//			model.setTypeOffer("offerta");
//		}
//
//		if (result >= 20 && model.getTypeOffer() == null) {
//			model.setTypeOffer("scontoImperdibile");
//		}
//
//		if (result >= 5 && model.getTypeOffer() == null) {
//			model.setTypeOffer("sconto");
//		}
//
//		log.info("End : typeOffer --> " + model.getTypeOffer());
//
//		return (int) result;
//
//	}

	public void checkFiledEmpty(ProductionModel model) throws IllegalArgumentException, IllegalAccessException {

		String fieldName = "";

		Field[] listField = model.getClass().getFields();

		for (Field field : listField) {
			fieldName = field.getName();
			Object fieldValue = field.get(model);
			if (fieldValue == null) {
				log.info("Il campo " + fieldName + " è vuoto");
			} else {
				log.info("Il campo " + fieldName + " ha il valore " + fieldValue);
			}
		}

	}

	public String formatPrice(String price) {

		int indexExtract = 0;

		if (price.contains(",")) {
			log.info("lastPrice conteins comma");
			indexExtract = price.indexOf(",");
			price = price.substring(0, indexExtract) + price.substring(indexExtract, indexExtract + 3 );
		}
		if (price.contains(".")) {
			log.info("lastPrice conteins dot");
			price = price.replace(".", "");
		}
		return price;
	}

	public String formatCoupon(String coupon) {

		int temp = 0;

		if (coupon.contains("€"))
			temp = coupon.indexOf("€") + 1;
		else if (coupon.contains("%"))
			temp = coupon.indexOf("%") + 1;

		return coupon.substring(0, temp);

	}

	public ArrayList<String> getListString(List<ProductionModel> listModel) {

		ArrayList<String> listLink = new ArrayList<String>();

		for (ProductionModel model : listModel) {
			listLink.add(model.getLink());
		}
		return listLink;
	}

	public String randomString(ArrayList<String> listString) {
		Random random = new Random();
		int index = random.nextInt(listString.size());
		return listString.get(index);
	}

	public  int randomInt(int max) {
		Random random = new Random();
		return random.nextInt(max);
	}

	public String getDateString() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}

	public Date getDate() {
		return new Date();
	}

	public Date stringToDate(String dateString) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = formatter.parse(dateString);
			log.info(date.toString());
		} catch (Exception e) {
			log.error("Errore durante la conversione della data");
		}
		return date;
	}
	
	public boolean compareStringDate(String dateNotificated, String dateToDay) {
		if(null != dateNotificated && !StringUtils.isEmpty(dateNotificated) && dateNotificated.contains("/")) {
			Date date0 = stringToDate(dateNotificated);
			Date date1 = stringToDate(dateToDay);
			return date0.before(date1);
		} else {
			return false;
		}
		
	}
	
	public String stringDateYestrday() {
		// Ottenere la data odierna
        Date today = new Date();
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        // Creare un oggetto Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // Sottrarre un giorno alla data
        calendar.add(Calendar.DATE, -1);

        // Ottenere la data del giorno precedente
        Date yesterday = calendar.getTime();
        
        return formatter.format(yesterday);
        
	}

	public boolean unlockItem(String dateFromJson) {
		log.info("test date : " + dateFromJson);

		String dateNow = getDateString();

		if(!StringUtils.isEmpty(dateFromJson)) {
			if (!dateFromJson.isEmpty()) {
				if (dateNow.equals(dateFromJson)) {
					log.info("La data è uguale a quella odierna");
					return false;
				} else {
					log.info("la data è diversa da quella odierna");
					return true;
				}
			} else {
				return false;
			}
		} else {
			log.info("la data è diversa da quella odierna");
			return true;
		}
	}

//	public boolean checkBeforeSeendMessage(ProductionModel model) {
//		return !model.getTitle().isEmpty() && !model.getLastPrice().isEmpty() && !model.getLink().isEmpty()
//				&& !model.getPriceAtTimeZero().isEmpty() && model.getRating().isEmpty();
//	}


//	public String extractImgSrc(String url) throws IOException {
//		Document doc = Jsoup.connect(url).get(); // il documento HTML della pagina
//
//		// Usa Jsoup per estrarre il primo link di un'immagine dalla pagina
//		Element image = doc.select("img.mimg").first(); // seleziona il primo elemento img con la classe mimg
//		String imageUrl = image.attr("src"); // ottiene l'attributo src dell'elemento
//		log.info("imgUrl : " + imageUrl);
//		return imageUrl;
//	}
	
	public String buildLinkCammel(String urlAmazonComplete){// bard google bad not work with small link (link end with only asin)
		
		String asin = "";
		String[] sliptString = null;
		int indexStart = 0, indexEnd = 0;
		
		String baseUrlCammel = "https://it.camelcamelcamel.com/product/";

		if(urlAmazonComplete.contains(EAsin.AsinCharMatch.getValue())) {
			
			sliptString = urlAmazonComplete.split(EAsin.AsinCharMatch.getValue());
			
			indexStart = urlAmazonComplete.indexOf(EAsin.AsinCharMatch.getValue()) + EAsin.AsinCharMatch.getValue().length(); // add lenght of string match into link

			indexEnd = sliptString[1].indexOf("/");
			
			asin = urlAmazonComplete.substring(indexStart, indexStart + indexEnd); // sum start and end becasue logic before split string link into array
			
			return baseUrlCammel + asin;
			
		} else {
            return EAsin.AsinNotFound404.getValue();
        }
	}
	
	public void openBrowser(String urlToOpen) {
		
		log.info("start openBrowser");
		
		String os = System.getProperty("os.name").toLowerCase();
		
		  try {
	        	 if (os.contains("win")) {
	                 log.info("Sistema operativo: Windows");
	                 // Sistema operativo Windows
	                 Runtime.getRuntime().exec("cmd /c start " + urlToOpen);
	             } else if (os.contains("nix") || os.contains("nux")) {
	                 // Sistema operativo basato su Unix (Linux)
	                 log.info("Sistema operativo: Unix (Linux)");
	                 Runtime.getRuntime().exec("xdg-open " + urlToOpen);
	             } else if (os.contains("mac")) {
	                 // Sistema operativo macOS
	                 log.info("Sistema operativo: MacOS");
	                 Runtime.getRuntime().exec("open " + urlToOpen);
	             } else {
	                 // Altro sistema operativo non supportato
	                 log.error("Sistema operativo non supportato");
	             }
	        	 
	        	 // codice fatto da chat gpt ma utilissimo aprire il browser su mac e windows sulla pagina localhost dove questo
	        	 // applicativo si appogia 
	        } catch (IOException e) {
	        	log.error("error openBrowser", e);
	            e.printStackTrace();
	        }
		  
		  log.info("end openBrowser");
		  
	}
	
	public File pathFileJson() {
		
		log.info("start create or recuvera file json storgae");
		
	     // Ottieni la directory di lavoro corrente (dove si trova il JAR)
        String currentWorkingDirectory = getDirExecuteJar(); // get current path on run jar on windows or mac

		File pathFileJson = new File(currentWorkingDirectory + File.separator + "repoProdution.json"); // for craeet seperator for windows or mac 

		try {

			if (!pathFileJson.exists()) {
				pathFileJson.createNewFile();
				try {
					ProductionModel production = new ProductionModel((long) 0, "https://www.amazon.it/dp/B0BTFWFRWN", "0", "0", "0", "0", "0", "0", "da recuperare", stringDateYestrday(), false); // default record of fire stick
					
					repoJson.writeJsonListRecords(new ArrayList<ProductionModel>(Arrays.asList(production)), pathFileJson);
				} catch (Exception e) {
					log.error("error on craete file and insert accound Admin");
					e.printStackTrace();
				}
			} else {
				log.info("file already exists on : " + currentWorkingDirectory);
			}

		} catch (IOException e) {
			log.error("error in pathFileJson , error : {}", e);
			e.printStackTrace();
		}
		
		log.info("end create or recuvera file json storage");
		
		return pathFileJson;
	}
	
	public String getDirExecuteJar() {
		return System.getProperty("user.dir");
	}

}
