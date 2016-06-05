import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.al3xable.mgke.TimetableStudent;
import com.al3xable.mgke.TimetableTeacher;

public class Main {


	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		TimetableStudent student = new TimetableStudent();
		TimetableTeacher teacher = new TimetableTeacher();
		timer.schedule( new TimerTask() {
		    public void run() {
		       // do your work 
		    	student.update();
		    	teacher.update();
		    	System.out.println(new Date().toString()+": Update");
		    }
		 }, 0, 60*1000*10); //Update each 10 minutes
	}
	
}
