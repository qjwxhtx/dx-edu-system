package com.edu.admin.app.student.service;


import com.edu.admin.app.student.executor.CreateStudentExecutor;
import com.edu.admin.app.student.executor.DeleteStudentExecutor;
import com.edu.admin.app.student.executor.QueryStudentExecutor;
import com.edu.admin.app.student.executor.UpdateStudentExecutor;
import com.edu.admin.client.ResResult;
import com.edu.admin.client.student.api.StudentClient;
import com.edu.admin.client.student.bean.dto.StudentDto;
import com.edu.admin.client.student.bean.request.StudentSynRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.edu.admin.app.student.convertor.StudentConvertor.request2CreateCmd;
import static com.edu.admin.app.student.convertor.StudentConvertor.request2UpdateCmd;

/**
 * 学员业务实现
 *
 * @author hjw
 * @since 2025-10-22
 */
@Service
@RequiredArgsConstructor
public class StudentClientServiceImpl implements StudentClient {

    private final QueryStudentExecutor queryStudentExecutor;

    private final CreateStudentExecutor createStudentExecutor;

    private final UpdateStudentExecutor updateStudentExecutor;

    private final DeleteStudentExecutor deleteStudentExecutor;

    @Override
    public ResResult<StudentDto> findByCode(String code) {
        return ResResult.okResult(queryStudentExecutor.queryStudent(code));
    }

    @Override
    public ResResult save(StudentSynRequest synRequest) {
        createStudentExecutor.createStudent(request2CreateCmd(synRequest));
        return ResResult.okResult();
    }

    @Override
    public ResResult update(StudentSynRequest synRequest) {
        updateStudentExecutor.updateStudent(request2UpdateCmd(synRequest));
        return ResResult.okResult();
    }

    @Override
    public ResResult deleteByCode(String code) {
        deleteStudentExecutor.deleteStudent(code);
        return ResResult.okResult();
    }
}
