package com.thymeleaft.stopservice.demo.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.thymeleaft.stopservice.demo.model.ProductionModel;
import com.thymeleaft.stopservice.demo.util.Tool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RepoJson {

	@Autowired
	private Tool tool;

/*	public static void writeFristRecord(File pathToSaveJson) throws IOException {

		JsonArray jsonArray = new JsonArray();

		try (FileWriter file = new FileWriter(pathToSaveJson, true)) {
			JsonObject newObject = new JsonObject();
			newObject.addProperty("ID", 1);
			newObject.addProperty("link", "https://www.amazon.it/echo-dot-2022/dp/B09B8RF4PY/ref=sr_1_1_ffob_sspa?__mk_it_IT=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=3GSYMG9IYX8MS&keywords=eco&qid=1686584835&sprefix=ec%2Caps%2C172&sr=8-1-spons&sp_csd=d2lkZ2V0TmFtZT1zcF9hdGY&psc=1)");
			newObject.addProperty("title", "");
			newObject.addProperty("rating", "");
			newObject.addProperty("targetPrice", "");
			newObject.addProperty("lastPrice", "");

			jsonArray.add(newObject);

			try (FileWriter fileWriter = new FileWriter(pathToSaveJson)) {
				System.out.println("tentativo di scrivere il json");
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				gson.toJson(jsonArray, fileWriter);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Scrittura completata");
		
	}*/
	
//	public void testUpdate(ProductionModel model) throws IOException {
//		// Leggi il contenuto del file JSON in una stringa
//		String jsonString = new String(Files.readAllBytes(Paths.get(Costants.path)));
//
//		// Analizza la stringa JSON in un oggetto Java
//		Gson gson = new Gson();
//		Type type = new TypeToken<List<ProductionModel>>(){}.getType();
//		List<ProductionModel> productionModelList = gson.fromJson(jsonString, type);
//
//		// Modifica l'oggetto Java come desiderato
//		for (ProductionModel myObject : productionModelList) {
//		    if (myObject.getId() == model.getId()) {
//		        myObject.setLastPrice(model.getLastPrice());
//		        tool.createMsgForTelegram(myObject);
//		        myObject.setNotificated(true);
//		    }
//		}
//
//		// Converti l'oggetto Java modificato in una stringa JSON
//		String updatedJsonString = gson.toJson(productionModelList);
//
//		// Scrivi la stringa JSON nel file
//		Files.write(Paths.get(Costants.path), updatedJsonString.getBytes());
//
//	}

	public void updateJson(ProductionModel model) throws Exception{ // maybe it works

		System.out.println("update json");
		
		FileWriter file = null;
		
		// Leggi il file JSON e convertilo in un oggetto JSON
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = null;
		try {
			jsonElement = parser.parse(new FileReader(tool.pathFileJson()));
		} catch (JsonIOException e1) {
			log.error("error updateJson");
			e1.printStackTrace();
		} catch (JsonSyntaxException e1) {
			log.error("error updateJson");
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			log.error("error updateJson");
			e1.printStackTrace();
			throw new Exception(e1.getMessage());
		}

		// Recupera l'elemento specifico che desideri aggiornare dall'oggetto JSON
		JsonArray jsonArray = jsonElement.getAsJsonArray();

		System.out.println(jsonArray.get(0).getAsJsonObject());
		
		System.out.println("model id : " + model.getId());
		
		JsonObject elementoDaAggiornare = jsonArray.get((int) (model.getId()-1)).getAsJsonObject();

//		if(!titleRating) {
			// Aggiorna il valore dell'elemento specifico ad ogni chaimata
			elementoDaAggiornare.addProperty("rating", model.getRating());
//			elementoDaAggiornare.addProperty("coupon", model.getCoupon());

			elementoDaAggiornare.addProperty("priceNow", model.getPriceNow());
			elementoDaAggiornare.addProperty("priceTarget", model.getPriceTarget());
			elementoDaAggiornare.addProperty("shippedBy", model.getShippedBy());
			elementoDaAggiornare.addProperty("seller", model.getSeller());
			
//			if(model.isNotificated()) {
//				model.setDateNotificated(tool.getDateString());
//				elementoDaAggiornare.addProperty("dateNotificated", model.getDateNotificated());
//				elementoDaAggiornare.addProperty("priceDropBy", model.getPriceDropBy());
//			}
			
//		}else{
			//inserisco solo la prima volta gli elementi statici che non cambiano nel tempo
			elementoDaAggiornare.addProperty("titleProdotto", model.getTitleProdotto());
			elementoDaAggiornare.addProperty("notificatedDate", model.getNotificatedDate());
			
	
//		}

		// Scrivi l'oggetto JSON aggiornato nel file
		try {
			file = new FileWriter(tool.pathFileJson());
			file.write(jsonArray.toString());
			file.close();
			System.out.println("fine tentativo di update : " + model.toString());
		}catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<ProductionModel> writeJsonListRecords(List<ProductionModel> newProductionList, File file) throws IOException {

		System.out.println("List in arrivo : " + newProductionList.toString());
		
		JsonArray jsonArray = new JsonArray();
		ArrayList<ProductionModel> oldProductionList = new ArrayList<ProductionModel>();
		ArrayList<String> listLinkOld = new ArrayList<String>();
		ArrayList<String> listLinkNew = new ArrayList<String>();
		int sizeOldList = 0;
		int sizeNewList = newProductionList.size();
		int cicleForDuplicate = 0;

		FileReader fileReader = new FileReader(file);
		
		JsonObject newObject = null;
		JsonObject objTrackingPrice = null; // obj json for new model

		if (file.exists()) {
			try {
				System.out.println("file exist");
				BufferedReader br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				}else{
					oldProductionList = readProductionFromJson(file);
					sizeOldList = oldProductionList.size();
					
					listLinkOld = tool.getListString(oldProductionList);
					listLinkNew = tool.getListString(newProductionList);
					/*ObjectMapper mapper = new ObjectMapper();
					JsonNode node = mapper.readTree(file);
					sizeOldList = node.size();
					System.out.println("last id : " + sizeOldList);*/
					
					for(String link : listLinkOld) {
						if(listLinkNew.contains(link)) {
//							listLinkNew.remove(link);
							for(int i = 0; i < newProductionList.size(); i++) {
								if(newProductionList.get(i).getLink().equalsIgnoreCase(link)) {
									newProductionList.remove(i);
								}
							}
						}
					}
				}
			}catch(Exception e){
				System.err.println("error on writeJsonListRecords");
				e.printStackTrace();
			}
		}
		
//		for(int i = 0; i < newProductionList.size(); i++) {
//			if(!newProductionList.get(0).isConsistentData3(newProductionList.get(i))) {
//				System.out.println("Remove link : " + newProductionList.get(i).getLink());
//				newProductionList.remove(i);
//			}
//		}
		
		sizeNewList = newProductionList.size();

		if(oldProductionList.size() != 0){
			System.out.println("Start for old list write");
			for(int i = 0; i < sizeOldList; i++){// add old link before write file complete
				newObject = new JsonObject();
				newObject.addProperty("ID", oldProductionList.get(i).getId());
				newObject.addProperty("link", oldProductionList.get(i).getLink());
				newObject.addProperty("titleProdotto", oldProductionList.get(i).getTitleProdotto());
				newObject.addProperty("rating", oldProductionList.get(i).getRating());
				newObject.addProperty("priceTarget", oldProductionList.get(i).getPriceTarget());
				newObject.addProperty("priceNow", oldProductionList.get(i).getPriceNow());
//				newObject.addProperty("priceDropBy", oldProductionList.get(i).getPriceDropBy());
//				newObject.addProperty("coupon", oldProductionList.get(i).getCoupon());
				newObject.addProperty("seller", oldProductionList.get(i).getSeller());
				newObject.addProperty("shippedBy", oldProductionList.get(i).getShippedBy());
				newObject.addProperty("notificatedDate", oldProductionList.get(i).getNotificatedDate());
//				newObject.addProperty("dateNotificated", oldProductionList.get(i).getDateNotificated());
				
//				objTrackingPrice = new JsonObject();
//				
//				objTrackingPrice.addProperty("link", oldProductionList.get(i).getTrackingPrice().getLink());// set paramiter for new obj json
//				objTrackingPrice.addProperty("priceAverage", oldProductionList.get(i).getTrackingPrice().getPriceAvg());
//				objTrackingPrice.addProperty("priceLowest", oldProductionList.get(i).getTrackingPrice().getPriceLowest());
//				objTrackingPrice.addProperty("datePriceLow", oldProductionList.get(i).getTrackingPrice().getDatePriceLow());
//				
//				newObject.add("TrackingPrice", objTrackingPrice);// add new obj json into main obj json
				
				jsonArray.add(newObject);// add obj main json into json array
			}

		}
		
		for (int i = 0; i < sizeNewList; i++) {
			
			System.out.println("Start for new list write");
			
			newObject = new JsonObject();
			newProductionList.get(i).setId((long) (sizeOldList + i +1));
			newObject.addProperty("ID", newProductionList.get(i).getId());
			newObject.addProperty("link", newProductionList.get(i).getLink());
			newObject.addProperty("titleProdotto", newProductionList.get(i).getTitleProdotto());
			newObject.addProperty("rating", newProductionList.get(i).getRating());
//			newObject.addProperty("priceAtTimeZero", newProductionList.get(i).getPriceAtTimeZero());
			newObject.addProperty("priceTarget", newProductionList.get(i).getPriceTarget());
			newObject.addProperty("priceNow", newProductionList.get(i).getPriceNow());
//			newObject.addProperty("priceDropBy", newProductionList.get(i).getPriceDropBy());
//			newObject.addProperty("coupon", newProductionList.get(i).getCoupon());
			newObject.addProperty("seller", newProductionList.get(i).getSeller());
			newObject.addProperty("shippedBy", newProductionList.get(i).getShippedBy());
			newObject.addProperty("notificatedDate", newProductionList.get(i).getNotificatedDate());
//			newObject.addProperty("dateNotificated", newProductionList.get(i).getDateNotificated());
			
//			objTrackingPrice = new JsonObject();
//			
//			objTrackingPrice.addProperty("link", newProductionList.get(i).getTrackingPrice().getLink());
//			objTrackingPrice.addProperty("priceAverage", newProductionList.get(i).getTrackingPrice().getPriceAvg());
//			objTrackingPrice.addProperty("priceLowest", newProductionList.get(i).getTrackingPrice().getPriceLowest());
//			objTrackingPrice.addProperty("datePriceLow", newProductionList.get(i).getTrackingPrice().getDatePriceLow());
//			
//			newObject.add("TrackingPrice", objTrackingPrice);

			jsonArray.add(newObject);
		}

		try (FileWriter fileWriter = new FileWriter(file)) {
			System.out.println("tentativo di scrittura del file json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonArray, fileWriter);
		}

		return newProductionList;

	}
	
	public void deleteJsonListRecords(ProductionModel toDelte, File file) throws IOException {

		System.out.println("List in arrivo : " + toDelte.toString());
		
		JsonArray jsonArray = new JsonArray();
		ArrayList<ProductionModel> oldProductionList = new ArrayList<ProductionModel>();
		ArrayList<String> listLinkOld = new ArrayList<String>();
		ArrayList<String> listLinkNew = new ArrayList<String>();
		int sizeOldList = 0;
		int cicleForDuplicate = 0;

		FileReader fileReader = new FileReader(file);
		
		JsonObject newObject = null;
		JsonObject objTrackingPrice = null; // obj json for new model

		if (file.exists()) {
			try {
				System.out.println("file exist");
				BufferedReader br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				}else{
					oldProductionList = readProductionFromJson(file);
					
				}
			}catch(Exception e){
				System.err.println("error on writeJsonListRecords");
				e.printStackTrace();
			}
		}
		
		for(ProductionModel model : oldProductionList) {
			if(toDelte.getId() == model.getId()) {
				oldProductionList.remove(model);
				break;
			}
		}
		
		sizeOldList = oldProductionList.size();
		
		if(oldProductionList.size() != 0){
			System.out.println("Start for old list write");
			for(int i = 0; i < sizeOldList; i++){// add old link before write file complete
				newObject = new JsonObject();
				newObject.addProperty("ID", oldProductionList.get(i).getId());
				newObject.addProperty("link", oldProductionList.get(i).getLink());
				newObject.addProperty("titleProdotto", oldProductionList.get(i).getTitleProdotto());
				newObject.addProperty("rating", oldProductionList.get(i).getRating());
				newObject.addProperty("priceTarget", oldProductionList.get(i).getPriceTarget());
				newObject.addProperty("priceNow", oldProductionList.get(i).getPriceNow());
//				newObject.addProperty("priceDropBy", oldProductionList.get(i).getPriceDropBy());
//				newObject.addProperty("coupon", oldProductionList.get(i).getCoupon());
				newObject.addProperty("seller", oldProductionList.get(i).getSeller());
				newObject.addProperty("shippedBy", oldProductionList.get(i).getShippedBy());
				newObject.addProperty("notificatedDate", oldProductionList.get(i).getNotificatedDate());
//				newObject.addProperty("dateNotificated", oldProductionList.get(i).getDateNotificated());
				
//				objTrackingPrice = new JsonObject();
//				
//				objTrackingPrice.addProperty("link", oldProductionList.get(i).getTrackingPrice().getLink());// set paramiter for new obj json
//				objTrackingPrice.addProperty("priceAverage", oldProductionList.get(i).getTrackingPrice().getPriceAvg());
//				objTrackingPrice.addProperty("priceLowest", oldProductionList.get(i).getTrackingPrice().getPriceLowest());
//				objTrackingPrice.addProperty("datePriceLow", oldProductionList.get(i).getTrackingPrice().getDatePriceLow());
//				
//				newObject.add("TrackingPrice", objTrackingPrice);// add new obj json into main obj json
				
				jsonArray.add(newObject);// add obj main json into json array
			}

		}

		try (FileWriter fileWriter = new FileWriter(file)) {
			System.out.println("tentativo di scrittura del file json");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonArray, fileWriter);
		}

		log.info("End deleteJsonListRecords");
		
	}

	public ArrayList<ProductionModel> readProductionFromJson(File pathFile) throws FileNotFoundException {
		
//		System.err.println("hello , readProductionFromJson");
		
		ProductionModel[] arrProduct = null;
		ArrayList<ProductionModel> listProduction = new ArrayList<ProductionModel>();
		JsonReader reader = null;

		FileReader fileReader = new FileReader(pathFile);

		try {

			BufferedReader br = new BufferedReader(fileReader);
			if (br.readLine() == null) {
				System.out.println("error file not have data");
			}else {
				reader = new JsonReader(new FileReader(pathFile));
				arrProduct = new Gson().fromJson(reader, ProductionModel[].class);
				
			}

//			System.out.println(arrProduct[0] + ", " + arrProduct[1]);
			if(null != arrProduct) {
				listProduction = tool.arrayToList(arrProduct);
			}

			for(int i = 0; i < listProduction.size(); i++){
				listProduction.get(i).setId( (long) (i + 1) );
				System.out.println("id product : " + listProduction.get(i).getId());
				System.out.println("obj reader : " + listProduction.get(i).toString());
			}

			return listProduction;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
		}
		return listProduction;
	}


	
//	public static void main(String[] args) {
//		File fileJson = new File(Costants.path);
//
//		ArrayList<ProductionModel> list = new ArrayList<ProductionModel>();
//
//		ProductionModel model = new ProductionModel(1, "ciao link", "prezzo desiderato AA ?");
//		ProductionModel model2 = new ProductionModel(2, "link ciao", "null");
//
//		list.add(model);
//		list.add(model2);
//
//		// writeJsonListRecords(list, new File("C:\\test\\test.json"));
//
//		try {
//			writeJsonListRecords(list, fileJson);
//		} catch (IOException e) {
//			log.error("");
//			e.printStackTrace();
//		}
//
////	  Thread.sleep(2000);
////	  
////	  readProdctFromJson(new File("C:\\test\\test.json"));
//	}

}
