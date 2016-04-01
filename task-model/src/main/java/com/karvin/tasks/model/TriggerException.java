package com.karvin.tasks.model;

/**
 * Created by karvin on 16/3/31.
 */
public class TriggerException extends RuntimeException{

    public TriggerException(){
        super();
    }

    public TriggerException(Throwable e){
        super(e);
    }

}
