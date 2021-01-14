package ooad.project.service.visitor;

import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.CheckResult;
import ooad.project.domain.regulatoryTask.SpotCheckTask;

import java.util.List;
import java.util.Map;

/**
 * 遍历获取所有未完成任务
 */
public class UnfinishedTaskVisitor extends Visitor{
    private final Map<SpotCheckTask, List<ProductsType>> unfinishedTasks;

    public UnfinishedTaskVisitor(Map<SpotCheckTask, List<ProductsType>> unfinishedTasks) {
        this.unfinishedTasks = unfinishedTasks;
    }

    public Map<SpotCheckTask, List<ProductsType>> getUnfinishedTasks() {
        return unfinishedTasks;
    }

    @Override
    public void visit(SpotCheckTask spotCheckTask) {
        List<ProductsType> productsTypeList = spotCheckTask.getProductsTypes();
        for (CheckResult checkResult:spotCheckTask.getCheckResults()) {
            productsTypeList.remove(checkResult.getProductsType());
        }
        if (!productsTypeList.isEmpty()){
            unfinishedTasks.put(spotCheckTask,productsTypeList);
        }
    }
}
