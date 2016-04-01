package com.karvin.tasks.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by karvin on 16/4/1.
 */
public class TriggerUnit implements Serializable {

    private String clazz;

    private String methodName;

    private String cronExpression;

    private Long jobId;

    private String[] argClass;

    @Override
    public String toString() {
        return "TriggerUnit{" +
                "argClass=" + Arrays.toString(argClass) +
                ", clazz='" + clazz + '\'' +
                ", methodName='" + methodName + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriggerUnit that = (TriggerUnit) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (cronExpression != null ? !cronExpression.equals(that.cronExpression) : that.cronExpression != null)
            return false;
        if (jobId != null ? !jobId.equals(that.jobId) : that.jobId != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(argClass, that.argClass);

    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (cronExpression != null ? cronExpression.hashCode() : 0);
        result = 31 * result + (jobId != null ? jobId.hashCode() : 0);
        result = 31 * result + (argClass != null ? Arrays.hashCode(argClass) : 0);
        return result;
    }

    public String[] getArgClass() {
        return argClass;
    }

    public void setArgClass(String[] argClass) {
        this.argClass = argClass;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
