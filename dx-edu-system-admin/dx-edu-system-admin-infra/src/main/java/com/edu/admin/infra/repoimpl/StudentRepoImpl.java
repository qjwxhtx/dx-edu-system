package com.edu.admin.infra.repoimpl;


import com.edu.admin.domain.bean.StudentDo;
import com.edu.admin.domain.repo.StudentRepo;
import com.edu.admin.infra.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.edu.admin.infra.convertor.StudentInfraConvertor.do2Entity;
import static com.edu.admin.infra.convertor.StudentInfraConvertor.entity2Do;

/**
 * 学员RepoImpl
 *
 * @author hjw
 * @since 2025-10-22
 */
@Repository
@RequiredArgsConstructor
public class StudentRepoImpl implements StudentRepo {

    private final StudentRepository studentRepository;

    @Override
    public StudentDo findByCode(String code) {
        return entity2Do(studentRepository.findByStudentCode(code));
    }

    @Override
    public void save(StudentDo studentDo) {
        studentRepository.save(do2Entity(studentDo));
    }

    @Override
    public void update(StudentDo studentDo) {
        studentRepository.save(do2Entity(studentDo));
    }

    @Override
    public void deleteByCode(String code) {
        studentRepository.deleteByStudentCode(code);
    }

}
