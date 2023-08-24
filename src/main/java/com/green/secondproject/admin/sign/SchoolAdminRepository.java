package com.green.secondproject.admin.sign;

import com.green.secondproject.common.entity.SchoolAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolAdminRepository extends JpaRepository<SchoolAdminEntity, Long> {
    SchoolAdminEntity findByEmail(String email);
}
