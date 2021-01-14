package ooad.project.service.visitor;

import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.CheckResult;
import ooad.project.domain.regulatoryTask.SpotCheckTask;

import java.util.Date;

/**
 * 遍历获取产品不合格总数
 */
public class GetTotalNonconformingVisitor extends Visitor{
    private final ProductsType productsType;
    private final Date startTime;
    private final Date endTime;
    private int totalNonconforming;

    public ProductsType getProductsType() {
        return productsType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTotalNonconforming() {
        return totalNonconforming;
    }

    public GetTotalNonconformingVisitor(ProductsType productsType, Date startTime, Date endTime, int totalNonconforming) {
        this.productsType = productsType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalNonconforming = totalNonconforming;
    }

    public GetTotalNonconformingVisitor(ProductsType productsType, Date startTime, Date endTime) {
        this.productsType = productsType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void visit(SpotCheckTask spotCheckTask) {
        for (CheckResult checkResult:spotCheckTask.getCheckResults()){
            Date checkTime = checkResult.getCheckTime();
            if (checkResult.getProductsType() == productsType && checkTime.after(startTime) && checkTime.before(endTime)){
                totalNonconforming += checkResult.getNonconforming();
            }
        }
    }
}
