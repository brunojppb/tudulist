package tudulist.activities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tudulist.adapter.TaskAdapter;
import tudulist.database.TaskProvider;
import tudulist.models.Task;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.tudulist.R;

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
			Log.i("tudu", "Tasks encontradas no DB" + tasks.size());
			taskAdapter = new TaskAdapter(this, tasks);
		}
		else{
			Log.i("tudu", "Sem Tasks cadastradas do DB");
			tasks = new ArrayList<Task>();
			taskAdapter = new TaskAdapter(this, tasks);
		}
		
		listView.setAdapter(taskAdapter);
		listView.setCacheColorHint(Color.TRANSPARENT);
		taskAdapter.notifyDataSetChanged();
		
	}
	
	public void reloadData(){
		tasks = taskManager.findAll();
		Log.i("tudu", "reload tasks DB: " + tasks.size());
		if(tasks != null){
			taskAdapter.setTasks(tasks);
		}
		else{
			tasks = new ArrayList<Task>();
			taskAdapter.setTasks(null);
			taskAdapter.setTasks(tasks);
		}
		taskAdapter.notifyDataSetChanged();
	}
	
	@Override
	@SuppressLint("SimpleDateFormat")
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i("tudu", "clicada");
		Task t = (Task) taskAdapter.getItem(position);
		DateFormat formatter;
		Log.i("tudu", Locale.getDefault().getDisplayName());
		Log.i("tudu", Locale.US.getDisplayName());
		formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		String text = getResources().getString(R.string.listview_click_text) + " " + formatter.format(t.getDate().getTime());
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		final int pos = position;
		Task task = (Task) taskAdapter.getItem(pos);
		builder.setTitle(getResources().getString(R.string.delete_msg) + " " + task.getDescription() + "?");
		builder.setNegativeButton(getResources().getString(R.string.no), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Log.i("tudu", "Clicou NO");
			}
		});
		
		builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("tudu", "Clicou YES");
				Task t = (Task) taskAdapter.getItem(pos);
				long taskId = taskManager.db().ext().getID(t);
				Log.i("tudu", "Task: " + t.getDescription());
				Task result = taskManager.db().ext().getByID(taskId);
				if(result != null){
					Log.i("tudu", "encontrou a task");
						Log.i("tudu", "desc: " + t.getDescription());
						taskManager.delete(t);
				}
				reloadData();
			}
		}).show();
		
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.taskManager.close();
		Log.i("tudu", "App destruido!");
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
		reloadData();
		Log.i("tudu", "Count: " + taskAdapter.getCount());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}
