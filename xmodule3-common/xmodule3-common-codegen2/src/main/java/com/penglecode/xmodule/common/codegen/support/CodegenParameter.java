package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.*;

/**
 * 代码生成Freemarker模板参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/24 15:50
 */
@SuppressWarnings("unchecked")
public class CodegenParameter extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    public CodegenParameter() {
        this(new HashMap<>());
    }

    public CodegenParameter(Map<? extends String, ?> m) {
        super(m);
        put("targetAllImportTypes", new HashSet<>());
    }

    public void setTemplateFileName(String templateFileName) {
        put("templateFileName", templateFileName);
    }

    /** 待生成目标对象的Freemarker模板文件名 */
    public String getTemplateFileName() {
        return (String) get("templateFileName");
    }

    public void setTargetFileName(String targetFileName) {
        put("targetFileName", targetFileName);
    }

    /** 待生成目标对象的文件名 */
    public String getTargetFileName() {
        return (String) get("targetFileName");
    }

    /** 待生成目标对象的项目内imports */
    protected void setTargetProjectImports(List<String> targetProjectImports) {
        put("targetProjectImports", targetProjectImports);
    }

    /** 待生成目标对象的第三方imports */
    protected void setTargetThirdImports(List<String> targetThirdImports) {
        put("targetThirdImports", targetThirdImports);
    }

    /** 待生成目标对象的JDK imports */
    protected void setTargetJdkImports(List<String> targetJdkImports) {
        put("targetJdkImports", targetJdkImports);
    }

    public Set<FullyQualifiedJavaType> getTargetAllImportTypes() {
        return (Set<FullyQualifiedJavaType>) get("targetAllImportTypes");
    }

    public void setTargetClass(String targetClass) {
        put("targetClass", targetClass);
    }

    /** 待生成目标对象的类名 */
    public String getTargetClass() {
        return (String) get("targetClass");
    }

    public void setTargetExtends(String targetExtends) {
        put("targetExtends", targetExtends);
    }

    /** 待生成目标对象的extends父类名 */
    public String getTargetExtends() {
        return (String) get("targetExtends");
    }

    public void setTargetImplements(List<String> targetImplements) {
        put("targetImplements", targetImplements);
    }

    /** 待生成目标对象的实现接口名列表 */
    public List<String> getTargetImplements() {
        return (List<String>) get("targetImplements");
    }

    public void setTargetPackage(String targetPackage) {
        put("targetPackage", targetPackage);
    }

    /** 待生成目标对象的包名 */
    public String getTargetPackage() {
        return (String) get("targetPackage");
    }

    public void setTargetComment(String targetComment) {
        put("targetComment", targetComment);
    }

    /** 待生成目标对象的注释 */
    public String getTargetComment() {
        return (String) get("targetComment");
    }

    public void setTargetAuthor(String targetAuthor) {
        put("targetAuthor", targetAuthor);
    }

    /** 待生成目标对象的作者 */
    public String getTargetAuthor() {
        return (String) get("targetAuthor");
    }

    public void setTargetVersion(String targetVersion) {
        put("targetVersion", targetVersion);
    }

    /** 待生成目标对象的版本 */
    public String getTargetVersion() {
        return (String) get("targetVersion");
    }

    public void setTargetCreated(String targetCreated) {
        put("targetCreated", targetCreated);
    }

    /** 待生成目标对象的创建时间 */
    public String getTargetCreated() {
        return (String) get("targetCreated");
    }

    public void setTargetAnnotations(List<String> targetAnnotations) {
        put("targetAnnotations", targetAnnotations);
    }

    /** 待生成目标对象的SpringBean的名称 */
    public List<String> getTargetAnnotations() {
        return (List<String>) get("targetAnnotations");
    }

    /**
     * 计算所生成的类需要导入的import类
     * @param targetPackage - 当前需要import的类的包名
     */
    public void calculateTargetImports(String targetPackage) {
        Set<FullyQualifiedJavaType> targetAllImportTypes = getTargetAllImportTypes();
        final List<String> jdkImports = new ArrayList<>(); //JDK包中的import类
        final List<String> thirdImports = new ArrayList<>(); //第三方jar包中的import类
        final List<String> projectImports = new ArrayList<>(); //具有共同BasePackage的import类
        if(!CollectionUtils.isEmpty(targetAllImportTypes)) {
            targetAllImportTypes.stream().filter(importType -> !importType.getPackageName().equals(targetPackage)) //过滤同一个包下的导入
                                .flatMap(importedType -> importedType.getImportList().stream())
                                .forEach(importedType -> {
                if(importedType.startsWith("java.")) {
                    jdkImports.add(importedType);
                } else if(importedType.startsWith(BasePackage.class.getPackage().getName())) {
                    projectImports.add(importedType);
                } else {
                    thirdImports.add(importedType);
                }
            });
        }
        Collections.sort(jdkImports);
        Collections.sort(thirdImports);
        Collections.sort(projectImports);
        setTargetJdkImports(jdkImports);
        setTargetThirdImports(thirdImports);
        setTargetProjectImports(projectImports);
    }

}