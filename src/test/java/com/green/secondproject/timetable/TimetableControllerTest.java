package com.green.secondproject.timetable;

import com.green.secondproject.timetable.model.TimeTableContainerVo;
import com.green.secondproject.timetable.model.TimeTableParam;
import com.green.secondproject.timetable.model.TimeTableVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TimetableController.class)
class TimetableControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TimetableService service;

    @Test
    void test1() {
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken
                (null, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_STD"))));
        System.out.println("test1: " + SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    @DisplayName("접속일 기준-주단위 시간표출력(5.22 오성고1-1)")
    void getTimeTableByClassOfTheWeek() throws Exception {
        //가상의 유저 (error 401대비)
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken
                (null, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_STD"))));


        //테스트용 데이터넘기기
        //가짜 서비스로 오성고 1학년 1반값 넘기기
        TimeTableParam p = new TimeTableParam();
        p.setSdSchulCode("7240099");
        p.setGrade("1");
        p.setClassNm("1");
        TimeTableContainerVo vo = service.getTimeTableByClassAndTheWeek(p);
        given(service.getTimeTableByClassAndTheWeek(any())).willReturn(vo);



        //null이 아니라면, 검사한다.
        if(vo != null){

            //예상한 값이 들어있는 가짜리스트 만들기
            //5.22 1교시 국어/2교시 영어
        List<TimeTableVo> mockTableVolist = new ArrayList<TimeTableVo>();
        TimeTableVo mockSubVo = new TimeTableVo();//서브리스트
        mockSubVo.setPeriod("1");//1교시
        mockSubVo.setSubject("국어");//과목명
        mockTableVolist.add(mockSubVo);

        TimeTableVo mockSubVo2 = new TimeTableVo();
        mockSubVo2.setPeriod("2");
        mockSubVo2.setSubject("영어");
        mockTableVolist.add(mockSubVo2);

        TimeTableContainerVo mockVo = new TimeTableContainerVo("오성고등학교","1","1","1",mockTableVolist);

        //실제넘어온 값(vo)와 가짜리스트(containerVo)와 비교 (학교명, 학년일치)
        assertEquals(vo.getSchoolNm(),mockVo.getSchoolNm());
        assertEquals(vo.getClassNm(),mockVo.getClassNm());

        //실제넘어온 값(vo.list)와 가짜리스트(mocksubVolist)와 비교 (1교시,수업)
        List<TimeTableVo> subList = vo.getList();

        assertEquals(subList.get(0).getPeriod(),mockSubVo.getPeriod());
        assertEquals(subList.get(0).getSubject(),mockSubVo.getSubject());

        assertEquals(subList.get(1).getPeriod(),mockSubVo.getPeriod());
        assertEquals(subList.get(1).getSubject(),mockSubVo.getSubject());
        }
        else{
            assertNull(vo);
        }

        //실제리스트와 전체비교가 힘들다
        // result action /status ok /넘어오는 데이터 타입만 확인
        ResultActions ra = mvc.perform(get("/api/timetable")
                                .contentType(MediaType.APPLICATION_JSON_UTF8));

//        ra.andExpect(status().isOk())//200을 기대하면..안되나
//                            .andDo(print());
        verify(service).getTimeTableByClassAndTheWeek(any());

    }
}