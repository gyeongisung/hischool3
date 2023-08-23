package com.green.secondproject.student;

import com.green.secondproject.common.entity.AcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AcaRepository extends JpaRepository<AcaEntity, Long> {

}
