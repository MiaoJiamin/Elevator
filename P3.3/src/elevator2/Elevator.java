package elevator2;

class Elevator {
	private int curfloor;
	Elevator(){
		curfloor = 1;
	}
	
	float run(Request ask, float time, String array){//time 前一个指令执行完的时间
		float maxtime;
		int aimfloor = ask.getaimfloor();
		
		if(ask.gettime() > time){
			maxtime = ask.gettime();
		}else
			maxtime = time;
		
		float arrivetime = Math.abs(aimfloor-curfloor) * (float)0.5 + maxtime;
		if(curfloor > aimfloor){
			System.out.println("["+array+"]"+" / "+"("+aimfloor+","+"DOWN"+","+arrivetime+")");
		}else if(curfloor < aimfloor){
			System.out.println("["+array+"]"+" / "+"("+aimfloor+","+ "UP" +","+arrivetime+")");
		}else{
			System.out.println("["+array+"]"+" / "+"("+aimfloor+ ","+"STILL"+","+(arrivetime+1)+")");
		} 
		curfloor = aimfloor;
		arrivetime += 1;
		
		return arrivetime;
	}
}
