package com.github.wangji92.arthas.plugin.constants;

/**
 * @author 汪小哥
 * @date 21-12-2019
 */
public interface ArthasCommandConstants {
    /**
     * 调用次数
     */
    String INVOKE_COUNT = "5";
    /**
     * 调用监控的次数
     */
    String INVOKE_MONITOR_COUNT = "10";
    /**
     * 调用监控的时间间隔
     */
    String INVOKE_MONITOR_INTERVAL="10";
    /**
     * 展开的结构
     */
    String RESULT_X = "3";

    String PRO_PREFIX = "ArthasIdeaPlugin";
    /**
     * 缓存hashValue的信息
     */
    String CLASSLOADER_HASH_VALUE = "CLASSLOADER_HASH_VALUE";

    /**
     * spring 获取表达式
     */
    String SPRING_CONTEXT_STATIC_OGNL_EXPRESSION = "springContextStaticOgnlExpression";

    /**
     * spring 表达式信息
     */
    String SPRING_CONTEXT_PARAM = "#springContext";
    /**
     * 安装脚本
     */
    String AS_INSTALL_BASH = "curl -sk https://arthas.gitee.io/arthas-boot.jar -o ~/.arthas-boot.jar  && echo \"alias as.sh='java -jar ~/.arthas-boot.jar --repo-mirror aliyun --use-http'\" >> ~/.bashrc && source ~/.bashrc";
}
