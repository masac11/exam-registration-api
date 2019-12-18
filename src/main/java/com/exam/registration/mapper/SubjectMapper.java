package com.exam.registration.mapper;

import com.exam.registration.model.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubjectMapper {
    long countSubjects();

    int deleteSubjectByPrimaryKey(Long id);

    int insertSubject(Subject subject);

    int insertSubjectSelective(Subject subject);

    Subject getSubjectByPrimaryKey(Long id);

    Subject getSubjectByCode(String code);

    List<Subject> listSubjects();

    int updateSubjectByPrimaryKeySelective(Subject subject);

    int updateSubjectByPrimaryKey(Subject subject);
}