package elevator2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scheduler {
	
	int i = 0;
	int temp = 0;
	int k = 1;
	Elevator ele = new Elevator();
	
	void findFirstRequest(String[] array, List<Request> queue){	
		
		while(array[i]!=null){
			
			String str1 = "\\(FR,\\+?\\d+,(UP|DOWN),\\+?\\d+\\)";
			String str2 = "\\(ER,\\+?\\d+,\\+?\\d+\\)";
			Pattern pattern1 = Pattern.compile(str1);
			Matcher matcher1 = pattern1.matcher(array[i]);
			Pattern pattern2 = Pattern.compile(str2);
			Matcher matcher2 = pattern2.matcher(array[i]);
			
			if(matcher1.matches()==false && matcher2.matches()==false){		//删除括号不匹配，输入时间、楼层有浮点数
				array[i] = array[i].replaceAll("\\(", "\\[");				//关键字不正确的情况
				array[i] = array[i].replaceAll("\\)", "\\]");
				System.out.println("INVALID " + array[i]);
				i++;
			}else{
				array[i] = array[i].replaceAll("\\(", "");
				array[i] = array[i].replaceAll("\\)", "");
				List<String> list = new ArrayList<>();
				list = Arrays.asList(array[i].split( ","));
				
				if(list.size()==4){
//					String FR = list.get(0);
					int floor = Integer.parseInt(list.get(1));
					String UD = list.get(2);
					int time  = Integer.parseInt(list.get(3));
					if(floor>10 || floor<1 || floor==1&&UD.equals("DOWN") || floor==10&&UD.equals("UP")){
						System.out.println("INVALID " + "[" + array[i] + "]");	//删除10层和1层的特殊情况以及不在楼层范围内的情况
						i++;
					}else{
						if(floor == 1 && UD.equals("UP") && time == 0){
							System.out.println("[" + array[i] + "]" +" / " + "(1,STILL,1.0)");
							queue.add(new Request());
							queue.get(0).setway(0);
							queue.get(0).setaimfloor(floor);
							queue.get(0).settime(0);
							queue.get(0).setdirection(0);
							queue.get(0).setTime(1);
							temp = time;
							i++;
							break;
						}else{
							System.out.println("INVALID " + "[" + array[i] + "]");
							i++;
						}
					}	
				}else{
					System.out.println("INVALID " + "[" + array[i] + "]");
					i++;
				}
			}
		}
	}		
		//
		
	void manage(String[] array, List<Request> queue){
		while(array[i]!=null){
			int valid = 1;
			int[] Ret = new int[2];
			Queue Queue = new Queue();
			Ret = Queue.addqueue(array[i],queue,i,k,temp,valid);	
			k = Ret[0];
			valid = Ret[1];
			if(valid == 1){
				array[i] = array[i].replaceAll("\\(","");
				array[i] = array[i].replaceAll("\\)","");
				if(k==0){
					queue.get(k).setTime(ele.run(queue.get(k), 0,array[i]));
				}else{
					queue.get(k).setTime(ele.run(queue.get(k), queue.get(k-1).getTime(),array[i]));
				}
			}
			k++;
			i++;
		}
		
	}
}