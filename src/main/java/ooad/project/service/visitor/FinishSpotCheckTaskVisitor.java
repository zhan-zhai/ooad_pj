package ooad.project.service.visitor;

import ooad.project.domain.regulatoryTask.SpotCheckTask;

/**
 * 遍历获取抽检任务是否完成
 */
public class FinishSpotCheckTaskVisitor extends Visitor{

    private boolean taskIsFinished;

    public boolean isTaskIsFinished() {
        return taskIsFinished;
    }

    public FinishSpotCheckTaskVisitor(boolean taskIsFinished) {
        this.taskIsFinished = taskIsFinished;
    }

    @Override
    public void visit(SpotCheckTask spotCheckTask){
        if (!spotCheckTask.isFinished())
            taskIsFinished = false;
    }
}
