package com.huixdou.api.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * Created by jinxin on 2018/12/27 9:55.
 */
public class AppVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    @NotBlank(message = "请输入版本号")
    @Pattern(regexp="^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$",message="请输入正确的版本号格式，如：1.1.1")
    @Size(max=10,message="版本号不能超过10个字符")
    private String version;
 
    private String content;

    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", content='" + content + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
