package com.tensquare.recruit.controller;

import com.tensquare.recruit.pojo.Recruit;
import com.tensquare.recruit.service.RecruitService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;


    @RequestMapping(value = "/search/recommend", method = RequestMethod.GET)
    public Result recuritlist() {
        List<Recruit> recruitlist = recruitService.recruitlist();
        return new Result(true, StatusCode.OK, "推荐职位", recruitlist);
    }


    @RequestMapping(value = "/search/newlist", method = RequestMethod.GET)
    public Result newrecurit() {
        List<Recruit> recruit = recruitService.findRecruit();
        return new Result(true, StatusCode.OK, "最新的职位查询", recruit);
    }
}
