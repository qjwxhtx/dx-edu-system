package com.edu.admin.infra.repoimpl;


import com.edu.admin.domain.bean.CourseDo;
import com.edu.admin.domain.repo.CourseRepo;
import com.edu.admin.infra.bean.entity.CourseEntity;
import com.edu.admin.infra.convertor.CourseInfraConvertor;
import com.edu.admin.infra.repositories.CourseRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 课程repo实现
 *
 * @author qianwj
 * @since 2025-10-22
 */
@Component
@RequiredArgsConstructor
public class CourseRepoImpl implements CourseRepo {

    private final CourseRepository courseRepository;

    @Override
    public CourseDo save(CourseDo courseDo) {
        CourseEntity courseEntity = CourseInfraConvertor.doToEntity(courseDo);
        CourseEntity save = courseRepository.save(courseEntity);
        return CourseInfraConvertor.entityToDo(save);
    }

    @Override
    public CourseDo update(CourseDo courseDo) {
        CourseEntity courseEntity = CourseInfraConvertor.doToEntity(courseDo);
        CourseEntity save = courseRepository.save(courseEntity);
        return CourseInfraConvertor.entityToDo(save);
    }

    @Override
    public void delete(CourseDo courseDo) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseDo.getId());
        optionalCourse.ifPresent(entity -> courseRepository.deleteById(entity.getId()));
    }

    @Override
    public CourseDo detail(CourseDo courseDo) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(courseDo.getId());
        return optionalCourse.map(CourseInfraConvertor::entityToDo).orElse(null);
    }

    @Override
    public Page<CourseDo> pageList(CourseDo courseDo) {
        Page<CourseEntity> entityPage =
            courseRepository.findAll((Specification<CourseEntity>) (root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (StringUtils.isNotEmpty(courseDo.getCourseName())) {
                    predicate = criteriaBuilder.like(root.get("courseName").as(String.class),
                        "%" + courseDo.getCourseName() + "%");
                }
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                return predicate;
            }, PageRequest.of(courseDo.getPageNum() - 1, courseDo.getPageSize()));
        return entityPage.map(CourseInfraConvertor::entityToDo);
    }
}
