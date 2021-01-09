package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;

import java.util.Date;
import java.util.List;

/**
 * 专家抽检任务，为一组专家分配监管任务，每一个专家对应一个农贸市场的抽检任务
 */
public class ProfessorCheckTask extends RegulatoryTask{
    private Professor professor;

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ProfessorCheckTask() {
    }

    public ProfessorCheckTask(List<Market> markets, List<ProductsType> productsTypes, Professor professor, Date deadLine) {
        super(markets, productsTypes,deadLine);
        this.professor = professor;
    }

}
