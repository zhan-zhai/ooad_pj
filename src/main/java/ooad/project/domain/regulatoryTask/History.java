package ooad.project.domain.regulatoryTask;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History<?, ?> history = (History<?, ?>) o;
        return Objects.equals(first, history.first) && Objects.equals(second, history.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}