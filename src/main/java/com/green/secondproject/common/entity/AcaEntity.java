package com.green.secondproject.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name="aca_result", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","year","subject_id", "semester","mid_final"})})
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true) // 부모클래스에 있는 toString 어노테이션을 호출 (만약 부모클래스에도 없으면 해시코드 출력)
@DynamicInsert
public class AcaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="result_id", updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "user_id"
            , nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "subject_id"
            , nullable = false)
    private SubjectEntity subjectEntity;

    @Column(length = 4)
    @NotNull
    private char year;

    @Column(length = 4)
    @NotNull
    private int semester;

    @Column(name = "mid_final", nullable = false, length = 4)
    @NotNull
    private int midFinal;

    @Column(length = 4)
    @NotNull
    private int score;

    @Column(length = 1)
    @NotNull
    private int rating;

    @Column(name = "class_rank", nullable = false, length = 4)
    @NotNull
    private int classRank;

    @Column(name = "whole_rank", nullable = false, length = 11)
    @NotNull
    private Long wholeRank;
}
