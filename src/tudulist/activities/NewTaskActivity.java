package tudulist.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import tudulist.database.TaskProvider;
import tudulist.models.Task;
import tudulist.receivers.NotificationReceiver;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;
import br.tudulist.R;

public class NewTaskActivity extends Activity{
	
	
	private Task task;
	private int myYear, myMonth, myDay;
	private int myHour, myMinute;
	private GregorianCalendar calendar;
	private TaskProvider taskManager;
	private ArrayList<RadioButton> rdButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_task);
		task = new Task();
		calendar = new GregorianCalendar();
		myYear = calendar.get(Calendar.YEAR);
		myMonth = calendar.get(Calendar.MONTH);
		myDay = calendar.get(Calendar.DAY_OF_MONTH);
		myHour = calendar.get(Calendar.HOUR_OF_DAY);
		myMinute = calendar.get(Calendar.MINUTE);
		calendar.set(Calendar.SECOND, 0);

		taskManager = new TaskProvider(this);
		
		rdButtons = new ArrayList<RadioButton>();
		rdButtons.add((RadioButton)findViewById(R.id.rd_not_important));
		rdButtons.add((RadioButton)findViewById(R.id.rd_important));
		rdButtons.add((RadioButton)findViewById(R.id.rd_very_important));
		
		for(RadioButton rd : rdButtons){
			rd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked) processRadioButtonClick(buttonView);
					
				}
			});
		}
	}
	
	private void processRadioButtonClick(View btnView){
		for (RadioButton button : rdButtons){

	        if (button != btnView ) button.setChecked(false);
	    }
	}
	
	public void saveTask(View v){
		EditText description = (EditText) findViewById(R.id.description);
		int rdIdButton = 0;
		for(RadioButton rdButton : rdButtons){
			if(rdButton.isChecked())
				rdIdButton = rdButton.getId();
		}
		
		if(description.getText().length() > 0){
			switch (rdIdButton) {
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
			taskManager.save(task);
			
			//schedule a new notification to trigger
			long timeIsUp = task.getDate().getTimeInMillis();
			long taskId = taskManager.db().ext().getID(task);
		
			//retrieve a alarmManager from the system
			AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			//create a intent that we need to use to trigger our notification
			Intent it = new Intent(this, NotificationReceiver.class);
			//new we need to put the TaskId into the intent
			Log.i("tudu", "ID passado: " + taskId);
			it.putExtra("taskId", taskId);
			
			//new we need to prepare a PendingIntent to execute after
			PendingIntent pendent = PendingIntent.getBroadcast(getApplicationContext(), 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
			
			//Register the alert in the system
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeIsUp, pendent);
			
			taskManager.close();
			finish();
		}
		else{
			Toast.makeText(this, getResources().getString(R.string.empty_task), Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	public void showTimeDialog(View v){
		TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Button btn = (Button)findViewById(R.id.btn_time);
				myHour = hourOfDay;
				myMinute = minute;
				
				calendar.set(Calendar.HOUR_OF_DAY,myHour);
				calendar.set(Calendar.MINUTE, myMinute);
				
				DateFormat format = new SimpleDateFormat("hh:mm a");
				btn.setText(format.format(calendar.getTime()));
			}
		}, myHour, myMinute, false);
		
		dialog.show();
	}
	
	public void showDateDialog(View v){
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				myYear = year;
				myMonth = monthOfYear;
				myDay = dayOfMonth;
				//setting the date
				calendar.set(Calendar.YEAR, myYear);
				calendar.set(Calendar.MONTH, myMonth);
				calendar.set(Calendar.DAY_OF_MONTH, myDay);
				
				//set selected date in to the button
				Button button = (Button) findViewById(R.id.btn_date);
				DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
				button.setText(formatter.format(calendar.getTime()));
				
			}
		}, myYear, myMonth, myDay);
		
		dialog.show();
	}
}
