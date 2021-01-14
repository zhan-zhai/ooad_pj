package ooad.project.service;

import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.CheckResult;
import ooad.project.domain.regulatoryTask.RegulatoryTask;
import ooad.project.domain.regulatoryTask.SpotCheckTask;
import ooad.project.repository.ProductsTypeRepository;
import ooad.project.service.visitor.GetTotalNonconformingVisitor;
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

    /**
     * 计算所有监管任务的某个产品类型的不合格总数
     * @param regulatoryTasks
     * @param startTime
     * @param endTime
     * @param productsType
     * @return
     */
    public int getTotalNonconforming(List<RegulatoryTask> regulatoryTasks, Date startTime, Date endTime,ProductsType productsType){
        GetTotalNonconformingVisitor getTotalNonconformingVisitor = new GetTotalNonconformingVisitor(productsType,startTime,endTime,0);

        for (RegulatoryTask regulatoryTask: regulatoryTasks) {
//            for (SpotCheckTask spotCheckTask:regulatoryTask.getSpotCheckTasks().values()) {
//                for (CheckResult checkResult:spotCheckTask.getCheckResults()){
//                    if (checkResult.getProductsType() == productsType && betweenTheTime(startTime,endTime,checkResult.getCheckTime())){
//                        getTotalNonconformingVisitor.getTotalNonconforming() += checkResult.getNonconforming();
//                    }
//                }
//            }
            regulatoryTask.accept(getTotalNonconformingVisitor);
        }

        return getTotalNonconformingVisitor.getTotalNonconforming();
    }

//    private boolean betweenTheTime(Date startTime, Date endTime, Date checkTime){
//        return checkTime.after(startTime) && checkTime.before(endTime);
//    }
}
