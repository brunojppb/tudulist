package tudulist.activities;

import java.util.ArrayList;
import java.util.List;

import tudulist.adapter.TaskAdapter;
import tudulist.database.TaskProvider;
import tudulist.models.Task;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.tudulist.R;

import com.db4o.ObjectSet;

public class TaskListActivity extends Activity implements OnItemClickListener, OnItemLongClickListener{

	private ListView listView;
	private TaskAdapter taskAdapter;
	private List<Task> tasks;
	private TaskProvider taskManager;
	Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		//set the main layout
		setContentView(R.layout.main);
		//get the list
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setLongClickable(true);
		taskManager = new TaskProvider(this);
		builder = new Builder(this);
		
		//populate the listView
		populateListView();
	}
	
	
	public void newTask(View v){
		Intent i = new Intent(this, NewTaskActivity.class);
		startActivity(i);
	}
	
	public void populateListView(){
		tasks = null;
		tasks = taskManager.findAll();
		if(tasks != null && tasks.size() > 0){
			taskAdapter = new TaskAdapter(this, tasks);
		}
		else{
			tasks = new ArrayList<Task>();
			taskAdapter = new TaskAdapter(this, tasks);
		}
		
		listView.setAdapter(taskAdapter);
		listView.setCacheColorHint(Color.TRANSPARENT);
		taskAdapter.notifyDataSetChanged();
		
	}
	
//	public void reloadData(){
//		tasks = taskManager.findAll();
//		Log.i("tudu", "reload tasks DB: " + tasks.size());
//		if(tasks != null){
//			taskAdapter.setTasks(tasks);
//		}
//		else{
//			tasks = new ArrayList<Task>();
//			taskAdapter.setTasks(tasks);
//		}
//		//taskAdapter.notifyDataSetChanged();
//	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i("tudu", "clicada");
		Task t = (Task) taskAdapter.getItem(position);
		Toast.makeText(this, "Task: " + t.getDescription(), Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		//builder.setCancelable(true);
		builder.setTitle("Delete a task");
		final int pos = position;
		builder.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Log.i("tudu", "Clicou NO");
			}
		});
		
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("tudu", "Clicou YES");
				Task t = (Task) taskAdapter.getItem(pos);
				Log.i("tudu", "Task: " + t.getDescription());
				ObjectSet<Task> result = taskManager.db().queryByExample(t);
				if(result != null){
					Log.i("task", "encontrou a task");
					if(result.size() != 0){
						t = result.get(0);
						Log.i("task", "desc: " + t.getDescription());
						taskManager.delete(t);
						populateListView();
					}
				}
			}
		}).show();
		
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.taskManager.close();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.populateListView();
		Log.i("tudu", "Count: " + taskAdapter.getCount());
	}
	
	
	
}
