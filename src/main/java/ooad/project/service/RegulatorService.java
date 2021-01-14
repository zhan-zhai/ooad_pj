package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;
import ooad.project.domain.regulatoryTask.ProfessorCheckTask;
import ooad.project.domain.regulatoryTask.RegulatoryTaskFactory;
import ooad.project.domain.regulatoryTask.SelfCheckTask;
import ooad.project.repository.MarketRepository;
import ooad.project.repository.ProductsTypeRepository;
import ooad.project.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service

public class RegulatorService {
    @Autowired
    private MarketRepository marketRepository;
    @Autowired
    private ProductsTypeRepository productsTypeRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    private void initMarketList(List<String> markets, List<Market> marketList){
        for (String marketName: markets) {
            Market market = marketRepository.findMarketByMarketName(marketName);
            marketList.add(market);
        }
    }

    private void initProductsTypeList(List<String> productsTypes, List<ProductsType> productsTypeList){
        for (String productsTypeName:productsTypes) {
            ProductsType productsType = productsTypeRepository.findProductsTypeByProductsTypeName(productsTypeName);
            productsTypeList.add(productsType);
        }
    }


    /**
     * 发布自检任务，指定市场，产品类别，截止时间
     * @param markets
     * @param productsTypes
     * @param deadLine
     * @return
     */
    public SelfCheckTask launchSelfCheckTask(List<String> markets, List<String> productsTypes,Date deadLine){
        List<Market> marketList = new ArrayList<>();
        List<ProductsType> productsTypeList = new ArrayList<>();
        initMarketList(markets,marketList);
        initProductsTypeList(productsTypes,productsTypeList);
        return new RegulatoryTaskFactory().createSelfCheckTask(marketList,productsTypeList,deadLine);
    }

    /**
     * 发布专家任务，指定专家，市场，产品类别，截止时间
     * @param markets
     * @param productsTypes
     * @param professorName
     * @param deadLine
     * @return
     */
    public ProfessorCheckTask launchProfessorCheckTask(List<String> markets, List<String> productsTypes, String professorName, Date deadLine){
        List<Market> marketList = new ArrayList<>();
        List<ProductsType> productsTypeList = new ArrayList<>();
        initMarketList(markets,marketList);
        initProductsTypeList(productsTypes,productsTypeList);
        Professor professor = professorRepository.findProfessorByProfessorName(professorName);
        return new RegulatoryTaskFactory().createProfessorCheckTask(marketList,productsTypeList,professor,deadLine);
    }
}
