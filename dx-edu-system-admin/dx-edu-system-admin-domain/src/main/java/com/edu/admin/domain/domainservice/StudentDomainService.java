package com.edu.admin.domain.domainservice;


import com.edu.admin.domain.bean.StudentDo;
import com.edu.admin.domain.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 学员domainservice
 *
 * @author hjw
 * @since 2025-10-22
 */
@Component
@RequiredArgsConstructor
public class StudentDomainService {

    private final StudentRepo studentRepo;

    public StudentDo findByCode(String code) {
        return studentRepo.findByCode(code);
    }

    public void save(StudentDo studentDo) {
        studentRepo.save(studentDo);
    }

    public void update(StudentDo studentDo) {
        studentRepo.update(studentDo);
    }

    public void deleteByCode(String code) {
        studentRepo.deleteByCode(code);
    }

}
