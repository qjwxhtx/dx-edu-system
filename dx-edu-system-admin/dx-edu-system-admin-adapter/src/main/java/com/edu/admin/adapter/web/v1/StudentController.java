package com.edu.admin.adapter.web.v1;

import com.edu.admin.client.ResResult;
import com.edu.admin.client.student.api.StudentClient;
import com.edu.admin.client.student.bean.dto.StudentDto;
import com.edu.admin.client.student.bean.request.StudentSynRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author hjw
 * @since 2025-10-22
 */
@RestController
@RequestMapping("web/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentClient studentClient;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("findByCode")
    public ResResult<StudentDto> findByCode(@RequestParam("code") String code) {
        return studentClient.findByCode(code);
    }

    @PostMapping("/save")
    public ResResult save(@RequestBody StudentSynRequest request) {
        return studentClient.save(request);
    }

    @PostMapping("/update")
    public ResResult update(@RequestBody StudentSynRequest request) {
        return studentClient.update(request);
    }

    @PutMapping("/deleteByCode")
    public ResResult deleteByCode(@RequestParam("code") String code) {
        return studentClient.deleteByCode(code);
    }

}
