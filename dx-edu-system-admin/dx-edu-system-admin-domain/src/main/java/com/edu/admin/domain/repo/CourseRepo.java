package com.edu.admin.domain.repo;

import com.edu.admin.domain.bean.CourseDo;
import org.springframework.data.domain.Page;

/**
 * 课程repo
 *
 * @author qianwj
 * @since 2025-10-15
 */

public interface CourseRepo {

    CourseDo save(CourseDo courseDo);

    CourseDo update(CourseDo courseDo);

    void delete(CourseDo courseDo);

    CourseDo detail(CourseDo courseDo);

    Page<CourseDo> pageList(CourseDo pageDo);
}
