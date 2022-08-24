package cn.fighter3.entity;


/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description 推送过程数据实体
 */
public class PushProcess {
    private Long id;
    private String processName;
    private String auditPerson;
    private String auditResult;
    private Integer pushFlag;
    private Integer pushState;

    public Integer getPushState() {
        return pushState;
    }

    public void setPushState(Integer pushState) {
        this.pushState = pushState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public Integer getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(Integer pushFlag) {
        this.pushFlag = pushFlag;
    }

    @Override
    public String toString() {
        return "PushProcess{" +
                "id=" + id +
                ", processName='" + processName + '\'' +
                ", auditPerson='" + auditPerson + '\'' +
                ", auditResult='" + auditResult + '\'' +
                ", pushFlag=" + pushFlag +
                ", pushState=" + pushState +
                '}';
    }
}
