package ooad.project.service.visitor;

import ooad.project.domain.regulatoryTask.SpotCheckTask;

/**
 * 用于实现访问者模式
 */
public abstract class Visitor {
    public abstract void visit(SpotCheckTask spotCheckTask);
}

