package com.thymeleaft.stopservice.demo.model;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.thymeleaft.stopservice.demo.util.Tool;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@JsonPropertyOrder({ "id", "link", "titleProdotto", "rating", "priceTarget", "priceNow",
	"seller", "shippedBy", "notificated", "notificatedDate" })
public class ProductionModel {

	private Long id;
	private String link, titleProdotto, rating, priceTarget, priceNow,
			seller, shippedBy, notificatedDate;
	
	private boolean notificated;

	public ProductionModel(Long id, String link, String title, String rating, String priceTarget,
			String priceNow, String seller,
			String shippedBy, String notificatedDate, boolean notificated) {
		Tool tool = new Tool();
		this.link = null == link ? "" : link;
		this.titleProdotto = null == title ? "" : title;
		this.rating = null == rating ? "" : rating;
		this.priceTarget = null == priceTarget ? "999" : priceTarget;
		this.seller = null == seller ? "" : seller;
		this.shippedBy = null == shippedBy ? "" : shippedBy;
		this.notificatedDate = null == notificatedDate ? tool.stringDateYestrday() : notificatedDate;  
		this.id = id;
		this.notificated = notificated;
	}

//	public boolean isConsistentData(ProductionModel model) {
//		return !model.getLastPrice().isEmpty() && !model.getPriceAtTimeZero().isEmpty() && !model.getTitle().isEmpty();
//	}

	public boolean isConsistentData2(ProductionModel model) {
		return !StringUtils.isEmpty(model.getLink()) && !StringUtils.isEmpty(model.getTitleProdotto());
	}

	public boolean isConsistentData3(ProductionModel model) {
		return !StringUtils.isEmpty(model.getLink()) && !StringUtils.isEmpty(model.getTitleProdotto())
				&& !StringUtils.isEmpty(model.getPriceNow())
				&& !StringUtils.isEmpty(model.getSeller()) && !StringUtils.isEmpty(model.getShippedBy());
	}
    
    public void setFieldOnFirstInsert(ProductionModel p) {
    	String test = "da recuperare";
    	
    	if(StringUtils.isEmpty(p.getTitleProdotto())) {
    		p.setTitleProdotto(test);
    		p.setRating("null");
    		p.setPriceNow(test);
    		p.setSeller(test);
    		p.setShippedBy(test);
    		p.setNotificated(false);
    	}
    	
    }
    
}








