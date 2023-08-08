package com.yg.pojo;

public class BasicData {
    private Integer baseId;

    private Integer parentId;

    private String baseName;

    private String baseDesc;

    private String u;

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName == null ? null : baseName.trim();
    }

    public String getBaseDesc() {
        return baseDesc;
    }

    public void setBaseDesc(String baseDesc) {
        this.baseDesc = baseDesc == null ? null : baseDesc.trim();
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u == null ? null : u.trim();
    }

    @Override
    public String toString() {
        return "BasicData{" +
                "baseId=" + baseId +
                ", parentId=" + parentId +
                ", baseName='" + baseName + '\'' +
                ", baseDesc='" + baseDesc + '\'' +
                ", u='" + u + '\'' +
                '}';
    }
}