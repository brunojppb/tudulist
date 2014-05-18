package tudulist.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task implements Serializable{

	
	private static final long serialVersionUID = -4316980831425911446L;
	public static final int NOT_IMPORTANT = 0;
	public static final int IMPORTANT = 1;
	public static final int VERY_IMPORTANT = 2;
	
	private String description;
	private GregorianCalendar date;
	private boolean status;
	private int grade;
	
	public Task(){}
	
	public Task(String description, GregorianCalendar date, int grade){
		this.description = description;
		this.date = date;
		this.status = false;
		this.grade = grade;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}
	
	public boolean getStatus(){
		return this.status;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + grade;
		result = prime * result + (status ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (grade != other.grade)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task: " + this.description + "\n" + "Status: " + this.status;
	}
}
