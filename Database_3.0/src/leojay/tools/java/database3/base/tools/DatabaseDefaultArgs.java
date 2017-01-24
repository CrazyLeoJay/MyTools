package leojay.tools.java.database3.base.tools;

/**
 * <p>
 * time: 17/1/16__10:57
 *
 * @author leojay
 */
public class DatabaseDefaultArgs {

    private String uniqueId = null;//主键id
    //数据表可选字段变量
    private String createTime = null;//创建时间
    private String updateTime = null;//更新时间

    //判断是否添加创建时间字段
    private boolean isCreateTimeField = true;
    //判断是否添加更新时间字段
    private boolean isUpdateTimeField = true;

    //数据字段
    public static final String UNId_ARG = "uniqueId";
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String UPDATE_TIME = "updateTime";//更新时间

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

    public boolean isCreateTimeField() {
        return isCreateTimeField;
    }

    public void setCreateTimeField(boolean createTimeField) {
        isCreateTimeField = createTimeField;
    }

    public boolean isUpdateTimeField() {
        return isUpdateTimeField;
    }

    public void setUpdateTimeField(boolean updateTimeField) {
        isUpdateTimeField = updateTimeField;
    }

}
