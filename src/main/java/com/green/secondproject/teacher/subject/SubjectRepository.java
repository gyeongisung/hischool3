package com.green.secondproject.teacher.subject;

import com.green.secondproject.common.entity.SbjCategoryEntity;
import com.green.secondproject.common.entity.SubjectEntity;
import com.green.secondproject.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findBySbjCategoryEntity(SbjCategoryEntity entity);
}
