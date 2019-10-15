package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽数据访问层
 *
 * @author Administrator *
 */
public interface SpitDao extends MongoRepository<Spit, String> {
}
