package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.regulatoryTask.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest

/*
 * 测试监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务
 */
public class SelfCheckTaskTest {
    @Autowired
    private RegulatoryTaskService regulatoryTaskService;
    @Autowired
    private ProductsTypeService productsTypeService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private RegulatorService regulatorService;

    private DateFormat dateFormat;
    private List<ProductsType> productsTypes;
    private List<Market> markets;
    private Date deadLine;
    private Map<SpotCheckTask, List<ProductsType>> unfinishedTask;

    @Before
    public void before() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        productsTypes = new ArrayList<>();
        markets = new ArrayList<>();
        deadLine = dateFormat.parse("2021-01-16");
        List<String> marketList = new ArrayList<>();
        List<String> productsTypeList = new ArrayList<>();
        unfinishedTask = new HashMap<>();

        marketList.add("杨浦菜市场");
        marketList.add("浦东菜市场");
        marketList.add("徐汇菜市场");
        productsTypeList.add("果蔬");
        productsTypeList.add("花卉");
        productsTypeList.add("粮油");

        for (String productsType: productsTypeList) {
            productsTypes.add(productsTypeService.searchProductsType(productsType));
        }
        for (String market: marketList) {
            markets.add(marketService.searchMarket(market));
        }
    }

    @Test
    public void testLaunchAndFinishSelfCheckTask() throws ParseException {
        SelfCheckTask selfCheckTask = regulatorService.launchSelfCheckTask(markets, productsTypes, deadLine);

        for (SpotCheckTask spotCheckTask:selfCheckTask.getSpotCheckTasks().values()){
            unfinishedTask.put(spotCheckTask,productsTypes);
        }

        //可以查询到未完成任务，此时未完成任务为自检任务的所有抽检任务的集合
        Map<SpotCheckTask, List<ProductsType>> aUnfinishedTask = regulatoryTaskService.getUnfinishedTask(selfCheckTask);
        assert(unfinishedTaskEquals(aUnfinishedTask, unfinishedTask));

        //负责人完成抽检任务，当所有抽检任务完成以后，自检任务自动标记为完成
        Date productsTypeCheckTime = dateFormat.parse("2021-01-15");
        Date spotCheckTaskCheckTime = dateFormat.parse("2021-01-16");
        for (SpotCheckTask spotCheckTask:unfinishedTask.keySet()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,2,productsTypeCheckTime);
            }
            regulatoryTaskService.finishSpotCheckTask(selfCheckTask,spotCheckTask,spotCheckTaskCheckTime);
        }
        assert (selfCheckTask.isFinished());

        //此时任务全部完成，未完成任务集合为空
        assert (regulatoryTaskService.getUnfinishedTask(selfCheckTask).size()==0);
    }

    @Test
    public void testHalfFinishedSelfTask() throws ParseException {//测试任务部分完成
        SelfCheckTask selfCheckTask = regulatorService.launchSelfCheckTask(markets, productsTypes, deadLine);

        for (SpotCheckTask spotCheckTask:selfCheckTask.getSpotCheckTasks().values()){
            unfinishedTask.put(spotCheckTask,productsTypes);
        }

        int task = 1;
        Date productsTypeCheckTime = dateFormat.parse("2021-01-15");
        Date spotCheckTaskCheckTime = dateFormat.parse("2021-01-16");
        Map<SpotCheckTask, List<ProductsType>> aUnfinishedTask = new HashMap<>();

        //第一个抽检任务全部完成，第二个抽检任务完成部分，第三个抽检任务完全没有完成
        for (SpotCheckTask spotCheckTask:unfinishedTask.keySet()) {
            if (task == 1){
                for (ProductsType productsType:productsTypes){
                    regulatoryTaskService.setCheckResult(spotCheckTask,productsType,2,productsTypeCheckTime);
                }
                aUnfinishedTask.put(spotCheckTask,new ArrayList<>());
                regulatoryTaskService.finishSpotCheckTask(selfCheckTask,spotCheckTask,spotCheckTaskCheckTime);
            }
            else if (task==2){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsTypes.get(0),2,productsTypeCheckTime);
                List<ProductsType> typeList = new ArrayList<>();
                for (ProductsType productsType:productsTypes){
                    if (!(productsType == productsTypes.get(0)))
                        typeList.add(productsType);
                }
                aUnfinishedTask.put(spotCheckTask,typeList);
            }
            else {
                aUnfinishedTask.put(spotCheckTask,productsTypes);
            }
            task ++;
        }

        assert (unfinishedTaskEquals(regulatoryTaskService.getUnfinishedTask(selfCheckTask),aUnfinishedTask));
    }

    boolean unfinishedTaskEquals(Map<SpotCheckTask, List<ProductsType>> unfinishedTask1, Map<SpotCheckTask, List<ProductsType>> unfinishedTask2){
        boolean flag = true;
        for (SpotCheckTask spotCheckTask: unfinishedTask1.keySet()) {
            if (!(unfinishedTask2.containsKey(spotCheckTask) && unfinishedTask1.get(spotCheckTask).containsAll(unfinishedTask2.get(spotCheckTask)) && unfinishedTask2.get(spotCheckTask).containsAll(unfinishedTask1.get(spotCheckTask)))) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}