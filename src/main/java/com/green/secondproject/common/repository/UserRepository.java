package com.green.secondproject.common.repository;

import com.green.secondproject.admin.teachermng.model.TeacherMngVo;
import com.green.secondproject.common.config.security.model.RoleType;
import com.green.secondproject.common.config.etc.EnrollState;
import com.green.secondproject.common.entity.UserEntity;
import com.green.secondproject.common.entity.VanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(Long userId);

    //규진작업
    long countByVanEntityInAndRoleTypeAndAprYn(List<VanEntity> vanEntity, RoleType roleType, int aprYn);


    //석경작업
//    List<UserEntity> findAllByRoleTypeAnd();

//    List<UserEntity> findAllByRoleTypeAndAprYnAndVanEntityAndEnrollState(RoleType roleType,int aprYn,List<VanEntity> vanEnti,EnrollState enrollState);
//    List<UserEntity> findAllByRoleTypeAndAprYnAndEnrollState(TeacherMngParam param);
//    List<UserEntity> findAllByVanEntityInAndRoleTypeAndAprYnAndEnrollState(List<VanEntity> vanEnti, RoleType roleType, int aprYn, EnrollState enrollState);

    @Query("SELECT u FROM UserEntity u WHERE u.vanEntity IN :vanEnti AND u.roleType = :roleType AND u.aprYn = :aprYn AND u.enrollState = :enrollState")
    Page<UserEntity> findUsersByConditions2(List<VanEntity> vanEnti, RoleType roleType, int aprYn, EnrollState enrollState,Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.vanEntity IN :vanEnti AND u.roleType = :roleType AND u.aprYn = :aprYn AND u.enrollState = :enrollState")
    List<UserEntity> findUsersByConditions(List<VanEntity> vanEnti, RoleType roleType, int aprYn, EnrollState enrollState);

//    Page<TeacherMngVo> findAll(Pageable page);
}
