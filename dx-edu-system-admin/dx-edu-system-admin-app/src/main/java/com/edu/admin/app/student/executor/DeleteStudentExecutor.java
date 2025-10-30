package com.edu.admin.app.student.executor;

import com.edu.admin.domain.domainservice.StudentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
@Component
@RequiredArgsConstructor
public class DeleteStudentExecutor {

    private final StudentDomainService domainService;

    public void deleteStudent(String code) {
        domainService.deleteByCode(code);
    }

}
