package ooad.project.domain.regulatoryTask;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreInfo scoreInfo = (ScoreInfo) o;

        return totalScore == scoreInfo.totalScore && Objects.equals(scoreHistory, scoreInfo.scoreHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalScore, scoreHistory);
    }
}
