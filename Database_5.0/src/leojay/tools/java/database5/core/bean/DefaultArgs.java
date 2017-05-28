package leojay.tools.java.database5.core.bean;

import leojay.tools.java.database5.core.assist_arg.DefaultProperty;

import java.io.Serializable;

/**
 * <p>
 * time: 17/1/16__10:57
 *
 * @author leojay
 */
public class DefaultArgs implements Serializable, DefaultProperty {

    private String uniqueId = null;//主键id
    //数据表可选字段变量
    private String createTime = null;//创建时间
    private String updateTime = null;//更新时间

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
