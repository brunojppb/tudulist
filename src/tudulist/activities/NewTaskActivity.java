package tudulist.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tudulist.database.TaskProvider;
import tudulist.models.Task;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import br.tudulist.R;

public class NewTaskActivity extends Activity{
	
	
	private Task task;
	private int year;
	private int month;
	private int day;
	private GregorianCalendar calendar;
	private TaskProvider taskManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_task);
		task = new Task();
		calendar = new GregorianCalendar();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		taskManager = new TaskProvider(this);
		//setting the dialog button action
	}
	
	public void saveTask(View v){
		EditText description = (EditText) findViewById(R.id.description);
		RadioGroup gradeRadio = (RadioGroup) findViewById(R.id.rd_grade);
		
		if(description.getText().length() > 0){
			switch (gradeRadio.getCheckedRadioButtonId()) {
			case R.id.rd_not_important:
				task.setGrade(Task.NOT_IMPORTANT);
				break;
			
			case R.id.rd_important:
				task.setGrade(Task.IMPORTANT);
				break;
			
			case R.id.rd_very_important:
				task.setGrade(Task.VERY_IMPORTANT);
				break;
				
			default:
				break;
			}
			task.setDescription(description.getText().toString());
			task.setDate(calendar);
			Log.i("task", "Salvando no banco...");
			taskManager.save(task);
			taskManager.close();
			finish();
		}
		else{
			Toast.makeText(this, getResources().getString(R.string.empty_task), Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	@SuppressWarnings("deprecation")
	public void showDateDialog(View v){
		showDialog(v.getId());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case R.id.btn_date:
			return new DatePickerDialog(this, datePickerListener, year, month, day);

		default:
			return null;
		}
	}
	
	//when the dialog dismiss the callback bellow will be trigger
	private DatePickerDialog.OnDateSetListener datePickerListener = 
			new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int yearOf, int monthOfYear,
						int dayOfMonth) {
					year = yearOf;
					month = monthOfYear;
					day = dayOfMonth;
					
					//set selected date in to the button
					Button button = (Button) findViewById(R.id.btn_date);
					button.setText(day + "/" + (month+1) + "/" + year);
					//setting the date on the task
					calendar.set(yearOf, monthOfYear, dayOfMonth);
				}
			};
	
	
	
}
