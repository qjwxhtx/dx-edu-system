package com.edu.admin.domain.domainservice;


import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 课程domainService
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepo courseRepo;

    public CourseDo save(CourseDo courseDo) {
        return courseRepo.save(courseDo);
    }

    public void delete(CourseDo courseDo) {
        courseRepo.delete(courseDo);
    }

    public CourseDo detail(CourseDo courseDo) {
        return courseRepo.detail(courseDo);
    }

    public CourseDo update(CourseDo courseDo) {
        return courseRepo.update(courseDo);
    }
}
