package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.*;

/**
 * 代码生成Freemarker模板参数基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/24 15:50
 */
public abstract class CodegenParameter {

    private static final long serialVersionUID = 1L;

    /** 待生成目标对象的Freemarker模板文件名 */
    private final String targetTemplateName;

    /** 待生成目标对象的文件名 */
    private String targetFileName;

    /** 待生成目标对象的第三方imports */
    private List<String> targetProjectImports;

    /** 待生成目标对象的第三方imports */
    private List<String> targetThirdImports;

    /** 待生成目标对象的JDK imports */
    private List<String> targetJdkImports;

    private final Set<FullyQualifiedJavaType> targetAllImportTypes = new HashSet<>();

    /** 待生成目标对象的类名 */
    private String targetClass;

    /** 待生成目标对象的extends父类名 */
    private String targetExtends;

    /** 待生成目标对象的实现接口名列表 */
    private List<String> targetImplements;

    /** 待生成目标对象的包名 */
    private String targetPackage;

    /** 待生成目标对象的注释 */
    private String targetComment;

    /** 待生成目标对象的作者 */
    private String targetAuthor;

    /** 待生成目标对象的版本 */
    private String targetVersion;

    /** 待生成目标对象的创建时间 */
    private String targetCreated;

    /** 待生成目标对象的SpringBean的名称 */
    private List<String> targetAnnotations;

    public CodegenParameter(String targetTemplateName) {
        this.targetTemplateName = targetTemplateName;
    }

    public String getTargetTemplateName() {
        return targetTemplateName;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public List<String> getTargetProjectImports() {
        return targetProjectImports;
    }

    protected void setTargetProjectImports(List<String> targetProjectImports) {
        this.targetProjectImports = targetProjectImports;
    }

    public List<String> getTargetThirdImports() {
        return targetThirdImports;
    }

    protected void setTargetThirdImports(List<String> targetThirdImports) {
        this.targetThirdImports = targetThirdImports;
    }

    public List<String> getTargetJdkImports() {
        return targetJdkImports;
    }

    protected void setTargetJdkImports(List<String> targetJdkImports) {
        this.targetJdkImports = targetJdkImports;
    }

    protected Set<FullyQualifiedJavaType> getTargetAllImportTypes() {
        return targetAllImportTypes;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetExtends() {
        return targetExtends;
    }

    public void setTargetExtends(String targetExtends) {
        this.targetExtends = targetExtends;
    }

    public List<String> getTargetImplements() {
        return targetImplements;
    }

    public void setTargetImplements(List<String> targetImplements) {
        this.targetImplements = targetImplements;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetComment() {
        return targetComment;
    }

    public void setTargetComment(String targetComment) {
        this.targetComment = targetComment;
    }

    public String getTargetAuthor() {
        return targetAuthor;
    }

    public void setTargetAuthor(String targetAuthor) {
        this.targetAuthor = targetAuthor;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getTargetCreated() {
        return targetCreated;
    }

    public void setTargetCreated(String targetCreated) {
        this.targetCreated = targetCreated;
    }

    public List<String> getTargetAnnotations() {
        return targetAnnotations;
    }

    public void setTargetAnnotations(List<String> targetAnnotations) {
        this.targetAnnotations = targetAnnotations;
    }

    public void addTargetImportType(FullyQualifiedJavaType targetImportType) {
        targetAllImportTypes.add(targetImportType);
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