package com.edu.admin.client.student.api;

import com.edu.admin.client.ResResult;
import com.edu.admin.client.student.bean.dto.StudentDto;
import com.edu.admin.client.student.bean.request.StudentSynRequest;

/**
 * 学员业务service
 *
 * @author hjw
 * @since 2025-10-22
 */
public interface StudentClient {

    ResResult<StudentDto> findByCode(String code);

    ResResult save(StudentSynRequest synRequest);

    ResResult update(StudentSynRequest synRequest);

    ResResult deleteByCode(String code);

}
