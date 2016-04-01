package com.karvin.tasks.utils;

import com.karvin.tasks.model.TriggerException;
import com.karvin.tasks.model.TriggerUnit;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karvin on 16/3/31.
 */
public class DataBaseUtils {

    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    private static DataSource dataSource;

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

    private Connection getConnection() throws SQLException {
        if(dataSource == null){
            synchronized (DataBaseUtils.class) {
                BasicDataSource ds = new BasicDataSource();
                ds.setUrl(this.jdbcUrl);
                dataSource = ds;
            }
        }
        return dataSource.getConnection(this.getJdbcUser(),this.getJdbcPassword());
    }

    public TriggerUnit update(TriggerUnit trigger){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement("insert into trigger_unit(cron_expression,method,class,args) values(?,?,?,?) on duplicate key update cron_expression=?,method=?,class=?,args=?");
            preparedStatement.execute();
            return trigger;
        }catch (Exception e){
            throw new TriggerException(e);
        }finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    public boolean delete(long taskId){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement("delete from trigger_unit where id=?");
            preparedStatement.setLong(1,taskId);
            return preparedStatement.execute();
        }catch (Exception e){
            throw new TriggerException(e);
        }finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    public List<TriggerUnit> getTriggers(){
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.getConnection();
            preparedStatement = connection.prepareStatement("select id, cron_expression,method,class,args from trigger_unit");
            resultSet = preparedStatement.executeQuery();
            return this.parse(resultSet);
        }catch (Exception e){
            throw new TriggerException(e);
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    private List<TriggerUnit> parse(ResultSet rs) throws SQLException {
        List<TriggerUnit> triggers = new ArrayList<TriggerUnit>();
        while(rs.next()){
            TriggerUnit trigger = new TriggerUnit();
            trigger.setCronExpression(rs.getString("cron_expression"));
            trigger.setClazz(rs.getString("class"));
            trigger.setMethodName(rs.getString("method"));
            String arg = rs.getString("args");
            if(arg != null) {
                String[] args = arg.split(",");
                trigger.setArgClass(args);
            }else{
                trigger.setArgClass(new String[0]);
            }
            triggers.add(trigger);
            trigger.setJobId(rs.getLong("id"));
        }
        return triggers;
    }

}
