# 1.简介
此项目用于广州商学院的青年大学习学时表制作，只要通过配置文件进行简单配置，即可根据班级学生信息Excel和大学习收集名单Excel自动生成相应的学时表。

# 2.如何使用

### 2.1 下载压缩安装包
通过[Release](https://github.com/CheungChingYin/YouthStudy/releases)下载最新的压缩包。

### 2.2 解压下载的压缩安装包
压缩包内容如下
![image](https://user-images.githubusercontent.com/20316736/231210326-b313bb3d-777a-468d-8704-d1602ec4d0d3.png)

### 2.3 配置`application.properties` 文件
在文件夹内，通过记事本打开`application.properties`文件
![image](https://user-images.githubusercontent.com/20316736/231211868-462903cb-a873-484e-81f3-98c19e719f7b.png)

`studentInfoExcelPath` 指学生信息Excel地址，路径分隔符使用 `\` 和 `/` 皆可，单使用 `\` 的时候必须要注意进行转义为 `\\`。

`studentInfoSheetPage` 指学生信息excel所在sheet页,注意0是指第一个表。

![image](https://user-images.githubusercontent.com/20316736/231212935-1aa77d1d-243e-4e17-b96c-9b06611275cc.png)

`studentInfoBeginRow` 指学生信息excel开始行数，注意行数是从0开始，例如学生信息表如下图所示

![image](https://user-images.githubusercontent.com/20316736/231214094-83548fbc-b0c5-49af-981e-e6516233effe.png)

由于图片中第一行是表头，第二行才是学生信息，故配置的时候



