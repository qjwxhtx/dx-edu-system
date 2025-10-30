package com.edu.admin.app.student.executor;

import com.edu.admin.app.student.bean.cmd.cud.StudentCreateCmd;
import com.edu.admin.domain.domainservice.StudentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.edu.admin.app.student.convertor.StudentConvertor.createCmd2Do;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
@Component
@RequiredArgsConstructor
public class CreateStudentExecutor {

    private final StudentDomainService domainService;

    public void createStudent(StudentCreateCmd createCmd) {
        domainService.save(createCmd2Do(createCmd));
    }

}
