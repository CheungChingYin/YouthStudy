package cn.cheungchingyin.ysm.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author 张正贤
 * @Date 2023/4/8 17:11
 * @Version 1.0
 */
@Data
public class StudyHourTable {

    /**
     * 活动名称
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","活动名称"})
    private String activityName;

    /**
     * 主办方
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","主办方"})
    private String organizer;

    /**
     * 姓名
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","姓名"})
    private String name;

    /**
     * 学号
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","学号"})
    private String studentNumber;

    /**
     * 学院
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","学院"})
    private String collegeName;

    /**
     * 班级
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","班级"})
    private String className;

    /**
     * 参加类型
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","参加类型"})
    private String participateType;

    /**
     * 获奖情况
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","获奖情况"})
    private String awardSituation;

    /**
     * 认定项目
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","认定项目"})
    private String identifyEvent;

    /**
     * 认定活动时
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","认定活动时"})
    private String identifyHours;

    /**
     * 填报人及联系方式
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","填报人及联系方式"})
    private String informant;

    /**
     * 审核人
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","审核人"})
    private String reviewer;

    /**
     * 备注
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","备注"})
    private String remark;

    /**
     * 归属年度
     */
    @ExcelProperty({"广州商学院“4+X”活动学时认定登记表","归属年度(如“2021-2022学年”)"})
    private String ownershipYear;


}
