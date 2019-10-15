package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findAll(@PathVariable Integer page, @PathVariable Integer size) {
        Page<Spit> spitPage = spitService.findAll(page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(spitPage.getTotalElements(), spitPage.getContent()));
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        return new Result(true, StatusCode.OK, "通过id查询成功", spitService.findById(spitId));
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit) {
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId) {
        return new Result(true, StatusCode.OK, "删除成功");
    }

    //根据上级id来查询
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result findParentId(@PathVariable String parentid, @PathVariable Integer page, Integer size) {
        Page<Spit> spitPage = spitService.findByParentId(parentid, page, size);
        return new Result(true, StatusCode.OK, "根据上级查询数据", new PageResult<Spit>(spitPage.getTotalElements(), spitPage.getContent()));

    }

    //吐槽点赞   //不能重复点赞
//    @RequestMapping(value = "/thumbup/{spitId}", method = RequestMethod.PUT)
//    public Result dianzan(@PathVariable String spitId) {
//        spitService.updateThumbup(spitId);
//        return new Result(true, StatusCode.OK, "点赞成功");
//    }

    /**
     * 吐槽点赞 * @param id * @return
     */
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id) {
        //判断用户是否点过赞
        String userid = "2023";// 后边我们会修改为当前登陆的用户
        if (redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id) != null) {
            return new Result(false, StatusCode.REPERROR, "你已经点过赞了");
        }
        spitService.updateThumbup(id);
        redisTemplate.opsForValue().set("thumbup_" + userid + "_" + id, "1");
        return new Result(true, StatusCode.OK, "点赞成功");
    }

}
