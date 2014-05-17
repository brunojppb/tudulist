package tudulist.database;

import java.util.List;

import tudulist.models.Task;
import android.content.Context;

public class TaskProvider extends Db4oHelper {
	
	public static final String TAG = "TaskProvider";
	private static TaskProvider provider = null;

	public TaskProvider(Context con) {
		super(con);
		// TODO Auto-generated constructor stub
	}
	
	public static TaskProvider getInstance(Context ctx){
		if(provider == null){
			provider = new TaskProvider(ctx);
		}
		return provider;
	}
	
	public void save(Task task){
		this.db().store(task);
		this.db().commit();
	}
	
	public void delete(Task task){
		this.db().delete(task);
		this.db().commit();
	}
	
	public List<Task> findAll(){
		return this.db().query(Task.class);
	}

}
