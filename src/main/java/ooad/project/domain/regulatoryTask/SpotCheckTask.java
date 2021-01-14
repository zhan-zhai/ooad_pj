package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;


/**
 * 抽检任务，对应于对一个具体农贸市场的相关产品类别的检查
 */
public class SpotCheckTask {
    private Market market;
    private List<ProductsType> productsTypes;
    private List<ProductsType> finishedProductsType;
    private Date deadLine;
    private Date checkTime;
    private boolean isFinished = false;
    private List<CheckResult> checkResults;

    public Market getMarket() {
        return market;
    }

    public List<ProductsType> getFinishedProductsType() {
        return finishedProductsType;
    }

    public void setFinishedProductsType(List<ProductsType> finishedProductsType) {
        this.finishedProductsType = finishedProductsType;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public List<ProductsType> getProductsTypes() {
        return productsTypes;
    }

    public void setProductsTypes(List<ProductsType> productsTypes) {
        this.productsTypes = productsTypes;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public List<CheckResult> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<CheckResult> checkResults) {
        this.checkResults = checkResults;
    }

    public SpotCheckTask() {
    }

    public SpotCheckTask(Market market, List<ProductsType> productsTypes, Date deadLine) {
        this.market = market;
        this.productsTypes = productsTypes;
        this.deadLine = deadLine;
        this.checkResults = new ArrayList<>();
        this.finishedProductsType = new ArrayList<>();
    }

    public boolean equals(SpotCheckTask spotCheckTask){
        return this.market == spotCheckTask.market && this.productsTypes == spotCheckTask.productsTypes;
    }
}
