package tudulist.receivers;

import java.util.ArrayList;
import java.util.List;

import tudulist.activities.TaskDetailActivity;
import tudulist.database.TaskProvider;
import tudulist.models.Task;
import tudulist.util.TaskSingleton;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import br.tudulist.R;

public class NotificationReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intentParam) {
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		//new we need to get the task to add an our new intent to trigger
		long taskId = intentParam.getLongExtra("taskId", 0);
		Log.i("tudu", "ID recebido: " + taskId);
		TaskProvider taskProvider = new TaskProvider(context);
		List<Task> tasks = new ArrayList<Task>();
		tasks = taskProvider.findAll();
		Log.i("tudu", "tasks list: " + tasks.size());
		for(Task t : tasks){
			if(taskProvider.db().ext().getID(t) == taskId){				
				TaskSingleton.getInstance().setTask(t);
				Intent it = new Intent(context, TaskDetailActivity.class);
				PendingIntent pendent = PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
				//create a new notification
				Notification nt = new Notification(R.drawable.ic_launcher, "tudu", System.currentTimeMillis());
				nt.setLatestEventInfo(context, "TuDuList", TaskSingleton.getInstance().getTask().getDescription(), pendent);
				notificationManager.notify(1, nt);
			}				
		}
	}

}
