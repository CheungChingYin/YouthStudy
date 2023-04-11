package cn.cheungchingyin.ysm.entity;

import lombok.Data;

/**
 * @Author 张正贤
 * @Date 2023/4/8 22:52
 * @Version 1.0
 */
@Data
public class PropertiesInfo {

    /**
     * 学生信息excel地址
     */
    private String studentInfoExcelPath;

    /**
     * 学生信息excel所在sheet页
     */
    private int studentInfoSheetPage;

    /**
     * 学生信息excel开始行数
     */
    private int studentInfoBeginRow;

    /**
     * 学生信息excel的学生学号列数
     */
    private int studentInfoStudentNumberCol;

    /**
     * 学生信息excel的学生姓名列数
     */
    private int studentInfoStudentNameCol;

    /**
     * 收集excel地址
     */
    private String collectExcelPath;

    /**
     * 收集excel所在sheet页
     */
    private int collectExcelSheetPage;

    /**
     * 收集excel开始行数
     */
    private int collectExcelBeginRow;

    /**
     * 收集excel的学生学号列数
     */
    private int collectExcelStudentNumberCol;

    /**
     * 收集excel的学生姓名列数
     */
    private int collectExcelStudentNameCol;

    /**
     * 导出Excel地址
     */
    private String exportExcelPath;

    /**
     * 学时表属性
     */
    private StudyHourTable studyHourTable;


}
