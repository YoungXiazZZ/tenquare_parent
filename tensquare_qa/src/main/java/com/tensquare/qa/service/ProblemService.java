package com.tensquare.qa.service;

import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class ProblemService {

    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private IdWorker idWorker;


    public Page<Problem> newReply(Integer labelid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return problemDao.newreply(labelid, pageable);
    }

    public Page<Problem> hotReply(Integer labelId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return problemDao.hotrely(labelId, pageable);

    }

    public Page<Problem> waitReply(Integer labelId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return problemDao.waitlist(labelId, pageable);
    }

    /**
     * 增加
     *
     * @param problem
     */
    public void add(Problem problem) {
        problem.setId(idWorker.nextId() + "");
        problemDao.save(problem);
    }
}