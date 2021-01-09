package ooad.project.domain.regulatoryTask;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 农贸市场自检任务，为一组农贸市场分配抽检任务
 */
public class SelfCheckTask extends RegulatoryTask{

    private Map<Market,SpotCheckTask> spotCheckTaskMap;

    public Map<Market, SpotCheckTask> getSpotCheckTaskMap() {
        return spotCheckTaskMap;
    }

    public void setSpotCheckTaskMap(Map<Market, SpotCheckTask> spotCheckTaskMap) {
        this.spotCheckTaskMap = spotCheckTaskMap;
    }

    public SelfCheckTask() {
    }

    public SelfCheckTask(List<Market> markets, List<ProductsType> productsTypes, Date deadLine) {
        super(markets, productsTypes, deadLine);
    }
}
