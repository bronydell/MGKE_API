import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.al3xable.mgke.TimetableStudent;

public class Main {


	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		TimetableStudent student = new TimetableStudent();
		timer.schedule( new TimerTask() {
		    public void run() {
		       // do your work 
		    	student.update();
		    	System.out.println(new Date().toString()+": Update");
		    }
		 }, 0, 60*1000*10); //Update each 10 minutes
	}
	
}
