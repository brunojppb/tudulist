package tudulist.util;

import tudulist.models.Task;

public class TaskSingleton {
	
	private Task task;
	private static final TaskSingleton instance = new TaskSingleton();
	
	private TaskSingleton(){}
	
	public static TaskSingleton getInstance(){
		return instance;
	}
	
	public void setTask(Task t){
		this.task = t;
	}
	
	public Task getTask(){
		return this.task;
	}
	
}
