package ooad.project.domain.regulatoryTask;

import java.util.List;

/**
 * 得分信息，存放当前总分以及历史记录
 */
public class ScoreInfo {
    private int totalScore;
    private List<History<Integer,String>> scoreHistory;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<History<Integer, String>> getScoreHistory() {
        return scoreHistory;
    }

    public void setScoreHistory(List<History<Integer, String>> scoreHistory) {
        this.scoreHistory = scoreHistory;
    }

    public ScoreInfo(int totalScore, List<History<Integer, String>> scoreHistory) {
        this.totalScore = totalScore;
        this.scoreHistory = scoreHistory;
    }

    public ScoreInfo(){}

}
