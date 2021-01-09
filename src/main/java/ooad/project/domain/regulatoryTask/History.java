package ooad.project.domain.regulatoryTask;

/**
 * 二元祖，用来存放得分和原因的记录
 * @param <A>
 * @param <B>
 */
public class History<A, B> extends ScoreInfo {

    private final A first;

    private final B second;

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public History(A a, B b){
        first = a;
        second = b;
    }

    public String toString(){
        return "(" + first + ", " + second + ")";
    }

}