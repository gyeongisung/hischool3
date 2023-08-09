package com.green.secondproject.teacher.subject.model.graph;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@Data
@Builder
public class MockGraphVo {
    private List<MockGraphVo2> koList;
    private List<MockGraphVo2> mtList;
    private List<MockGraphVo2> enList;
    private List<MockGraphVo2> hiList;
}
