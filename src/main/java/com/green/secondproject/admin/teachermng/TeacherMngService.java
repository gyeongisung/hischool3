package com.green.secondproject.admin.teachermng;

import com.green.secondproject.admin.teachermng.model.TeacherMngVo;
import com.green.secondproject.admin.teachermng.model.TeacherMngVoContainer;
import com.green.secondproject.admin.teachermng.model.TeacherMngWithPicVo;
import com.green.secondproject.common.config.etc.EnrollState;
import com.green.secondproject.common.config.security.AuthenticationFacade;
import com.green.secondproject.common.config.security.model.RoleType;
import com.green.secondproject.common.entity.SchoolAdminEntity;
import com.green.secondproject.common.entity.SchoolEntity;
import com.green.secondproject.common.entity.UserEntity;
import com.green.secondproject.common.entity.VanEntity;
import com.green.secondproject.common.repository.SchoolAdminRepository;
import com.green.secondproject.common.repository.SchoolRepository;
import com.green.secondproject.common.repository.UserRepository;
import com.green.secondproject.common.repository.VanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherMngService {


    private final UserRepository userRepository;
    private final SchoolRepository scRep;
    private final VanRepository vanRep;
    private final AuthenticationFacade facade;
    private final SchoolAdminRepository scAdminRep;


    @Value("${file.aprimgPath}")
    private String aprimgPath;


    public TeacherMngVoContainer teacherNotapprovedList( Pageable pageable) {
      SchoolEntity scEnti = scRep.findBySchoolId(facade.getLoginUser().getSchoolId());
        List<VanEntity> vanEnti = vanRep.findDistinctBySchoolEntity(scEnti);
        pageable = PageRequest.of(pageable.getPageNumber()-1,  16);
        Page<UserEntity> tcList = userRepository.findUsersByConditions(vanEnti, RoleType.TC, 0, EnrollState.ENROLL, pageable);

        List<TeacherMngVo> finalResult = new ArrayList<>();

        for (UserEntity en : tcList) {
            VanEntity vanEntity = vanRep.findByVanId(en.getVanEntity().getVanId());
            finalResult.add(TeacherMngVo.builder()
                    .userId(en.getUserId())
                    .schoolNm(scEnti.getNm())
                    .grade(vanEntity.getGrade())
                    .vanNum(vanEntity.getClassNum())
                    .email(en.getEmail())
                    .nm(en.getNm())
                    .birth(en.getBirth())
                    .phone(en.getPhone())
                    .address(en.getAddress())
                    .detailAddr(en.getDetailAddr())
                    .role(en.getRoleType().toString())
                    .aprYn(en.getAprYn())
                    .enrollState(en.getEnrollState()).build());
        }
        return  TeacherMngVoContainer.builder()
                .list(finalResult)
                .totalCount((int)tcList.getTotalElements())
                .totalPage(tcList.getTotalPages()).build();
    }


    public TeacherMngVoContainer teacherListOfTheSchool(Pageable pageable,String search) {

        Optional<SchoolEntity> scEntiOpt = scRep.findById(facade.getLoginUser().getSchoolId());//학교 코드로 학교 entity 가져오기
        if (scEntiOpt.isEmpty()) {
            throw new RuntimeException("관리자 로그인 필요");
        }
        SchoolEntity scEnti = scEntiOpt.get();
        pageable = PageRequest.of(pageable.getPageNumber()-1,  16);

        List<VanEntity> vanEnti = vanRep.findDistinctBySchoolEntity(scEnti);

        List<TeacherMngVo> subResult = new ArrayList<>();

        if(search==null) {
            Page<UserEntity> tcList = userRepository.findUsersByVanEntityAndRoleType(vanEnti, RoleType.TC, pageable);
            for (UserEntity en : tcList) {
                VanEntity vanEntity = vanRep.findByVanId(en.getVanEntity().getVanId());
                subResult.add(TeacherMngVo.builder()
                        .userId(en.getUserId())
                        .schoolNm(scEnti.getNm())
                        .grade(vanEntity.getGrade())
                        .vanNum(vanEntity.getClassNum())
                        .email(en.getEmail())
                        .nm(en.getNm())
                        .birth(en.getBirth())
                        .phone(en.getPhone())
                        .address(en.getAddress())
                        .detailAddr(en.getDetailAddr())
                        .role(en.getRoleType().toString())
                        .aprYn(en.getAprYn())
                        .enrollState(en.getEnrollState())
                        .build());
            }

            return TeacherMngVoContainer.builder()
                    .list(subResult)
                    .totalCount((int) tcList.getTotalElements())
                    .totalPage(tcList.getTotalPages()).build();
        }

        else{
            Page<UserEntity> tcResearchList = userRepository.findByNmContainingAndVanEntityInAndRoleType(search,vanEnti,RoleType.TC,pageable);
                for (UserEntity en : tcResearchList) {
                    VanEntity vanEntity = vanRep.findByVanId(en.getVanEntity().getVanId());
                    subResult.add(TeacherMngVo.builder()
                            .userId(en.getUserId())
                            .schoolNm(scEnti.getNm())
                            .grade(vanEntity.getGrade())
                            .vanNum(vanEntity.getClassNum())
                            .email(en.getEmail())
                            .nm(en.getNm())
                            .birth(en.getBirth())
                            .phone(en.getPhone())
                            .address(en.getAddress())
                            .detailAddr(en.getDetailAddr())
                            .role(en.getRoleType().toString())
                            .aprYn(en.getAprYn())
                            .enrollState(en.getEnrollState())
                            .build());
                }

                return TeacherMngVoContainer.builder()
                        .list(subResult)
                        .totalCount((int) tcResearchList.getTotalElements())
                        .totalPage(tcResearchList.getTotalPages()).build();
            }
    }


//잘 작동되는 친구^^
    public TeacherMngWithPicVo teacherDetailNotApr(Long userId){
        UserEntity userEnti = userRepository.findByUserId(userId);

              SchoolEntity scEnti = scRep.findByCode(userEnti.getVanEntity().getVanId().toString());

//        UserEntity userEnti = userRepository.findByUserId(userId);


        String aprPicPath =  aprimgPath +"/" + userId + "/"+ userEnti.getAprPic();

        return TeacherMngWithPicVo.builder()
                .aprPic(aprPicPath)
                .userId(userEnti.getUserId())
                .schoolNm(userEnti.getVanEntity().getSchoolEntity().getNm())
                .grade(userEnti.getVanEntity().getGrade())
                .vanNum(userEnti.getVanEntity().getClassNum())
                .email(userEnti.getEmail())
                .nm(userEnti.getNm())
                .birth(userEnti.getBirth())
                .phone(userEnti.getPhone())
                .address(userEnti.getAddress())
                .detailAddr(userEnti.getDetailAddr())
                .role(userEnti.getRoleType().toString())
                .aprYn(userEnti.getAprYn())
                .enrollState(userEnti.getEnrollState())
                .build();

    }

        public String teacherAprv(Long teacherId,Long schoolId) {
            UserEntity getUserInfo = userRepository.findByUserId(teacherId);
            //선생님 확인과정
            if (getUserInfo == null) {
                return "유저 정보가 존재하지 않습니다";
            }

            if (getUserInfo.getVanEntity().getSchoolEntity().getSchoolId() == schoolId) {
                if (getUserInfo.getRoleType().equals(RoleType.TC)) {
                    if (getUserInfo.getAprYn() == 0) {
                        getUserInfo.setAprYn(1);
                        userRepository.save(getUserInfo);
                        return "승인처리되었습니다";
                    } else {
                        return "aprYn : " + getUserInfo.getAprYn();
                    }
                } else {
                    return "올바른 요청이 아닙니다 : " + getUserInfo.getRoleType();
                }
            } else {
                return "권한이없는 유저에 대한 요청";
            }

//EntityManager em = entityManagerFactory.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        tx.begin(); //트랜잭션 시작
//        Pay pay = em.find(Pay.class, id);
//        pay.changeTradeNo(tradeNo); // 엔티티만 변경
//        tx.commit(); //트랜잭션 커밋
        }




}


