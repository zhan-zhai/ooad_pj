package ooad.project.service;

import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.CheckResult;
import ooad.project.domain.regulatoryTask.RegulatoryTask;
import ooad.project.domain.regulatoryTask.SpotCheckTask;
import ooad.project.repository.ProductsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service

public class ProductsTypeService {
    @Autowired
    private ProductsTypeRepository productsTypeRepository;

    public ProductsType searchProductsType(String productsTypeName){
        return productsTypeRepository.findProductsTypeByProductsTypeName(productsTypeName);
    }

    public void addProductsType(String productsTypeName){
        ProductsType productsType = new ProductsType(productsTypeName);
        productsTypeRepository.save(productsType);
    }

    public int getTotalNonconforming(List<RegulatoryTask> regulatoryTasks, Date startTime, Date endTime,ProductsType productsType){
        int totalNonconforming = 0;

        for (RegulatoryTask regulatoryTask: regulatoryTasks) {
            for (SpotCheckTask spotCheckTask:regulatoryTask.getSpotCheckTasks().values()) {
                for (CheckResult checkResult:spotCheckTask.getCheckResults()){
                    if (checkResult.getProductsType() == productsType && betweenTheTime(startTime,endTime,checkResult.getCheckTime())){
                        totalNonconforming += checkResult.getNonconforming();
                    }
                }
            }
        }

        return totalNonconforming;
    }

    private boolean betweenTheTime(Date startTime, Date endTime, Date checkTime){
        return checkTime.after(startTime) && checkTime.before(endTime);
    }
}
