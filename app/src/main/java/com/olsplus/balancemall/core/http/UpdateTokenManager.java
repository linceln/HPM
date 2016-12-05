package com.olsplus.balancemall.core.http;


import java.util.LinkedList;

public class UpdateTokenManager {

    private LinkedList<UpdateTokenTask> updateTokenTasks;

    private  static  UpdateTokenManager updateTokenManager;

    private UpdateTokenManager(){
        updateTokenTasks = new LinkedList<UpdateTokenTask>();
    }

    public static UpdateTokenManager getInstance(){
        if(updateTokenManager == null) {
            synchronized (UpdateTokenManager.class) {
                if (updateTokenManager == null) {
                    updateTokenManager = new UpdateTokenManager();
                }
            }
        }
        return updateTokenManager;
    }

    public boolean isHasTask(){
        return updateTokenTasks.isEmpty();
    }

    public void addTask(UpdateTokenTask updateTokenTask){
        if(updateTokenTasks.isEmpty()){
            updateTokenTasks.add(updateTokenTask);
        }
    }

    public void removeTask(){
        updateTokenTasks.clear();
    }


}
