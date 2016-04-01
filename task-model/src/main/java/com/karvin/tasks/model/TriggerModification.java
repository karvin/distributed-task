package com.karvin.tasks.model;

import java.io.Serializable;

/**
 * Created by karvin on 16/4/1.
 */
public class TriggerModification implements Serializable {

    private String jobId;
    private ModifyType modifyType;


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public ModifyType getModifyType() {
        return modifyType;
    }

    public void setModifyType(ModifyType modifyType) {
        this.modifyType = modifyType;
    }

    public static enum ModifyType{
        UPDATE,DELETE,MODIFY;
    }

}
