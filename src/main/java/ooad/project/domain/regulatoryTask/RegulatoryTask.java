package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.service.visitor.Visitor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 监管任务，可分为两个子类
 */
public class RegulatoryTask {
    private Map<Market,SpotCheckTask> spotCheckTasks;
    private List<Market> markets;
    private List<ProductsType> productsTypes;
    private Date deadLine;
    private Date checkTime;
    private boolean isFinished = false;
//    private List<SpotCheckTask> spotCheckTasks;


    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
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

//    public List<SpotCheckTask> getSpotCheckTasks() {
//        return spotCheckTasks;
//    }
//
//    public void setSpotCheckTasks(List<SpotCheckTask> spotCheckTasks) {
//        this.spotCheckTasks = spotCheckTasks;
//    }


    public Map<Market, SpotCheckTask> getSpotCheckTasks() {
        return spotCheckTasks;
    }

    public void setSpotCheckTasks(Map<Market, SpotCheckTask> spotCheckTasks) {
        this.spotCheckTasks = spotCheckTasks;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public RegulatoryTask() {
    }

    public RegulatoryTask(List<Market> markets, List<ProductsType> productsTypes, Date deadLine) {
        this.markets = markets;
        this.productsTypes = productsTypes;
        this.deadLine = deadLine;
    }

}
