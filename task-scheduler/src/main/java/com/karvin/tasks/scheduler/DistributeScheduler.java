package com.karvin.tasks.scheduler;

import com.karvin.tasks.model.TaskTrigger;
import org.quartz.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karvin on 16/3/31.
 */
public class DistributeScheduler {

    private ConcurrentHashMap<String,TaskTrigger> context = new ConcurrentHashMap<String, TaskTrigger>();

    private SchedulerFactory schedulerFactory;

    private boolean existingReplace = true;

    public DistributeScheduler(){

    }

    public boolean register(String jobId,TaskTrigger trigger) throws SchedulerException {
        TaskTrigger taskTrigger = context.get(jobId);
        if(taskTrigger == null){
            context.put(jobId,taskTrigger);
            return true;
        }
        if(this.existingReplace){
            context.put(jobId, taskTrigger);
            Trigger jobTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(trigger.getCronExpression())).withIdentity(jobId).build();
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("invoker",trigger.getInvoker());
            dataMap.put("args",trigger.getArgs());
            dataMap.put("method",trigger.getMethod());
            JobDetail jobDetail = JobBuilder.newJob().ofType(MethodInvokingJob.class).setJobData(dataMap).build();
            Scheduler scheduler = this.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobId);
            if(scheduler.checkExists(jobKey)){
                scheduler.scheduleJob(jobDetail, jobTrigger);
            }
            return true;
        }
        return false;
    }

    public boolean unregister(String jobId) throws SchedulerException {
        TaskTrigger taskTrigger = context.get(jobId);
        if(taskTrigger == null){
            return false;
        }
        stop(jobId);
        context.remove(jobId);
        return true;
    }

    private boolean stop(String jobId) throws SchedulerException {
        TaskTrigger trigger = context.get(jobId);
        if(trigger == null){
            return false;
        }
        Scheduler scheduler = this.getScheduler();
        return scheduler.deleteJob(JobKey.jobKey(jobId));
    }

    public boolean isExistingReplace() {
        return existingReplace;
    }

    public void setExistingReplace(boolean existingReplace){
        this.existingReplace = existingReplace;
    }


    public ConcurrentHashMap<String, TaskTrigger> getContext() {
        return context;
    }

    public void setContext(ConcurrentHashMap<String, TaskTrigger> context) {
        this.context = context;
    }

    public SchedulerFactory getSchedulerFactory() {
        return schedulerFactory;
    }

    public void setSchedulerFactory(SchedulerFactory schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    private org.quartz.Scheduler getScheduler() throws SchedulerException {
        return this.schedulerFactory.getScheduler();
    }

    /**
     * Quartz Job implementation that invokes a specified method.
     * Automatically applied by MethodInvokingJobDetailFactoryBean.
     */
    public static class MethodInvokingJob implements Job {

        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            Object invoker = context.get("invoker");
            Method method = (Method)context.get("method");
            Object[] args = (Object[])context.get("args");
            try {
                context.setResult(method.invoke(invoker,args));
            }
            catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof JobExecutionException) {
                    // -> JobExecutionException, to be logged at info level by Quartz
                    throw (JobExecutionException) ex.getTargetException();
                }
            }
            catch (Exception ex) {
                // -> "unhandled exception", to be logged at error level by Quartz
                throw new RuntimeException(ex);
            }
        }

        public void execute(JobExecutionContext context) throws JobExecutionException {
            this.executeInternal(context);
        }
    }
}
