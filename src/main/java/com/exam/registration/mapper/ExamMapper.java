package com.exam.registration.mapper;

import com.exam.registration.model.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExamMapper {
    long countExams();

    int deleteExamByPrimaryKey(Long id);

    int insertExam(Exam record);

    int insertExamSelective(Exam record);

    Exam getExamByPrimaryKey(Long id);

    List<Exam> listExams();

    int updateExamByPrimaryKeySelective(Exam record);

    int updateExamByPrimaryKey(Exam record);
}