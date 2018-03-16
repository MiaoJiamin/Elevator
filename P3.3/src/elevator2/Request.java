package elevator2;

class Request {
	private int way;		//FR 0, ER 1
//	private int floor;		//请求发出的楼层
	private int direction;	//UP 0, DOWN 1
	private float time;		//请求发出的时间
	private int aimfloor;	//目标楼层
	private float Time=0; 	//请求完成的时间
	
	int getway(){
		return way;
	}
	void setway(int a){
		way = a;
	}
	
//	int getfloor(){
//		return floor;
//	}
//	void setfloor(int a){
//		floor = a;
//	}
	
	int getdirection(){
		return direction;
	}
	void setdirection(int a){
		direction = a;
	}
	
	float  gettime(){
		return time;
	}
	void settime(float a){
		time = a;
	}
	
	int getaimfloor(){
		return aimfloor;
	}
	void setaimfloor(int a){
		aimfloor = a;
	}
	
	float getTime(){
		return Time;
	}
	void setTime(float a){
		Time = a;
	}
	
}
