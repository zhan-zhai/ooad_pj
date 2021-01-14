package ooad.project.service.evaluationDecorator;

import ooad.project.domain.regulatoryTask.History;

/**
 * 未完成的评分策略
 */
public class UnfinishedEvaluation extends EvaluationStrategy{
    public UnfinishedEvaluation(Evaluation evaluation) {
        super(evaluation);
    }

    public History<Integer,String> unfinishedEvaluate(){
        return new History<>(-10,"未完成-10");
    }

    /** @noinspection Duplicates*/
    public History<Integer,String> evaluate(int timeout){
        History<Integer,String> history1 = unfinishedEvaluate();
        History<Integer,String> history2 = super.evaluate(timeout);
        int score = history1.getFirst() + history2.getFirst();
        String reason = history2.getSecond().equals("") ?history1.getSecond():String.join(";",history1.getSecond(), history2.getSecond());
        return new History<Integer, String>(score,reason);
    }
}
