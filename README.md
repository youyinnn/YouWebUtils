## YouWebUtils
[![Travis](https://img.shields.io/badge/version-1.8.4-green.svg)]()
[![Travis](https://img.shields.io/badge/servletapi-3.1.0-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/log4j2-2.9.0-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/cos-26Dec2008-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/fastjson-1.2.46-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/javajwt-3.2.0-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/dom4j-1.6.1-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/jaxen-1.1.6-brightgreen.svg)]()

**这是一个“自用”的Web开发工具包**

#### 介绍

- 分4个梯队
    - first：主要放和web组件相关的基层类，如BaseHttpFilter
    - second：主要放web开发常用工具类，如：
        - Json处理
            - json模板池
            - json对象与json串转换
            - json以key寻value
            - json串处理
        - Jwt处理
            - 签发token
            - 验证token
        - Properties处理
            - 常用系统prop的快速获取
            - pid获取
            - 快速load一个properties文件
    - third：主要放项目开发常用工具类，如：
        - Class处理
            - 提供扫描类等方法
        - 数据库处理
            - 库处理：从连接中获取所连接的库名/从库中获取所有表名
            - 表处理：表存在/从表中获取所有列名
            - 类名处理：转换类名为符合阿里巴巴手册对应的表名格式
            - sql脚本执行
        - Dom4j封装处理
            - 快速xml文件读取
        - Log4j2封装处理
            - 分主次地融合多个具有log4j2配置文件格式的xml文件（实现嵌入log4j2配置）
        - 反射工具类
        - sql脚本执行工具类
        - 集合框架工具类
    - fourth：主要放前三个梯队所需辅助类

#### 使用

maven:
```xml
        <dependency>
            <groupId>com.github.youyinnn</groupId>
            <artifactId>you-web-utils</artifactId>
            <version>1.8.4</version>
        </dependency>
```

#### TODO:

- 待添加