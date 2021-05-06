package com.github.wangji92.arthas.plugin.common.enums;

import com.github.wangji92.arthas.plugin.common.enums.base.EnumCodeMsg;
import com.github.wangji92.arthas.plugin.common.param.ScriptParam;
import com.github.wangji92.arthas.plugin.utils.OgnlPsUtils;
import com.github.wangji92.arthas.plugin.utils.SpringStaticContextUtils;
import com.github.wangji92.arthas.plugin.utils.StringUtils;

/**
 * 可以直接执行的脚本通用信息
 *
 * @author 汪小哥
 * @date 04-05-2021
 */
public enum ShellScriptCommandEnum implements EnumCodeMsg<String> {
    /**
     * 调用静态变量 或者方法
     */
    OGNL_GETSTATIC("ognl "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " @"
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + "@"
            + ShellScriptVariableEnum.EXECUTE_INFO.getCode()
            + " -c "
            + ShellScriptVariableEnum.CLASSLOADER_HASH_VALUE.getCode(),
            "ognl to get static method field 注意需要编执行方法的参数") {
        @Override
        public boolean support(ScriptParam param) {

            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isStaticMethodOrField(param.getPsiElement());
        }
    },
    /**
     * 简单的字段
     */
    GETSTATIC("getstatic "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.FIELD_NAME.getCode(),
            "get simple static field") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isStaticField(param.getPsiElement());
        }
    },

    /**
     * watch static field
     */
    WATCH_STATIC_FILED("watch "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " * "
            + " '{params,returnObj,throwExp,@" + ShellScriptVariableEnum.CLASS_NAME.getCode() + "@" + ShellScriptVariableEnum.FIELD_NAME.getCode() + "}'"
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "watch  static field") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isStaticField(param.getPsiElement());
        }
    },
    /**
     * watch non static field
     */
    WATCH_NON_STATIC_FILED("watch "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode() + " "
            + " '{params,returnObj,throwExp,target." + ShellScriptVariableEnum.FIELD_NAME.getCode() + "}' "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " "
            + "'method.initMethod(),method.constructor!=null || !@java.lang.reflect.Modifier@isStatic(method.method.getModifiers())'",
            "watch non static field") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isNonStaticField(param.getPsiElement());
        }
    },
    /**
     * watch
     */
    WATCH("watch "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode() + " "
            + "'{params,returnObj,throwExp}' "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "watch input/output parameter, return object,exception") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },

    /**
     * trace
     */
    TRACE("trace "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode() + " "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + ShellScriptVariableEnum.SKIP_JDK_METHOD.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "trace the execution time of specified method invocation. ") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },

    /**
     * spring get bean
     */
    SPRING_GET_BEAN("ognl "
            + " -x "
            + "'#springContext=" + ShellScriptVariableEnum.SPRING_CONTEXT.getCode() + ",#springContext.getBean(\"" + ShellScriptVariableEnum.SPRING_BEAN_NAME.getCode() + "\")."
            + ShellScriptVariableEnum.EXECUTE_INFO.getCode() + " "
            + " -c "
            + ShellScriptVariableEnum.CLASSLOADER_HASH_VALUE.getCode(),
            "invoke static spring bean【手动编辑填写参数】【bean名称可能不正确,可以手动修改】 ") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            // 必须要配置spring static context
            if (!SpringStaticContextUtils.booleanConfigStaticSpringContext(param.getProject())) {
                return false;
            }
            // spring bean 的名称
            String springBeanName = OgnlPsUtils.getSpringBeanName(param.getPsiElement());
            if (StringUtils.isBlank(springBeanName) || "errorBeanName".equals(springBeanName)) {
                return false;
            }
            String className = OgnlPsUtils.getCommonOrInnerOrAnonymousClassName(param.getPsiElement());
            // 非 java.lang
            if (className.contains("java.lang.") || className.contains("java.util.")) {
                return false;
            }
            return OgnlPsUtils.isNonStaticMethodOrField(param.getPsiElement());
        }
    },

    /**
     * trace
     */
    STACK("stack "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode() + " "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "display the stack trace for the specified class and method") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },
    /**
     * monitor
     */
    MONITOR("monitor "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode() + " "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_MONITOR_COUNT.getCode() + "  --cycle "
            + ShellScriptVariableEnum.INVOKE_MONITOR_INTERVAL.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "monitor method execution statistics ") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },
    /**
     * jad
     */
    JAD("jad --source-only "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME_NOT_STAR.getCode(),
            "decompile class") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },
    /**
     * sc
     */
    SC("sc -d "
            + ShellScriptVariableEnum.CLASS_NAME.getCode(),
            "search all the classes loaded by jvm") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },
    /**
     * sc
     */
    SM("sm -d "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + ShellScriptVariableEnum.METHOD_NAME.getCode(),
            "search the method of classes loaded by jvm") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },


    /**
     * watch * to execute static method
     */
    WATCH_EXECUTE_STATIC_METHOD("watch "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " * "
            + " '{params,returnObj,throwExp,@" + ShellScriptVariableEnum.CLASS_NAME.getCode() + "@" + ShellScriptVariableEnum.EXECUTE_INFO.getCode() + "}' "
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " "
            + ShellScriptVariableEnum.CONDITION_EXPRESS_DEFAULT.getCode(),
            "watch * to execute static method 注意需要编辑执行静态方法的参数") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isNonStaticMethod(param.getPsiElement());
        }
    },
    /**
     * watch 执行 非静态方法
     */
    WATCH_EXECUTE_NO_STATIC_METHOD("watch "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " * "
            + " '{params,returnObj,throwExp,target." + ShellScriptVariableEnum.EXECUTE_INFO.getCode() + "}'"
            + ShellScriptVariableEnum.PRINT_CONDITION_RESULT.getCode() + " -n "
            + ShellScriptVariableEnum.INVOKE_COUNT.getCode() + " "
            + " -x "
            + ShellScriptVariableEnum.PROPERTY_DEPTH.getCode() + " "
            + "'method.initMethod(),method.constructor!=null || !@java.lang.reflect.Modifier@isStatic(method.method.getModifiers())'",
            "watch * to execute method 注意需要编执行方法的参数") {
        @Override
        public boolean support(ScriptParam param) {
            return OgnlPsUtils.isNonStaticMethod(param.getPsiElement());
        }
    },
    /**
     * logger
     */
    LOGGER("logger --name "
            + ShellScriptVariableEnum.CLASS_NAME.getCode() + " "
            + "--level debug "
            + " -c "
            + ShellScriptVariableEnum.CLASSLOADER_HASH_VALUE.getCode(),
            "--level debug 可以编辑修改为 info、error") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },


    /**
     * dump
     */
    DUMP("dump "
            + ShellScriptVariableEnum.CLASS_NAME.getCode()
            + " -d /tmp/output "
            + " -c "
            + ShellScriptVariableEnum.CLASSLOADER_HASH_VALUE.getCode(),
            "dump class byte array from jvm") {
        @Override
        public boolean support(ScriptParam param) {
            if (OgnlPsUtils.isAnonymousClass(param.getPsiElement())) {
                return false;
            }
            return OgnlPsUtils.isPsiFieldOrMethodOrClass(param.getPsiElement());
        }
    },


    ;

    /**
     * @param code
     * @param msg
     */
    ShellScriptCommandEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * code 脚本
     */
    private String code;

    /**
     * 提示信息
     */
    private String msg;


    /**
     * 是否支持
     *
     * @param param
     * @return
     */
    public abstract boolean support(ScriptParam param);


    @Override
    public String getEnumMsg() {
        return msg;
    }

    @Override
    public String getCode() {
        return code;
    }

}
