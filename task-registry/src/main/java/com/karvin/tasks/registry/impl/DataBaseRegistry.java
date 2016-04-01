package com.karvin.tasks.registry.impl;

import com.karvin.tasks.model.TriggerUnit;
import com.karvin.tasks.registry.Registry;
import com.karvin.tasks.utils.DataBaseUtils;

/**
 * Created by karvin on 16/3/31.
 */
public class DataBaseRegistry implements Registry {

    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    private static DataBaseUtils dataBaseUtils;


    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    private DataBaseUtils getDBUtils(){
        if(dataBaseUtils == null){
            dataBaseUtils = new DataBaseUtils();
            dataBaseUtils.setJdbcUrl(jdbcUrl);
            dataBaseUtils.setJdbcUser(this.jdbcUser);
            dataBaseUtils.setJdbcPassword(this.jdbcPassword);
        }
        return dataBaseUtils;
    }

    public String register(TriggerUnit triggerUnit) {
        TriggerUnit unit = dataBaseUtils.update(triggerUnit);
        return String.valueOf(unit.getJobId());
    }

    public boolean unregister(String taskId) {
        long id = 0;
        try{
            id = Long.parseLong(taskId);
        }catch (Exception e){
            return false;
        }
        return dataBaseUtils.delete(id);
    }
}
