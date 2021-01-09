package ooad.project.service;

import ooad.project.domain.ProductsType;
import ooad.project.domain.Professor;
import ooad.project.domain.regulatoryTask.ProfessorCheckTask;
import ooad.project.domain.regulatoryTask.SpotCheckTask;
import ooad.project.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
@Service

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public Professor searchProfessor(String professorName){
        return professorRepository.findProfessorByProfessorName(professorName);
    }

}
