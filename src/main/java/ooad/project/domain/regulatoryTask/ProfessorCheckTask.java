package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 专家抽检任务，为一组专家分配监管任务，每一个专家对应一个农贸市场的抽检任务
 */
public class ProfessorCheckTask extends RegulatoryTask{
    private List<Professor> professors;
    private Map<Professor,SpotCheckTask> spotCheckTaskMap;

//    public Professor getProfessor() {
//        return professor;
//    }
//
//    public void setProfessor(Professor professor) {
//        this.professor = professor;
//    }


    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public Map<Professor, SpotCheckTask> getSpotCheckTaskMap() {
        return spotCheckTaskMap;
    }

    public void setSpotCheckTaskMap(Map<Professor, SpotCheckTask> spotCheckTaskMap) {
        this.spotCheckTaskMap = spotCheckTaskMap;
    }

    public ProfessorCheckTask() {
    }

    public ProfessorCheckTask(List<Market> markets, List<ProductsType> productsTypes, List<Professor> professors, Date deadLine) {
        super(markets, productsTypes,deadLine);
        this.professors = professors;
    }

}
