package ooad.project.service;

import ooad.project.domain.Market;
import ooad.project.domain.Professor;
import ooad.project.domain.regulatoryTask.*;
import ooad.project.service.evaluationDecorator.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** @noinspection ALL*/
@Transactional
@Service

public class RegulatoryTaskService {

    private int getTimout(Date checkTime,Date currentTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = 0;
        long endDateTime = 0;
        try {
            startDateTime = dateFormat.parse(dateFormat.format(checkTime)).getTime();
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
        Date checkTime = spotCheckTask.getCheckTime();
        int timeout = spotCheckTask.isFinished()?0:getTimout(checkTime,currentTime);
        return getEvaluation(timeout);
    }

//    public int evaluateProfessorCheckTask(ProfessorCheckTask professorCheckTask,Date currentTime){
//        Date checkTime = professorCheckTask.getCheckTime();
//        int timeout = professorCheckTask.isFinished()?0:getTimout(checkTime,currentTime);
//        return getEvaluation(timeout);
//    }

    /**
     * 对所有市场的自检任务进行评估，生成市场和对应得分信息的记录
     * @param selfCheckTasks
     * @param currentTime
     * @return
     */
    public Map<Market, ScoreInfo> evaluateMarkets(List<SelfCheckTask> selfCheckTasks, Date currentTime){
        Map<Market,ScoreInfo> marketsScoreInfo = new HashMap<>();
        for (SelfCheckTask selfCheckTask:selfCheckTasks) {
            Map<Market,SpotCheckTask> spotCheckTaskMap = selfCheckTask.getSpotCheckTaskMap();
            for (Map.Entry<Market,SpotCheckTask> entry:spotCheckTaskMap.entrySet()) {
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
            Map<Professor,SpotCheckTask> spotCheckTaskMap = professorCheckTask.getSpotCheckTaskMap();
            for (Map.Entry<Professor,SpotCheckTask> entry:spotCheckTaskMap.entrySet()) {
                if (professorsScoreInfo.containsKey(entry.getKey())){

                    History<Integer,String> scoreRecord = evaluateSpotCheckTask(entry.getValue(),currentTime);
                    int totalScore = professorsScoreInfo.get(entry.getKey()).getTotalScore() + scoreRecord.getFirst();

                    ScoreInfo scoreInfo = professorsScoreInfo.get(entry.getKey());
                    scoreInfo.getScoreHistory().add(scoreRecord);
                    scoreInfo.setTotalScore(totalScore);

                    professorsScoreInfo.put(entry.getKey(),scoreInfo);
                }

                else {
                    ScoreInfo scoreInfo = new ScoreInfo();
                    History<Integer,String> scoreRecord = evaluateSpotCheckTask(entry.getValue(),currentTime);
                    scoreInfo.setTotalScore(scoreRecord.getFirst());

                    List<History<Integer,String>> scoreHistory = new ArrayList<>();
                    scoreHistory.add(scoreRecord);
                    scoreInfo.setScoreHistory(scoreHistory);
                    professorsScoreInfo.put(entry.getKey(),scoreInfo);
                }
            }
        }

        return professorsScoreInfo;
    }
}
