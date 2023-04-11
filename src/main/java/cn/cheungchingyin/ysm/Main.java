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

//        String configPath = System.getProperty("configPath", "");
        log.info("------开始生成学时表Excel------");

        try {
            File config = new File("application.properties");
            if (config.exists()) {
                log.info("------使用自定义的application.properties,路径为{}------", config.getAbsolutePath());
                propertiesInputStream = new BufferedInputStream(Files.newInputStream(config.toPath()));
            } else {
                propertiesInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties");
                log.info("------使用系统默认的的application.properties,路径为{}------", config.getAbsolutePath());
            }
            properties.load(new BufferedReader(new InputStreamReader(propertiesInputStream, StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("读取配置文件失败");
            return;
        }
        JSONObject propertiesJson = new JSONObject();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            propertiesJson.put(key, properties.getProperty(key, null));
        }
        StudyHourTable studyHourTableConfig = JSONObject.toJavaObject(propertiesJson, StudyHourTable.class);
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
            studentInfoList = EasyExcel.read(new FileInputStream(studentInfoExcelPath))
                    .sheet().headRowNumber(propertiesInfo.getStudentInfoBeginRow()).doReadSync();
        } catch (FileNotFoundException e) {
            log.error("读取学生信息Excel异常，result={}", e.getMessage());
            throw new RuntimeException(e);
        }
        Map<String, Student> studentNumberMap = new HashMap<>(10);
        Map<String, Student> studentNameMap = new HashMap<>(10);
        AtomicBoolean isRepeated = new AtomicBoolean(false);
        if (studentInfoList != null) {
            studentInfoList.forEach(studentInfo -> {
                Student student = new Student();
                student.setStudentNumber(studentInfo.get(propertiesInfo.getStudentInfoStudentNumberCol()));
                student.setName(studentInfo.get(propertiesInfo.getStudentInfoStudentNameCol()));
                studentNumberMap.put(student.getStudentNumber(), student);
                // 名字重复
                if (studentNameMap.containsKey(student.getName())) {
                    isRepeated.set(true);
                }
                studentNameMap.put(student.getName(), student);
            });
        }
        String collectExcelPath = propertiesInfo.getCollectExcelPath();
        if (StringUtil.isBlank(collectExcelPath)) {
            log.error("collectExcelPath不能为空");
            return;
        }
        // 收集Excel信息
        try {
            collectList = EasyExcel.read(new FileInputStream(collectExcelPath))
                    .sheet().headRowNumber(propertiesInfo.getCollectExcelBeginRow()).doReadSync();
        } catch (FileNotFoundException e) {
            log.error("读取收集Excel异常，result={}", e.getMessage());
            throw new RuntimeException(e);
        }
        List<StudyHourTable> studyHourTableList = new ArrayList<>();
        if (collectList != null) {
            collectList.forEach(collectInfo -> {
                String studentNumber = collectInfo.get(propertiesInfo.getCollectExcelStudentNumberCol());
                String studentName = collectInfo.get(propertiesInfo.getCollectExcelStudentNameCol());
                if (studentNumberMap.containsKey(studentNumber)) {
                    StudyHourTable tempStudentHourTable = new StudyHourTable();
                    BeanUtil.copyProperties(studyHourTableConfig, tempStudentHourTable);
                    tempStudentHourTable.setStudentNumber(studentNumberMap.get(studentNumber).getStudentNumber());
                    tempStudentHourTable.setName(studentNumberMap.get(studentNumber).getName());
                    studyHourTableList.add(tempStudentHourTable);
                } else if (!(isRepeated.get()) && studentNameMap.containsKey(studentName)) {
                    StudyHourTable tempStudentHourTable = new StudyHourTable();
                    BeanUtil.copyProperties(studyHourTableConfig, tempStudentHourTable);
                    tempStudentHourTable.setStudentNumber(studentNumberMap.get(studentNumber).getStudentNumber());
                    tempStudentHourTable.setName(studentNumberMap.get(studentNumber).getName());
                    studyHourTableList.add(tempStudentHourTable);
                } else {
                    log.error("找不到相应的学学生信息,collectInfo = {}", collectInfo);
                }
            });
        }
        List<StudyHourTable> studyHourTableSortList = studyHourTableList.stream().sorted(Comparator.comparing(StudyHourTable::getStudentNumber)).collect(Collectors.toList());
        String exportPath = propertiesInfo.getExportExcelPath();
        if (StringUtil.isBlank(exportPath)) {
            exportPath = new File("").getAbsolutePath();
        }
        String fileName = DateUtil.format(new Date(), "YYYY.MM.dd") + "-" + studyHourTableConfig.getClassName()
                + "-" + studyHourTableConfig.getActivityName() + ".xls";
        InputStream srcInputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("广州商学院“4+X”活动学时认定登记表(2022版).xls");
        log.info("------获取模板Excel成功------");
        File tempFile = FileUtil.createTempFile();
        File srcFile = FileUtil.writeFromStream(srcInputStream, tempFile);
        log.info("------生成临时Excel成功------");
        String finalExportPath = exportPath +"\\"+ fileName;
        log.info("导出到：{}", finalExportPath);
        File distFile = new File(finalExportPath);
        FileUtil.copy(srcFile, distFile, true);
        EasyExcel.write(distFile, StudyHourTable.class).withTemplate(srcFile).sheet(0).doFill(studyHourTableSortList);
        log.info("------学时表已生成于{}------", finalExportPath);


    }
}
