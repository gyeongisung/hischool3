package com.green.secondproject.notice;

import com.green.secondproject.common.entity.NoticeEntity;
import com.green.secondproject.notice.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ToString
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService service;

    @GetMapping
    @Operation(summary = "공지사항 리스트")
    public List<NoticeVo> noticeList(){
        return service.noticeList();
    }
//    @PostMapping
//    @Operation(summary = "공지사항 입력")
//    public

    @PostMapping
    @Operation(summary = "공지사항등록")
    public NoticeVo saveByNotice(@RequestBody NoticeInsDto dto){
        return service.saveByNotice(dto);
    }

    @GetMapping("/bynotice")
    @Operation(summary = "공지사항 하나 선택보기")
    public NoticeVo findBySchoolNotice(@RequestParam Long noticeId){
        return service.findBySchoolNotice(noticeId);
    }

    @PatchMapping
    @Transactional
    @Operation(summary = "글 수정")
    public NoticeVo2 no(NoticePatchDto dto){

        return service.upNotice(dto);
    }
    @DeleteMapping
    @Operation(summary = "글 삭제")
    public void del(@RequestParam Long noticeId){
        service.delNotice(noticeId);
    }

}
