package ooad.project.service.visitor;

import ooad.project.domain.regulatoryTask.SpotCheckTask;

public abstract class Visitor {
    public abstract void visit(SpotCheckTask spotCheckTask);
}

