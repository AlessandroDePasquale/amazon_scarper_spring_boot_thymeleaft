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
			elementoDaAggiornare.addProperty("timeToRetrive", model.getTimeToRetrive());
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
		BufferedReader br = null;

		if (file.exists()) {
			try {
				System.out.println("file exist");
				br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				}else{
					oldProductionList = readProductionFromJson(file);
					sizeOldList = oldProductionList.size();
					
					listLinkOld = tool.getListString(oldProductionList);
					listLinkNew = tool.getListString(newProductionList);
					
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
			} finally {
				if(null != br) {
					br.close();
				}
			}
		}
		
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
				newObject.addProperty("timeToRetrive", oldProductionList.get(i).getTimeToRetrive());
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
			newObject.addProperty("timeToRetrive", newProductionList.get(i).getTimeToRetrive());
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

		System.out.println("ProductionModel in arrivo : " + toDelte.toString());
		
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
				newObject.addProperty("timeToRetrive", oldProductionList.get(i).getTimeToRetrive());
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
		
		ProductionModel[] arrProduct = null;
		ArrayList<ProductionModel> listProduction = new ArrayList<ProductionModel>();
		JsonReader reader = null;
		BufferedReader br = null;
		FileReader fileReader = new FileReader(pathFile);

		try {

			br = new BufferedReader(fileReader);
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
		} finally { //chiusura del file json
			try {
				if(null != reader) {
					reader.close();
				}
				if(null != br) {
					br.close();
				}
				if(null != fileReader) {
					br.close();
				}
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
		}
		return listProduction;
	}

}
