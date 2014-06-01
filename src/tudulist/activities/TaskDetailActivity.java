package tudulist.activities;

import java.text.DateFormat;
import java.util.Locale;

import tudulist.models.Task;
import tudulist.util.TaskSingleton;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.tudulist.R;

public class TaskDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);
		Task t = TaskSingleton.getInstance().getTask();
		if(t != null){
			TextView date = (TextView) findViewById(R.id.date_detail);
			TextView description = (TextView) findViewById(R.id.description_detail);
			View background = (View) findViewById(R.id.background_view_detail);
			
			DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
			date.setText(formatter.format(t.getDate().getTime()));
			description.setText(t.getDescription());
			
			switch (t.getGrade()) {
			case Task.NOT_IMPORTANT:
				background.setBackgroundColor(Color.rgb(101, 198, 187));
				break;
			
			case Task.IMPORTANT:
				background.setBackgroundColor(Color.rgb(244, 208, 63));
				break;
			
			case Task.VERY_IMPORTANT:
				background.setBackgroundColor(Color.rgb(224, 130, 131));
				break;

			default:
				background.setBackgroundColor(Color.rgb(101, 198, 187));
				break;
			}
			
			
		}
	}

}
