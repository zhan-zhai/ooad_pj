package ooad.project.service.evaluationDecorator;

import ooad.project.domain.regulatoryTask.History;

/**
 * 装饰角色，用于增加新的评分策略
 */
public class EvaluationStrategy implements Evaluation{
    protected Evaluation evaluation;

    public EvaluationStrategy(Evaluation evaluation){
        super();
        this.evaluation = evaluation;
    }

    @Override
    public History<Integer, String> evaluate(int timeout) {
        return evaluation.evaluate(timeout);
    }
}
