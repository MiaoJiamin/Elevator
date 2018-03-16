package elevator2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Queue {
	int[] addqueue(String array , List<Request> queue, int i, int k, int temp, int valid){
		String str1 = "\\(FR,\\d+,(UP|DOWN)?,\\d+\\)";
		String str2 = "\\(ER,\\d+,\\d+\\)";
		Pattern pattern1 = Pattern.compile(str1);
		Matcher matcher1 = pattern1.matcher(array);
		Pattern pattern2 = Pattern.compile(str2);
		Matcher matcher2 = pattern2.matcher(array);
			
		if(matcher1.matches()==false && matcher2.matches()==false){		//删除括号不匹配，输入时间、楼层有浮点数
			array = array.replaceAll("\\(", "\\[");				//关键字不正确的情况
			array = array.replaceAll("\\)", "\\]");
			System.out.println("INVALID " + array);
			valid = 0;
		}else{
			array = array.replaceAll("\\(", "");
			array = array.replaceAll("\\)", "");
			List<String> list = new ArrayList<>();
			list = Arrays.asList(array.split( ","));
				
			if(list.size()==4){
//				String FR = list.get(0);
				int floor = Integer.parseInt(list.get(1));
				String UD = list.get(2);
				int direction = UD.equals("UP") ? 0 : 1;
				int time  = Integer.parseInt(list.get(3));
				if(floor>10 || floor<1 || floor==1&&UD.equals("DOWN") || floor==10&&UD.equals("UP")){
					System.out.println("INVALID " + "[" + array + "]");	//删除10层和1层的特殊情况以及不在楼层范围内的情况
					valid = 0;
				}else{
					if(time<temp){
						System.out.println("INVALID " + "[" + array + "]");
						valid = 0;
					}else{
						queue.add(new Request());
						queue.get(k).setway(0);
		//				queue.get(k).setfloor(floor);
						queue.get(k).setaimfloor(floor);
						queue.get(k).settime(time);
						queue.get(k).setdirection(direction);
						temp = time;
							
						for(int j=0;j<k;j++){
							if(queue.get(k).getway()==queue.get(j).getway()){
								if(queue.get(k).getway()==0 && queue.get(k).getaimfloor()==queue.get(j).getaimfloor() 
										&& queue.get(k).getdirection()==queue.get(j).getdirection()
										&& queue.get(k).gettime() <= queue.get(j).getTime()){
									System.out.println("SAME " + "[" + array + "]");
									queue.remove(k);
									k--;
									valid = 0;
								}
								if(queue.get(k).getway()==1 && queue.get(k).getaimfloor()==queue.get(j).getaimfloor() 
										&& queue.get(k).gettime() <= queue.get(j).getTime()){
									System.out.println("SAME " + "[" + array + "]");
									queue.remove(k);
									k--;
									valid = 0;
								}	
							}
						}//for
					}
				}
			}else{
//				String ER = list.get(0);
				int floor = Integer.parseInt(list.get(1));
				int time = Integer.parseInt(list.get(2));
				if(floor>10 || floor<1){
					System.out.println("INVALID " + "[" + array + "]");	
					valid = 0;
				}else{
					if(time<temp){
						System.out.println("INVALID " + "[" + array + "]");
						valid = 0;
					}else{
						queue.add(new Request());
						queue.get(k).setway(1);
						queue.get(k).settime(time);
						queue.get(k).setaimfloor(floor);
		//				queue.get(k).setfloor(1);
						queue.get(k).setdirection(0);
						temp = time;
							
						for(int j=0;j<k;j++){
							if(queue.get(k).getway()==queue.get(j).getway()){
								if(queue.get(k).getway()==0 && queue.get(k).getaimfloor()==queue.get(j).getaimfloor() 
										&& queue.get(k).getdirection()==queue.get(j).getdirection()
										&& queue.get(k).gettime() <= queue.get(j).getTime()){
									System.out.println("SAME " + "[" + array + "]");
									queue.remove(k);
									k--;
									valid = 0;
								}
								if(queue.get(k).getway()==1 && queue.get(k).getaimfloor()==queue.get(j).getaimfloor() 
										&& queue.get(k).gettime() <= queue.get(j).getTime()){
									System.out.println("SAME " + "[" + array + "]");
									queue.remove(k);
									k--;
									valid = 0;
								}	
							}
						}//for
					}//else
				}//else
			}//else
		}//else	
		
		int[] ret = new int[2];
		ret[0] = k;
		ret[1] = valid;
		return ret;
	}//addqueue
}//Queue
			
			
			
			

