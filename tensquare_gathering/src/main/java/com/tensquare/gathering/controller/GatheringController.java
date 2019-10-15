package com.tensquare.gathering.controller;


import com.tensquare.gathering.service.GatheringService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/gathering")
public class GatheringController {
    @Autowired
    private GatheringService gatheringService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", gatheringService.findById(id));
    }
}
