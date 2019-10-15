package com.tensquare.gathering.service;

import com.tensquare.gathering.dao.GatheringDao;
import com.tensquare.gathering.pojo.Gathering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class GatheringService {

    @Autowired
    GatheringDao gatheringDao;

    @Autowired
    IdWorker idWorker;

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    @Cacheable(value="gathering",key="#id")
    public Gathering findById(String id) {
        return gatheringDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param gathering
     */
    public void add(Gathering gathering) {
        gathering.setId(idWorker.nextId() + "");
        gatheringDao.save(gathering);
    }

    /**
     * 修改
     *
     * @param gathering
     */
    public void update(Gathering gathering) {
        gatheringDao.save(gathering);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        gatheringDao.deleteById(id);
    }
}
