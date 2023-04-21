# 1.简介
此项目用于广州商学院的青年大学习学时表制作，只要通过配置文件进行简单配置，即可根据班级学生信息Excel和大学习收集名单Excel自动生成相应的学时表。

### 1.1 环境要求
- JDK版本 >= 1.8

# 2.如何使用

注意:使用此工具需要先安装好JDK环境。

### 2.1 下载压缩安装包
通过[Release](https://github.com/CheungChingYin/YouthStudy/releases)下载最新的压缩包。

### 2.2 解压下载的压缩安装包
压缩包内容如下
![image](https://user-images.githubusercontent.com/20316736/231210326-b313bb3d-777a-468d-8704-d1602ec4d0d3.png)

### 2.3 配置`application.properties` 文件
在文件夹内，通过记事本打开`application.properties`文件
![image](https://user-images.githubusercontent.com/20316736/233511437-00f1093e-56fb-4e0d-bd57-06c7fac261e1.png)

`studentInfoExcelPath` 指学生信息Excel地址，路径分隔符使用 `\` 和 `/` 皆可，单使用 `\` 的时候必须要注意进行转义为 `\\`。

`studentInfoSheetPage` 指学生信息excel所在sheet页,注意0是指第一个表。

![image](https://user-images.githubusercontent.com/20316736/231212935-1aa77d1d-243e-4e17-b96c-9b06611275cc.png)

`studentInfoBeginRow` 指学生信息excel开始行数，注意行数是从0开始，例如学生信息表如下图所示

![image](https://user-images.githubusercontent.com/20316736/231214094-83548fbc-b0c5-49af-981e-e6516233effe.png)

由于图片中第一行是表头，第二行才是学生信息，故配置的时候使用为1，配置如图所示
![image](https://user-images.githubusercontent.com/20316736/231215532-2982c283-b49c-4e19-9d9a-e740a12f11d3.png)

`studentInfoStudentNumberCol` 指学生信息excel的学生学号列数，从0开始

例如上方的Excel截图中，学生学号位于第七列，故配置的时候应该为6

`studentInfoStudentNameCol`学生信息excel的学生姓名列数，从`0`开始

例如上方的Excel截图中，学生姓名位于第六列，故配置的时候应该为`5`

余下的collect开头的收集excel配置与学生信息Excel配置相类似，配置中含有注释，于是不再逐一解释，下图为配置的部分内容

![image](https://user-images.githubusercontent.com/20316736/231217688-e39d28df-8d07-480b-8340-61f0338d12e4.png)

下方截图为收集excel的内容，可以和collect配置对照比较

![image](https://user-images.githubusercontent.com/20316736/231222788-e4d481f2-ca4a-4819-b863-cc84bc50210d.png)

`exportExcelPath` 是最终学时表导出的位置，可以留空，留空时则默认为当前Jar包下生成文件

![image](https://user-images.githubusercontent.com/20316736/231220828-c4bd1bbb-3abe-4036-8396-fbc55927b1f0.png)

余下的配置为学时表模板每列的固定内容，可以按需进行修改

![image](https://user-images.githubusercontent.com/20316736/231221419-6d8888bd-9f6b-44eb-94da-0d55d5b8f597.png)

### 2.3执行生成脚本
在解压缩包所在的文件夹，双击`startUp.bat`即可运行
![image](https://user-images.githubusercontent.com/20316736/231223005-baee6f9c-30cc-48ed-b788-e8fee2698d02.png)

见到提示`已生成学时表`即表示成功，如下图所示
![image](https://user-images.githubusercontent.com/20316736/231223190-1b28d6cd-e61c-4dd2-b10e-eba89b18b49e.png)

### 2.4查看生成的学时表
在解压缩包所在的文件夹内，多出了一个Excel

![image](https://user-images.githubusercontent.com/20316736/231224227-667c4f77-a60f-4f5f-8343-27a79ba6bcd7.png)

生成的学时表内容

![image](https://user-images.githubusercontent.com/20316736/231224339-8d5cac29-50d6-4ab4-b13c-22da63dae8c8.png)



### 2.5此次配置例子

此次演示我的`application.properties`配置如下
```
#学生信息excel地址
studentInfoExcelPath = C:/Users/CheungChingYin/Desktop/temp/1_计科2001人员名单.xlsx
#学生信息excel所在sheet页,注意0为第一个表
studentInfoSheetPage = 0
#学生信息excel开始行数，从0开始
studentInfoBeginRow = 1
#学生信息excel的学生学号列数，从0开始
studentInfoStudentNumberCol = 6
#学生信息excel的学生姓名列数，从0开始
studentInfoStudentNameCol = 5

#收集excel地址
collectExcelPath = C:/Users/CheungChingYin/Desktop/temp/组织名单【青年大学习】5.xlsx
#收集excel所在sheet页,注意0为第一个表
collectExcelSheetPage = 0
#收集excel开始行数，从0开始
collectExcelBeginRow = 2
#收集excel的学生学号列数，从0开始
collectExcelStudentNumberCol = 1
#收集excel的学生姓名列数，从0开始
collectExcelStudentNameCol = 0

#导出地址,为空则表示为jar包的当前定制
exportExcelPath =


# 活动名称
activityName = 青年大学习第5期
# 主办方
organizer = 现代信息产业学院
# 学院
collegeName = 现代信息产业学院
# 班级
className = 计算机科学技术2001班
# 参加类型
participateType = 参加者
# 获奖情况
awardSituation = 无
# 认定项目
identifyEvent = 思想品德素质
# 认定活动时长
identifyHours = 1
# 填报人及联系方式
informant = 张三13200001111
# 审核人
reviewer = 李四
# 备注
remark =
# 归属年度
ownershipYear = 2022-2023学年
```


学生信息Excel
![image](https://user-images.githubusercontent.com/20316736/231223560-84f5d33d-664b-47c9-b072-7490c9ff6952.png)

收集信息Excel

![image](https://user-images.githubusercontent.com/20316736/231223635-8c49a3f5-85ca-4a9a-b4a1-ccde49c31359.png)

# 3.二次开发

此项目欢迎各位进行修正bug和二次开发，二次开发请fork本项目，在`dev`分支基础上上新建新分支进行二次开发，开发后可以发起push Request项目的`dev`分支，确认无误后会将代码进行合并。

[项目地址](https://github.com/CheungChingYin/YouthStudy)








