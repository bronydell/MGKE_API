import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.al3xable.mgke.TimetableStudent;
import com.al3xable.mgke.TimetableTeacher;

public class Main {
	public static void main(String[] args) {
		Timer timer = new Timer();
		TimetableStudent student = new TimetableStudent();
		TimetableTeacher teacher = new TimetableTeacher();
		
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					student.update();
					System.out.println(new Date().toString() + ": Student updated successfully");
				} catch (Throwable e) {
 					System.out.println(new Date().toString() + ": Student update exception");
					e.printStackTrace();
				}
				
				try {
					teacher.update();
					System.out.println(new Date().toString() + ": Teacher updated successfully");
				} catch (Throwable e) {
					System.out.println(new Date().toString() + ": Teacher update exception");
					e.printStackTrace();
				}
			}
		}, 0, 60*1000*10); //Update each 10 minutes
	}
}
