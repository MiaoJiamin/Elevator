package elevator2;
import java.util.ArrayList;
import java.util.Scanner;

public class lift {
	public static void main(String[] args){
		String[] array = new String[10000];
		int i = 0;
		String line = null;
		Scanner scanner = new Scanner(System.in);;			
		while(!"run".equals(line=scanner.nextLine())){
			array[i++] = line;
		}							//将所有的请求放到字符串数组中	
		
		for(i=0;array[i]!=null;i++){
			array[i] = array[i].replaceAll(" ","");
		}							//去除空格
		scanner.close();
		

		ArrayList<Request> queue = new ArrayList<Request>();
//		Scheduler scheduler = new Scheduler();
//		scheduler.findFirstRequest(array, queue);
//		scheduler.manage(array, queue);
		override sch = new override();
		sch.findFirstRequest(array, queue);
		sch.manage(array, queue);
		
		
	}
}
