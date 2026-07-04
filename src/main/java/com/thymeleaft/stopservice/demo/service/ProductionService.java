package com.thymeleaft.stopservice.demo.service;

import java.util.List;

import com.thymeleaft.stopservice.demo.model.ProductionModel;

public interface ProductionService {

    List<ProductionModel> findAll();

    ProductionModel findById(Long theId);

    List<ProductionModel> save(List<ProductionModel> theEmployee);
    
    void updateProdution(ProductionModel production);

    void deleteById(Long theId) throws Exception;
    
    void checkFieldObj(ProductionModel p);

}
