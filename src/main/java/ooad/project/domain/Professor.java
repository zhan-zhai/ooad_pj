package ooad.project.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROFESSOR")

public class Professor implements Serializable {

    private static final long serialVersionUID = -5941972960933012542L;

    @Id
    @Column(name = "PROFESSOR_ID")
    private int professorId;

    @Column(name = "PROFESSOR_NAME")
    private String professorName;

    public Professor() {
    }

    public Professor(int professorId, String professorName) {
        this.professorId = professorId;
        this.professorName = professorName;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
}
