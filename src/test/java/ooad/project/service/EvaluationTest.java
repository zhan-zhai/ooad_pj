package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;
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
 * 验证专家和农贸市场按时完成和未按时完成的抽检的场景，获取评估总得分和评估得/扣分的记录
 */
public class EvaluationTest {
    @Autowired
    private RegulatoryTaskService regulatoryTaskService;
    @Autowired
    private ProductsTypeService productsTypeService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private RegulatorService regulatorService;

    private DateFormat dateFormat;
    private List<ProductsType> productsTypes;
    private List<Market> markets;
    private Date deadLine;
    private Professor professor;
    private List<History<Integer,String>> marketScoreHistory1 ;
    private List<History<Integer,String>> marketScoreHistory2 ;
    private List<History<Integer,String>> marketScoreHistory3 ;
    private List<History<Integer,String>> professorScoreHistory;

    @Before
    public void before() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        productsTypes = new ArrayList<>();
        markets = new ArrayList<>();
        deadLine = dateFormat.parse("2021-01-16");
        professor = professorService.searchProfessor("马老师");

        productsTypes.add(productsTypeService.searchProductsType("果蔬"));
        productsTypes.add(productsTypeService.searchProductsType("花卉"));
        productsTypes.add(productsTypeService.searchProductsType("粮油"));
        markets.add(marketService.searchMarket("杨浦菜市场"));
        markets.add(marketService.searchMarket("浦东菜市场"));
        markets.add(marketService.searchMarket("徐汇菜市场"));

        History<Integer, String> finishOnTimeHistory = new History<>(10, "按时完成+10");
        History<Integer, String> unfinishedHistory = new History<>(-10, "未完成-10");
        History<Integer, String> overTwentyUnfinishedHistory = new History<>(-30, "未完成-10;超时20天未完成-20");

        marketScoreHistory1 = new ArrayList<>();
        marketScoreHistory2 = new ArrayList<>();
        marketScoreHistory3 = new ArrayList<>();
        professorScoreHistory = new ArrayList<>();
        marketScoreHistory1.add(finishOnTimeHistory);
        marketScoreHistory1.add(unfinishedHistory);
        marketScoreHistory2.add(unfinishedHistory);
        marketScoreHistory2.add(unfinishedHistory);
        marketScoreHistory3.add(unfinishedHistory);
        marketScoreHistory3.add(overTwentyUnfinishedHistory);

        professorScoreHistory.add(unfinishedHistory);
        professorScoreHistory.add(overTwentyUnfinishedHistory);
    }

    @Test
    public void testEvaluation() throws ParseException {
        Map<Market, ScoreInfo> marketScoreInfoMap = new HashMap<>();
        Map<Professor,ScoreInfo> professorScoreInfoMap = new HashMap<>();
        List<Market> marketMap = new ArrayList<>();

        SelfCheckTask selfCheckTask1 = regulatorService.launchSelfCheckTask(markets, productsTypes, deadLine);
        SelfCheckTask selfCheckTask2 = regulatorService.launchSelfCheckTask(markets, productsTypes, deadLine);
        ProfessorCheckTask professorCheckTask1 = regulatorService.launchProfessorCheckTask(markets, productsTypes, professor,deadLine);
        ProfessorCheckTask professorCheckTask2 = regulatorService.launchProfessorCheckTask(markets, productsTypes, professor,deadLine);

        List<ProfessorCheckTask> professorCheckTasks = new ArrayList<>();
        professorCheckTasks.add(professorCheckTask1);
        professorCheckTasks.add(professorCheckTask2);
        List<SelfCheckTask> selfCheckTasks = new ArrayList<>();
        selfCheckTasks.add(selfCheckTask1);
        selfCheckTasks.add(selfCheckTask2);

        Date currentTime = dateFormat.parse("2021-02-15");
        //对于第一个自检任务的每个产品类型，每个市场下有两个不合格数，
        //市场先后依次完成任务
        //日期从5号开始，每完成一种类型，日期加3
        //市场得分依次为10，-10，-10
        Date productsTypeCheckTime = dateFormat.parse("2021-01-05");
        for (SpotCheckTask spotCheckTask:selfCheckTask1.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,2,productsTypeCheckTime);
                productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000 * 3);
            }

            regulatoryTaskService.finishSpotCheckTask(selfCheckTask1,spotCheckTask,productsTypeCheckTime);
            marketMap.add(spotCheckTask.getMarket());
        }
        //对于第二个自检任务的每个产品类型，每个市场下有两个不合格数，
        //市场先后依次完成任务
        //日期从10号开始，每完成一种类型，日期加3
        //市场得分依次为-10，-10，-30
        productsTypeCheckTime = dateFormat.parse("2021-01-10");
        for (SpotCheckTask spotCheckTask:selfCheckTask2.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,2,productsTypeCheckTime);
                productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000 * 3);
            }

            regulatoryTaskService.finishSpotCheckTask(selfCheckTask2,spotCheckTask,productsTypeCheckTime);
        }

        //对于第一个专家任务的每个产品类型，每个市场下有三个不合格数，
        //专家对市场先后依次进行抽检
        //日期从10号开始，每完成一种类型，日期加1
        //专家得分为-10
        productsTypeCheckTime = dateFormat.parse("2021-01-10");
        for (SpotCheckTask spotCheckTask:professorCheckTask1.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,3,productsTypeCheckTime);
                productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000);
            }

            regulatoryTaskService.finishSpotCheckTask(professorCheckTask1,spotCheckTask,productsTypeCheckTime);
        }

        //对于第二个专家任务的每个产品类型，每个市场下有三个不合格数，
        //专家对市场先后依次进行抽检
        //日期从10号开始，每完成一种类型，日期加3
        //专家得分为-30
        productsTypeCheckTime = dateFormat.parse("2021-01-10");
        for (SpotCheckTask spotCheckTask:professorCheckTask1.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,3,productsTypeCheckTime);
                productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000 * 3);
            }

            regulatoryTaskService.finishSpotCheckTask(professorCheckTask1,spotCheckTask,productsTypeCheckTime);
        }

        marketScoreInfoMap.put(marketMap.get(0),new ScoreInfo(0,marketScoreHistory1));
        marketScoreInfoMap.put(marketMap.get(1),new ScoreInfo(-20,marketScoreHistory2));
        marketScoreInfoMap.put(marketMap.get(2),new ScoreInfo(-40,marketScoreHistory3));
        professorScoreInfoMap.put(professor,new ScoreInfo(-40,professorScoreHistory));

        assert (marketScoreInfoMapEquals(regulatoryTaskService.evaluateMarkets(selfCheckTasks,currentTime),marketScoreInfoMap));
        assert (professorScoreInfoMapEquals(regulatoryTaskService.evaluateProfessors(professorCheckTasks,currentTime),professorScoreInfoMap));
    }

    boolean marketScoreInfoMapEquals(Map<Market, ScoreInfo> marketScoreInfoMap1,Map<Market, ScoreInfo> marketScoreInfoMap2){
        for (Market market:marketScoreInfoMap1.keySet()){
            if (!(marketScoreInfoMap2.containsKey(market) && marketScoreInfoMap1.get(market).equals(marketScoreInfoMap2.get(market))))
                return false;
        }
        return true;
    }

    boolean professorScoreInfoMapEquals(Map<Professor,ScoreInfo> professorScoreInfoMap1,Map<Professor,ScoreInfo> professorScoreInfoMap2){
        for (Professor professor:professorScoreInfoMap1.keySet()){
            if (!(professorScoreInfoMap2.containsKey(professor) && professorScoreInfoMap1.get(professor).equals(professorScoreInfoMap2.get(professor))))
                return false;
        }
        return true;
    }
}
