package ooad.project.service.evaluationDecorator;

import ooad.project.domain.regulatoryTask.History;

/**
 * 按时完成的评分策略
 */
public class FinishOnTimeEvaluation extends EvaluationStrategy {

    public FinishOnTimeEvaluation(Evaluation evaluation) {
        super(evaluation);
    }

    public History<Integer,String> finishOnTimeEvaluate(){

        return new History<>(10,"按时完成");
    }

    /** @noinspection Duplicates*/
    public History<Integer,String> evaluate(int timeout){
        History<Integer,String> history1 = finishOnTimeEvaluate();
        History<Integer,String> history2 = super.evaluate(timeout);

        int score = history1.getFirst() + history2.getFirst();
        String reason = history2.getSecond().equals("") ?history1.getSecond():String.join(",",history1.getSecond(), history2.getSecond());

        return new History<Integer, String>(score,reason);
    }
}
