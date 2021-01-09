package ooad.project.service.evaluationDecorator;

import ooad.project.domain.regulatoryTask.History;

/**
 * 装饰模式的构件，来评价超时时间为timeout的得分
 */

public interface Evaluation {
    History<Integer,String> evaluate(int timeout);
}
