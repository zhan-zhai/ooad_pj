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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest

/*
 * 测试监管局查看某个农贸产品类别在某个时间范围内的总的不合格数
 */
public class GetNonconformingTest {
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
    }

    @Test
    public void testGetTotalNonconforming() throws ParseException {
        List<RegulatoryTask> regulatoryTasks = new ArrayList<>();

        SelfCheckTask selfCheckTask = regulatorService.launchSelfCheckTask(markets, productsTypes, deadLine);
        ProfessorCheckTask professorCheckTask = regulatorService.launchProfessorCheckTask(markets, productsTypes, professor,deadLine);
        regulatoryTasks.add(selfCheckTask);
        regulatoryTasks.add(professorCheckTask);

        //对于自检任务的每个产品类型，每个市场下有两个不合格数，
        //日期从15号开始，每完成一种类型，日期加1，17号结束
        Date productsTypeCheckTime = dateFormat.parse("2021-01-15");
        for (SpotCheckTask spotCheckTask:selfCheckTask.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,2,productsTypeCheckTime);
            }
            productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000);
            regulatoryTaskService.finishSpotCheckTask(selfCheckTask,spotCheckTask,productsTypeCheckTime);
        }

        //对于专家任务的每个产品类型，每个市场下有三个不合格数，
        //日期从16号开始，每完成一种类型，日期加1，18号结束
        productsTypeCheckTime = dateFormat.parse("2021-01-16");
        for (SpotCheckTask spotCheckTask:professorCheckTask.getSpotCheckTasks().values()) {
            for (ProductsType productsType:productsTypes){
                regulatoryTaskService.setCheckResult(spotCheckTask,productsType,3,productsTypeCheckTime);

            }
            productsTypeCheckTime = new Date(productsTypeCheckTime.getTime() + 86400000);
            regulatoryTaskService.finishSpotCheckTask(professorCheckTask,spotCheckTask,productsTypeCheckTime);
        }

        //测试14到17号之间（不包含）的不合格数
        Date startTime = dateFormat.parse("2021-01-14");
        Date endTime = dateFormat.parse("2021-01-17");
        assert (productsTypeService.getTotalNonconforming(regulatoryTasks,startTime,endTime,productsTypes.get(0))==7);

        //测试14到18号（不包含）之间的不合格数
        startTime = dateFormat.parse("2021-01-14");
        endTime = dateFormat.parse("2021-01-19");
        assert (productsTypeService.getTotalNonconforming(regulatoryTasks,startTime,endTime,productsTypes.get(0))==15);
    }
}
