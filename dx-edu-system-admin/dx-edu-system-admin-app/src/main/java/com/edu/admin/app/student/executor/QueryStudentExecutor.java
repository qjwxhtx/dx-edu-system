package com.edu.admin.app.student.executor;


import com.edu.admin.client.student.bean.dto.StudentDto;
import com.edu.admin.domain.domainservice.StudentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.edu.admin.app.student.convertor.StudentConvertor.do2Dto;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
@Component
@RequiredArgsConstructor
public class QueryStudentExecutor {

    private final StudentDomainService domainService;

    public StudentDto queryStudent(String code) {
        return do2Dto(domainService.findByCode(code));
    }

}
