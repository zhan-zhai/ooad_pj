package ooad.project.service.evaluationDecorator;

import ooad.project.domain.regulatoryTask.History;

/**
 * 实现Evaluation构件
 */
public class SpecificEvaluation implements Evaluation{

    @Override
    public History<Integer,String> evaluate(int timeout) {
        return new History<>(0,"");
    }
}
