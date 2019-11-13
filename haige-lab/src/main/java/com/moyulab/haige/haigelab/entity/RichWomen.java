package com.moyulab.haige.haigelab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moyulab.haige.haigelab.enums.SexEnum;
import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_rich_women")
public class RichWomen {

    @TableId(value = "ID",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "NAME",exist = true)
    private String name;
    @TableField(value = "SEX",exist = true)
    private String sex;
    @TableField(value = "ADDRESS",exist = true)
    private String address;
    @TableField(value = "PHONE",exist = true)
    private String phone;
    @TableField(value = "WEIGHT",exist = true)
    private String weight;
    @TableField(value = "TALL",exist = true)
    private String tall;
    @TableField(value = "level",exist = true)
    private Integer level;
    @TableField(value = "create_date",exist = true)
    private Date createDate;



}
