package com.green.secondproject.awards;

import com.green.secondproject.awards.model.AwardsInsDto;
import com.green.secondproject.awards.model.AwardsVo;
import com.green.secondproject.common.entity.AwardsEntity;
import com.green.secondproject.volunteerwork.model.VolWorkInsDto;
import com.green.secondproject.volunteerwork.model.VolWorkVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/awards")
@RequiredArgsConstructor
@Tag(name = "수상경력")
public class AwardsController {
    private final AwardsService service;



    @PostMapping
    @Operation(summary = "학생별 수상경력 입력"
            , description = """
                    입력값 :<br> (1)userId : 입력대상학생pk <br>(2)grade : 입력대상학년 <br>
                    (3)startDate : 봉사활동시작일 <br>(4)endDate : 봉사활동종료일 <br>(5)place : 봉사장소 <br>
                    (6)ctnt : 봉사내용 <br>(6)hrs : 봉사시간 <br>(8)totalHrs : 총 봉사시간(학년단위) <br><br><br>
                    출력값 : 입력요구값과 동일하나 정상반영시 volunteerId가 추가됩니다
                    volunteerId : 봉사활동 pk
                    """)
    public ResponseEntity<AwardsVo> postAward(@RequestBody AwardsInsDto dto){
        AwardsVo vo = service.postAward(dto);
        return ResponseEntity.ok(vo);
    }
}
