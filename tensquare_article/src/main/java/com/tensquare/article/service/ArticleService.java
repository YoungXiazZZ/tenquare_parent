package com.tensquare.article.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    //文章审核
    public void examine(String articleId) {
        articleDao.examine(articleId);
    }

    @Transactional
    //点赞
    public void updateThumbup(String articleId) {
        articleDao.dianzan(articleId);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public Article findById(String id) {
        //先去缓存中获取数据
        Article article = (Article) redisTemplate.opsForValue().get("article" + id);
        if (article == null && !"".equals(article)) {
            article = articleDao.findById(id).get();
            //重新放入数据库
            //redisTemplate.opsForValue().set("article" + id, article);
            redisTemplate.opsForValue().set("article_" + id, article, 100, TimeUnit.SECONDS);
            // redisTemplate.boundValueOps("xx").set("xx");
        }
        return article;
    }

    /**
     * 增加
     *
     * @param article
     */
    public void add(Article article) {
        article.setId(idWorker.nextId() + "");
        articleDao.save(article);
    }

    /**
     * 修改
     *
     * @param article
     */
    public void update(Article article) {
        articleDao.save(article);
        redisTemplate.delete("article" + article.getId());//删除缓存
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        articleDao.deleteById(id);
        redisTemplate.delete("article" + id);
    }

}
