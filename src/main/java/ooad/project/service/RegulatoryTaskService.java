package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;
import ooad.project.domain.regulatoryTask.*;
import ooad.project.service.evaluationDecorator.*;
import ooad.project.service.visitor.FinishSpotCheckTaskVisitor;
import ooad.project.service.visitor.UnfinishedTaskVisitor;
import ooad.project.service.visitor.Visitor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** @noinspection ALL*/
@Transactional
@Service

public class RegulatoryTaskService {

    private int getTimout(Date deadLine,Date currentTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = 0;
        long endDateTime = 0;
        try {
            startDateTime = dateFormat.parse(dateFormat.format(deadLine)).getTime();
            endDateTime = dateFormat.parse(dateFormat.format(currentTime)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }

    private History<Integer, String> getEvaluation(int timeout){
        Evaluation evaluation = new SpecificEvaluation();
        if (timeout == 0){
            return new FinishOnTimeEvaluation(evaluation).evaluate(timeout);
        }
        else if (timeout <= 20){
            return new UnfinishedEvaluation(evaluation).evaluate(timeout);
        }
        else {
            return new OverTwentyUnfinishedEvaluation(new UnfinishedEvaluation(evaluation)).evaluate(timeout);
        }
    }

    /**
     * 对具体的对某个市场的抽检任务进行评估，生成得分记录
     * @param spotCheckTask
     * @param currentTime
     * @return
     */
    public History<Integer, String> evaluateSpotCheckTask(SpotCheckTask spotCheckTask, Date currentTime){
        Date deadLine = spotCheckTask.getDeadLine();
        int timeout = spotCheckTask.isFinished()?0:getTimout(deadLine,currentTime);
        return getEvaluation(timeout);
    }

    /**
     * 对专家任务进行评分，专家将所有市场的抽检任务全部完成视为完成任务，对整个监管任务评分
     * @param professorCheckTask
     * @param currentTime
     * @return
     */
    public History<Integer, String> evaluateProfessorCheckTask(ProfessorCheckTask professorCheckTask, Date currentTime){
        Date deadLine = professorCheckTask.getDeadLine();
        int timeout = professorCheckTask.isFinished()?0:getTimout(deadLine,currentTime);
        return getEvaluation(timeout);
    }

    /**
     * 对所有市场的自检任务进行评估，生成市场和对应得分信息的记录
     * @param selfCheckTasks
     * @param currentTime
     * @return
     */
    public Map<Market, ScoreInfo> evaluateMarkets(List<SelfCheckTask> selfCheckTasks, Date currentTime){
        Map<Market,ScoreInfo> marketsScoreInfo = new HashMap<>();
        for (SelfCheckTask selfCheckTask:selfCheckTasks) {
            Map<Market,SpotCheckTask> spotCheckTaskMap = selfCheckTask.getSpotCheckTasks();
            for (Map.Entry<Market,SpotCheckTask> entry:spotCheckTaskMap.entrySet()) {
                if (getTimout(entry.getValue().getDeadLine(),currentTime) < 0)
                    continue;
                if (marketsScoreInfo.containsKey(entry.getKey())){
                    History<Integer,String> scoreRecord = evaluateSpotCheckTask(entry.getValue(),currentTime);
                    int totalScore = marketsScoreInfo.get(entry.getKey()).getTotalScore() + scoreRecord.getFirst();

                    ScoreInfo scoreInfo = marketsScoreInfo.get(entry.getKey());
                    scoreInfo.getScoreHistory().add(scoreRecord);
                    scoreInfo.setTotalScore(totalScore);

                    marketsScoreInfo.put(entry.getKey(),scoreInfo);
                }
                else {
                    ScoreInfo scoreInfo = new ScoreInfo();
                    History<Integer,String> scoreRecord = evaluateSpotCheckTask(entry.getValue(),currentTime);
                    scoreInfo.setTotalScore(scoreRecord.getFirst());

                    List<History<Integer,String>> scoreHistory = new ArrayList<>();
                    scoreHistory.add(scoreRecord);
                    scoreInfo.setScoreHistory(scoreHistory);
                    marketsScoreInfo.put(entry.getKey(),scoreInfo);
                }
            }
        }

        return marketsScoreInfo;
    }

    /**
     * 对所有的专家任务进行评估，生成专家和对应得分信息的记录
     */
    public Map<Professor,ScoreInfo> evaluateProfessors(List<ProfessorCheckTask> professorCheckTasks, Date currentTime){
        Map<Professor,ScoreInfo> professorsScoreInfo = new HashMap<>();
        for (ProfessorCheckTask professorCheckTask: professorCheckTasks) {
            Professor professor = professorCheckTask.getProfessor();
            if (getTimout(professorCheckTask.getDeadLine(),currentTime) < 0)
                    continue;

            if (professorsScoreInfo.containsKey(professor)){
                History<Integer,String> scoreRecord = evaluateProfessorCheckTask(professorCheckTask,currentTime);
                int totalScore = professorsScoreInfo.get(professor).getTotalScore() + scoreRecord.getFirst();

                ScoreInfo scoreInfo = professorsScoreInfo.get(professor);
                scoreInfo.getScoreHistory().add(scoreRecord);
                scoreInfo.setTotalScore(totalScore);

                professorsScoreInfo.put(professor,scoreInfo);
            }

            else {
                ScoreInfo scoreInfo = new ScoreInfo();
                History<Integer,String> scoreRecord = evaluateProfessorCheckTask(professorCheckTask,currentTime);
                scoreInfo.setTotalScore(scoreRecord.getFirst());

                List<History<Integer,String>> scoreHistory = new ArrayList<>();
                scoreHistory.add(scoreRecord);
                scoreInfo.setScoreHistory(scoreHistory);
                professorsScoreInfo.put(professor,scoreInfo);
            }
        }

        return professorsScoreInfo;
    }

    /**
     * 负责人将对某个市场的抽检任务标志为完成
     * @param task
     * @param spotCheckTask
     * @param checkTime
     */
    public void finishSpotCheckTask(RegulatoryTask task, SpotCheckTask spotCheckTask, Date checkTime){
        if (spotCheckTask.getCheckResults().size() == spotCheckTask.getProductsTypes().size()){
            spotCheckTask.setCheckTime(checkTime);
            spotCheckTask.setFinished(true);
//            Map<Market,SpotCheckTask> spotCheckTaskMap = task.getSpotCheckTasks();
//            boolean taskIsFinished = true;
            FinishSpotCheckTaskVisitor finishSotCheckTaskVisitor = new FinishSpotCheckTaskVisitor(true);
            task.accept(finishSotCheckTaskVisitor);
//            for (Map.Entry<Market,SpotCheckTask> entry: spotCheckTaskMap.entrySet()) {
//                if (!entry.getValue().isFinished()){
//                    taskIsFinished = false;
//                    break;
//                }
//            }
            if (finishSotCheckTaskVisitor.isTaskIsFinished()){
                task.setFinished(true);
                task.setCheckTime(checkTime);
            }
        }
    }

    /**
     * 负责人上报抽检结果
     * @param spotCheckTask
     * @param productsType
     * @param nonconforming
     * @param checkTime
     */
    public void setCheckResult(SpotCheckTask spotCheckTask, ProductsType productsType, int nonconforming, Date checkTime){
        CheckResult checkResult = new CheckResult(spotCheckTask.getMarket(),productsType,nonconforming,checkTime);
        spotCheckTask.getCheckResults().add(checkResult);
    }

    /**
     * 查看未完成任务，以及具体哪些产品类型的抽检还未完成
     * @param regulatoryTask
     * @return
     */
    public Map<SpotCheckTask, List<ProductsType>> getUnfinishedTask(RegulatoryTask regulatoryTask){
//        Map<SpotCheckTask,List<ProductsType>> unfinishedTasks = new HashMap<>();
        UnfinishedTaskVisitor unfinishedTaskVisitor = new UnfinishedTaskVisitor(new HashMap<>());
        regulatoryTask.accept(unfinishedTaskVisitor);
//        for (SpotCheckTask spotCheckTask:regulatoryTask.getSpotCheckTasks().values()) {
//            List<ProductsType> productsTypeList = spotCheckTask.getProductsTypes();
//            for (CheckResult checkResult:spotCheckTask.getCheckResults()) {
//                productsTypeList.remove(checkResult.getProductsType());
//            }
//            if (!productsTypeList.isEmpty()){
//                unfinishedTasks.put(spotCheckTask,productsTypeList);
//            }
//        }
        return unfinishedTaskVisitor.getUnfinishedTasks();
    }
}
