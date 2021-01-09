package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;

import java.util.Date;
import java.util.List;

/**
 * 任务工厂，用来创建两种不用的任务
 */
public class RegulatoryTaskFactory {
    public ProfessorCheckTask createProfessorCheckTask(List<Market> markets, List<ProductsType> productsTypes, Professor professor, Date deadLine){
        return new ProfessorCheckTask(markets,productsTypes,professor,deadLine);
    }

    public SelfCheckTask createSelfCheckTask(List<Market> markets, List<ProductsType> productsTypes, Date deadLine){
        return new SelfCheckTask(markets,productsTypes,deadLine);
    }
}
