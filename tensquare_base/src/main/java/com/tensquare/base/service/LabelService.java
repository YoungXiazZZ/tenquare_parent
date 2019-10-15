package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签业务逻辑类
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部标签 * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据ID查询标签 * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 增加标签
     *
     * @param label
     */
    public void add(Label label) {
        label.setId(idWorker.nextId() + "");//设置ID
        labelDao.save(label);
    }

    /**
     * 修改标签
     *
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 删除标签 * @param id
     */
    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //new一个list集合。来存放所有的条件
                List<Predicate> list = new ArrayList<>();

                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {

                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" +
                            label.getLabelname() + "%"); //labelname like "%小明%"
                    list.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate predicate = criteriaBuilder.like(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                //new一个数组作为最终返回条件值
                Predicate[] parr = new Predicate[list.size()];
                //把list直接转成数组
                list.toArray(parr);

                return criteriaBuilder.and(parr);
            }
        });
    }


    /**
     * 构建查询条件
     *
     * @param searchMap * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?>
                    criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                if (searchMap.get("labelname") != null &&
                        !"".equals(searchMap.get("labelname"))) {
                    predicateList.add(cb.like(
                            root.get("labelname").as(String.class), "%" +
                                    (String) searchMap.get("labelname") + "%"));
                }
                if (searchMap.get("state") != null &&
                        !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.equal(
                            root.get("state").as(String.class), (String) searchMap.get("state")));
                }
                if (searchMap.get("recommend") != null &&
                        !"".equals(searchMap.get("recommend"))) {
                    predicateList.add(cb.equal(
                            root.get("recommend").as(String.class),
                            (String) searchMap.get("recommend")));
                }
                return cb.and(predicateList.toArray(new
                        Predicate[predicateList.size()]));
            }
        };
    }


    /**
     * 分页条件查询
     *
     * @param searchMap * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Map searchMap, int page, int size) {
        Specification specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification, pageRequest);
    }
}