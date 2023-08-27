package com.green.secondproject.admin.teachermng;

import com.green.secondproject.admin.teachermng.model.TeacherMngVo;
import com.green.secondproject.admin.teachermng.model.TeacherMngWithPicVo;
import com.green.secondproject.common.config.etc.EnrollState;
import com.green.secondproject.common.config.exception.MyErrorResponse;
import com.green.secondproject.common.config.security.model.RoleType;
import com.green.secondproject.common.entity.SchoolAdminEntity;
import com.green.secondproject.common.entity.SchoolEntity;
import com.green.secondproject.common.entity.UserEntity;
import com.green.secondproject.common.entity.VanEntity;
import com.green.secondproject.common.repository.SchoolAdminRepository;
import com.green.secondproject.common.repository.SchoolRepository;
import com.green.secondproject.common.repository.UserRepository;
import com.green.secondproject.common.repository.VanRepository;
import com.green.secondproject.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Slf4j
@Service
public class TeacherMngService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolRepository scRep;
    @Autowired
    VanRepository vanRep;

    @Autowired
    SchoolAdminRepository scAdminRep;

    @Value("${file.aprimgPath}")
    private String aprimgPath;

    private String schoolCode;

    public List<TeacherMngVo> teacherNotapprovedList(Long schoolId) {
        schoolCode = scRep.findBySchoolId(schoolId).getCode();
        SchoolEntity scEnti = scRep.findByCode(schoolCode);//학교 코드로 학교 entity 가져오기
        List<VanEntity> vanEnti = vanRep.findDistinctBySchoolEntity(scEnti);

        List<UserEntity> tcList = userRepository.findUsersByConditions(vanEnti, RoleType.TC, 0, EnrollState.ENROLL);

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
        } //->람돠도 가넝 ?

        return finalResult;
    }
//
//    public Page<UserEntity> teacherNotapprovedList2(Long schoolId, Pageable page) {
//        SchoolEntity scEnti = scRep.findByCode(schoolId.toString());//학교 코드로 학교 entity 가져오기
//        List<VanEntity> vanEnti = vanRep.findDistinctBySchoolEntity(scEnti);
//        //school entity 로 반 entity 리스트 가져오기
////        page = page.
//        //(학교코드 ->반코드)   ->>RoleType : TC / apr_Yn =0 / enrollstate = enroll인 애들
////        List<UserEntity> tcList =  userRepository.findAllByVanEntityInAndRoleTypeAndAprYnAndEnrollState(vanEnti,RoleType.TC, 0, EnrollState.ENROLL);
//        Page<UserEntity> tcList = userRepository.findUsersByConditions2(vanEnti, RoleType.TC, 0, EnrollState.ENROLL, page);
//
//
////        for(UserEntity en : tcList){
////            finalResult.add(TeacherMngVo.builder()
////                    .userId(en.getUserId())
////                    .classId(en.getVanEntity().getVanId())
////                    .email(en.getEmail())
////                    .nm(en.getNm())
////                    .birth(en.getBirth())
////                    .phone(en.getPhone())
////                    .address(en.getAddress())
////                    .detailAddr(en.getDetailAddr())
////                    .role(en.getRoleType().toString())
////                    .aprYn(en.getAprYn())
////                    .enrollState(en.getEnrollState()).build());
////        } //->람돠도 가넝 ?
//
//        return tcList;
//
////
////        TeacherMngParam param = TeacherMngParam.builder().roleType(RoleType.TC).aprYn(0).enrollState(EnrollState.ENROLL).vanEntityList(vanEnti).build();
////        List<UserEntity> tcList = userRepository.findAllByRoleTypeAndAprYnAndVanEntityAndEnrollState(param);
////        List<UserEntity> tcList = userRepository.findAllByRoleTypeAndAprYnAndEnrollState(param);
//    }


//잘 작동되는 친구^^
    public TeacherMngWithPicVo teacherDetailNotApr(Long userId){
        UserEntity userEnti = userRepository.findByUserId(userId);

              SchoolEntity scEnti = scRep.findByCode(userEnti.getVanEntity().getVanId().toString());

//        UserEntity userEnti = userRepository.findByUserId(userId);


        String aprPicPath =  aprimgPath + userId + userEnti.getAprPic();

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



    public TeacherMngWithPicVo teacherDetailNotApr2(Long signedinId, Long userId){
        //진정한 학교관리자인지 확인한다!
        SchoolAdminEntity sdAdminEnti = scAdminRep.getReferenceById(signedinId);
        UserEntity userEnti = userRepository.findByUserId(userId);

        //로그인한 관리자의 소속학급과 조회대상유저의 아이디가 일치한다면

        if(sdAdminEnti.getSchoolEntity().getSchoolId()
                .equals(userEnti.getVanEntity().getSchoolEntity().getSchoolId())){

                String aprPicPath =  aprimgPath + userId + userEnti.getAprPic();
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
        else{
            return null;
        }

//        UserEntity userEnti = userRepository.findByUserId(userId);






    }
}


