package com.karvin.tasks.registry;

import com.karvin.tasks.model.TriggerUnit;

/**
 * Created by karvin on 16/3/31.
 */
public interface Registry {

    String register(TriggerUnit triggerUnit);

    boolean unregister(String taskId);

}
