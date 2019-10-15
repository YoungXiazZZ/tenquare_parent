package com.tensquare.qa.controller;

import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    ProblemService problemService;

    @Autowired
    private LabelClient labelClient;

    @RequestMapping(value = "/label/{labelid}")
    public Result findLabelById(@PathVariable String labelid) {
        Result result = labelClient.findById(labelid);
        return result;
    }


    @RequestMapping(value = "/newlist/{label}/{page}/{size}", method = RequestMethod.GET)
    public Result newlist(@PathVariable Integer label, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problemPage = problemService.newReply(label, page, size);
        return new Result(true, StatusCode.OK, "最新回答列表", new PageResult<Problem>(problemPage.getTotalElements(), problemPage.getContent()));
    }

    @RequestMapping(value = "/hotlist/{label}/{page}/{size}", method = RequestMethod.GET)
    public Result hotlist(@PathVariable Integer label, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problems = problemService.hotReply(label, page, size);
        return new Result(true, StatusCode.OK, "热门列表", new PageResult<Problem>(problems.getTotalElements(), problems.getContent()));
    }

    @RequestMapping(value = "/waitlist/{label}/{page}/{size}", method = RequestMethod.GET)
    public Result waitlist(@PathVariable Integer label, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Problem> problems = problemService.waitReply(label, page, size);
        return new Result(true, StatusCode.OK, "等待回答", new PageResult<Problem>(problems.getTotalElements(), problems.getContent()));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        //发布问题之前验证权限
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "无权发布");
        }
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

}
