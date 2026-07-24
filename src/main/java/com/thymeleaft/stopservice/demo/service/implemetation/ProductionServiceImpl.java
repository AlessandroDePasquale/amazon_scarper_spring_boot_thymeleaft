package com.thymeleaft.stopservice.demo.service.implemetation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thymeleaft.stopservice.demo.model.ProductionModel;
import com.thymeleaft.stopservice.demo.repo.RepoJson;
import com.thymeleaft.stopservice.demo.service.ProductionService;
import com.thymeleaft.stopservice.demo.util.Tool;
import com.thymeleaft.stopservice.demo.util.Utility;

@Service
public class ProductionServiceImpl implements ProductionService {

	private Tool tool;
    private RepoJson repoJson;

    @Autowired
    public ProductionServiceImpl(RepoJson repoJson, Tool tool) {
        this.repoJson = repoJson;
        this.tool = tool;
    }

    @Override
    public List<ProductionModel> findAll() {
        try {
			return repoJson.readProductionFromJson(tool.pathFileJson());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    @Override
    public ProductionModel findById(Long theId) {

    	List<ProductionModel> allProdution = findAll();

    	ProductionModel toUpdate = null;

    	if (CollectionUtils.isNotEmpty(allProdution)) {
    		for(ProductionModel p : allProdution) {
    			if(p.getId() == theId) {
    				toUpdate = p;
    			}
    		}

    	} else {
    		// we didn't find the employee
    		throw new RuntimeException("Did not find production id - " + theId);
    	}

    	return toUpdate;
    }

    @Override
    public List<ProductionModel> save(List<ProductionModel> productions) {
        try {
			return repoJson.writeJsonListRecords(productions, tool.pathFileJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }

    @Override
    public void deleteById(Long theId) throws IOException {
        List<ProductionModel> allProduction = findAll();
        
        for(ProductionModel p : allProduction) {
        	if(p.getId() == theId) {
        		allProduction.remove(p);
        		repoJson.deleteJsonListRecords(p, tool.pathFileJson());
        		break;
        		}
        }
        
    }

	@Override
	public void checkFieldObj(ProductionModel production) {
		production.setFieldOnFirstInsert(production);
		production.setLink(Utility.getLinkWithAsin(production.getLink()));

	}
	@Override
	public void updateProdution(ProductionModel production) {
		try {
			repoJson.updateJson(production);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}






