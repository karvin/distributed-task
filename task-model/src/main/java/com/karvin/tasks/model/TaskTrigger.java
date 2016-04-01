package com.karvin.tasks.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by karvin on 16/3/31.
 */
public class TaskTrigger implements Serializable{

    private String cronExpression;
    private Method method;
    private Object[] args;
    private Object invoker;
    private String jobId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskTrigger that = (TaskTrigger) o;

        if (cronExpression != null ? !cronExpression.equals(that.cronExpression) : that.cronExpression != null)
            return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(args, that.args)) return false;
        return !(invoker != null ? !invoker.equals(that.invoker) : that.invoker != null);

    }

    @Override
    public int hashCode() {
        int result = cronExpression != null ? cronExpression.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (args != null ? Arrays.hashCode(args) : 0);
        result = 31 * result + (invoker != null ? invoker.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskTrigger{" +
                "args=" + Arrays.toString(args) +
                ", cronExpression='" + cronExpression + '\'' +
                ", method=" + method +
                ", invoker=" + invoker +
                '}';
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Object getInvoker() {
        return invoker;
    }

    public void setInvoker(Object invoker) {
        this.invoker = invoker;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
