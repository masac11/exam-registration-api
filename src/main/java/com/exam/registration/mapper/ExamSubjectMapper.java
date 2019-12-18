package com.exam.registration.mapper;

import com.exam.registration.model.ExamSubject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamSubjectMapper {
    long countExamSubjects();

    int deleteExamSubjectByPrimaryKey(Long id);

    int insertExamSubject(ExamSubject examSubject);

    int insertExamSubjectSelective(ExamSubject examSubject);

    ExamSubject getExamSubjectByPrimaryKey(Long id);

    List<ExamSubject> listExamSubjects();

    int updateExamSubjectByPrimaryKeySelective(ExamSubject examSubject);

    int updateExamSubjectByPrimaryKey(ExamSubject examSubject);
}