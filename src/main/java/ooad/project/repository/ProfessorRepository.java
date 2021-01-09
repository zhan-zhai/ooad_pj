package ooad.project.repository;

import ooad.project.domain.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor,Long> {
    Professor findProfessorByProfessorName(String professorName);
}
