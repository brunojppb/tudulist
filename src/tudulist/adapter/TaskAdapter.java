package tudulist.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import tudulist.models.Task;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.tudulist.R;

public class TaskAdapter extends BaseAdapter{
	
	private LayoutInflater myInflater;
	private List<Task> tasks;
	
	public TaskAdapter(Context ctx, List<Task> tasks){
		this.tasks = tasks;
		this.myInflater = LayoutInflater.from(ctx);
		
	}
	/*
	 * Return how many tasks is there.
	 * 
	 * */
	@Override
	public int getCount() {
		return tasks.size();
	}
	/*
	 * Return the touched item
	 * 
	 * */
	@Override
	public Task getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(this.tasks != null){
			if(this.tasks.size() != 0){
				//get a task from position
				Task t = this.tasks.get(position);
				//inflate the layout to put the date on the screen
				view = myInflater.inflate(R.layout.task_list, null);
				//we can put the date on the screen using a view
				TextView desc = (TextView) view.findViewById(R.id.description);
				TextView date = (TextView) view.findViewById(R.id.date);
				View color = (View) view.findViewById(R.id.color_grade_indicator);
				if(t != null){
					switch (t.getGrade()) {
					case Task.NOT_IMPORTANT:
						color.setBackgroundColor(Color.rgb(46, 204, 113));
						break;
					
					case Task.IMPORTANT:
						color.setBackgroundColor(Color.rgb(245, 171, 53));
						break;
					
					case Task.VERY_IMPORTANT:
						color.setBackgroundColor(Color.rgb(242, 38, 19));
						break;

					default:
						break;
				}
				
					//formatting a task date to string
				
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					
					date.setText(formatter.format(t.getDate().getTime()));
					desc.setText(t.getDescription());
				} 	
			}
		}
		return view;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
		this.notifyDataSetChanged();
	}
	
	

}









