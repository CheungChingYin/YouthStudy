package cn.cheungchingyin.ysm;

import cn.cheungchingyin.ysm.entity.PropertiesInfo;
import cn.cheungchingyin.ysm.entity.Student;
import cn.cheungchingyin.ysm.entity.StudyHourTable;
import cn.cheungchingyin.ysm.utils.StringUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @Author 张正贤
 * @Date 2023/4/8 16:41
 * @Version 1.0
 */
@Slf4j
public class Main {


    public static void main(String[] args) {
        InputStream propertiesInputStream;
        Properties properties = new Properties();
        log.info("------开始生成学时表Excel------");
        try {
            // 获取所在jar包同一级文件夹内的application.properties
            File config = new File("application.properties");
            // 存在则使用自定义配置文件
            if (config.exists()) {
                log.info("------使用自定义的application.properties,路径为{}------", config.getAbsolutePath());
                propertiesInputStream = new BufferedInputStream(Files.newInputStream(config.toPath()));
            } else {
                propertiesInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties");
                log.info("------使用系统默认的的application.properties,路径为{}------", config.getAbsolutePath());
            }
            // 读取配置文件
            properties.load(new BufferedReader(new InputStreamReader(propertiesInputStream, StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("读取配置文件失败");
            return;
        }
        // 用于配置到实体类的转换
        JSONObject propertiesJson = new JSONObject();
        Enumeration<?> enumeration = properties.propertyNames();
        // 遍历配置文件内容
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            propertiesJson.put(key, properties.getProperty(key, null));
        }
        // 学时表的配置
        StudyHourTable studyHourTableConfig = JSONObject.toJavaObject(propertiesJson, StudyHourTable.class);
        // 学生信息和参与大学习名单的配置
        PropertiesInfo propertiesInfo = JSONObject.toJavaObject(propertiesJson, PropertiesInfo.class);
        String studentInfoExcelPath = propertiesInfo.getStudentInfoExcelPath();
        if (StringUtil.isBlank(studentInfoExcelPath)) {
            log.error("studentInfoExcelPath不能为空");
            return;
        }
        // 学生信息列表
        List<Map<Integer, String>> studentInfoList;
        List<Map<Integer, String>> collectList;
        try {
            // 读取学生信息Excel
            studentInfoList = EasyExcel.read(new FileInputStream(studentInfoExcelPath))
                    .sheet().headRowNumber(propertiesInfo.getStudentInfoBeginRow()).doReadSync();
        } catch (FileNotFoundException e) {
            log.error("读取学生信息Excel异常，result={}", e.getMessage());
            throw new RuntimeException(e);
        }
        // 学生学号为主键的map
        Map<String, Student> studentNumberMap = new HashMap<>(10);
        // 学生名称为主键的map
        Map<String, Student> studentNameMap = new HashMap<>(10);
        // 名字是否重复
        AtomicBoolean isRepeated = new AtomicBoolean(false);
        if (studentInfoList != null) {
            // 遍历学生信息，装入到两个map中
            studentInfoList.forEach(studentInfo -> {
                Student student = new Student();
                student.setStudentNumber(studentInfo.get(propertiesInfo.getStudentInfoStudentNumberCol()));
                student.setName(studentInfo.get(propertiesInfo.getStudentInfoStudentNameCol()));
                studentNumberMap.put(student.getStudentNumber(), student);
                // 名字重复，则不在进行根据名称进行校验
                if (studentNameMap.containsKey(student.getName())) {
                    isRepeated.set(true);
                }
                studentNameMap.put(student.getName(), student);
            });
        }
        // 收集大学习名单的Excel路径
        String collectExcelPath = propertiesInfo.getCollectExcelPath();
        if (StringUtil.isBlank(collectExcelPath)) {
            log.error("collectExcelPath不能为空");
            return;
        }
        try {
            // 读取收集大学习Excel的内容
            collectList = EasyExcel.read(new FileInputStream(collectExcelPath))
                    .sheet().headRowNumber(propertiesInfo.getCollectExcelBeginRow()).doReadSync();
        } catch (FileNotFoundException e) {
            log.error("读取收集Excel异常，result={}", e.getMessage());
            throw new RuntimeException(e);
        }
        // 学时表学生信息名单
        List<StudyHourTable> studyHourTableList = new ArrayList<>();
        if (collectList != null) {
            collectList.forEach(collectInfo -> {
                String studentNumber = collectInfo.get(propertiesInfo.getCollectExcelStudentNumberCol());
                String studentName = collectInfo.get(propertiesInfo.getCollectExcelStudentNameCol());
                // 优先根据学生学号进行匹配
                if (studentNumberMap.containsKey(studentNumber)) {
                    StudyHourTable tempStudentHourTable = new StudyHourTable();
                    BeanUtil.copyProperties(studyHourTableConfig, tempStudentHourTable);
                    tempStudentHourTable.setStudentNumber(studentNumberMap.get(studentNumber).getStudentNumber());
                    tempStudentHourTable.setName(studentNumberMap.get(studentNumber).getName());
                    studyHourTableList.add(tempStudentHourTable);
                } else if (!(isRepeated.get()) && studentNameMap.containsKey(studentName)) {
                    // 如果当前班级学生名称没有重复的情况下，且根据学生名字匹配到信息
                    StudyHourTable tempStudentHourTable = new StudyHourTable();
                    BeanUtil.copyProperties(studyHourTableConfig, tempStudentHourTable);
                    tempStudentHourTable.setStudentNumber(studentNameMap.get(studentNumber).getStudentNumber());
                    tempStudentHourTable.setName(studentNameMap.get(studentNumber).getName());
                    studyHourTableList.add(tempStudentHourTable);
                } else {
                    log.error("找不到相应的学学生信息,collectInfo = {}", collectInfo);
                }
            });
        }
        // 根据学号进行排序
        List<StudyHourTable> studyHourTableSortList = studyHourTableList.stream()
                .sorted(Comparator.comparing(StudyHourTable::getStudentNumber)).collect(Collectors.toList());
        // 获取配置中导出Excel的地址（不含文件名）
        String exportPath = propertiesInfo.getExportExcelPath();
        // 如果没有配置导出Excel地址，则默认在Jar包的同级目录生成Excel
        if (StringUtil.isBlank(exportPath)) {
            exportPath = new File("").getAbsolutePath();
        }
        // 生成Excel的文件名称
        String fileName = DateUtil.format(new Date(), "YYYY.MM.dd") + "-" + studyHourTableConfig.getClassName()
                + "-" + studyHourTableConfig.getActivityName() + ".xls";
        // 获取resource下的模板Excel的输入流
        InputStream srcInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("广州商学院“4+X”活动学时认定登记表(2022版).xls");
        log.info("------获取模板Excel成功------");
        // 创建临时文件，用于输出模板Excel流（由于在Jar包下无法直接获取文件，所以只能通过生成临时文件的方法实现）
        File tempFile = FileUtil.createTempFile();
        File srcFile = FileUtil.writeFromStream(srcInputStream, tempFile);
        log.info("------生成临时Excel成功------");
        // 获取配置中导出Excel的地址（含文件名）
        String finalExportPath = exportPath + "\\" + fileName;
        log.info("导出到：{}", finalExportPath);
        File distFile = new File(finalExportPath);
        // 复制模板文件到导出Excel的目的目录
        FileUtil.copy(srcFile, distFile, true);
        // 对导出Excel进行数据写入
        EasyExcel.write(distFile, StudyHourTable.class).withTemplate(srcFile).sheet(0).doFill(studyHourTableSortList);
        log.info("------学时表已生成于{}------", finalExportPath);
    }
}
