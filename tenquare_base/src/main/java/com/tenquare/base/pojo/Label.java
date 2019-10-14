package com.tenquare.base.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author xiayang
 * @date 2019/9/28
 */
@Entity
@Table(name = "tb_label")
@Data
public class Label implements Serializable{
    @Id
    private String id;

    /**
     * 标签名称
     */
    @Column(name = "labelname")
    private String labelName;

    /**
     * 状态
     */
    private String state;

    /**
     * 使用数量
     */
    private String count;

    /**
     * 关注数
     */
    private String fans;

    /**
     * 是否推荐
     */
    private String recommend;

}
