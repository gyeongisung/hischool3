package com.green.secondproject.teacher.subject.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectVo {
    private Long categoryId;
    private String nm;
}
