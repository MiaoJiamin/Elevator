package elevator2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class override extends Scheduler {
	
	//在父类中找到第一个(FR,1,UP,0)请求并执行
	int state;	// 0 UP;  1 DOWN; 2 STILL
	int curfloor = 1;
	float curtime = 1;
	
	void manage(String[] array, List<Request> queue){
		int k = 0;
		List<String> reqlist = new ArrayList<>();
		
		while(array[i]!=null){
			reqlist.add(array[i]);
			i++;
		}
	
		while(!reqlist.isEmpty()){		//主请求
			/*在这里判断是否为有效请求  括号 浮点数 关键字不正确 */
			String strr1 = "\\(FR,\\+?\\d+,(UP|DOWN)?,\\+?\\d+\\)";
			String strr2 = "\\(ER,\\+?\\d+,\\+?\\d+\\)";
			Pattern pattern1 = Pattern.compile(strr1);
			Matcher matcher1 = pattern1.matcher(reqlist.get(k));
			Pattern pattern2 = Pattern.compile(strr2);
			Matcher matcher2 = pattern2.matcher(reqlist.get(k));
			
			String str = null;
			String array1 = null;
			if(matcher1.matches()==false && matcher2.matches()==false){		//删除括号不匹配，输入时间、楼层有浮点数
				array1 = reqlist.get(k).replaceAll("\\(", "\\[");				//关键字不正确的情况
				array1 = array1.replaceAll("\\)", "\\]");
				System.out.println("INVALID " + array1);
				reqlist.remove(k);
				continue;
			}else{
				String str1 = reqlist.get(k).replaceAll("\\(","");
				str1 = str1.replaceAll("\\)","");
				List<String> list = new ArrayList<>();
				list = Arrays.asList(str1.split(","));
				
				int flag = 0;
				int infloor = 1;
				
				if(list.size()==3){
					int aimfloor = Integer.parseInt(list.get(1));
					int time = Integer.parseInt(list.get(2));
					/*删除楼层10 1 等特殊情况*/
					if(aimfloor>10 || aimfloor < 1){
						System.out.println("INVALID " + "[" + str1 + "]");
						reqlist.remove(k);
						continue;
					}else if(aimfloor == curfloor && time <= curtime){
						System.out.println("SAME " + "[" + str1 + "]");
						reqlist.remove(k);
						continue;
					}else{
					
						if(aimfloor > curfloor){
							state = 0;
						}else if(aimfloor < curfloor){
							state = 1;
						}else{
							state = 2;
						}
						
						if(time > curtime)	curtime = time;
						if(state==0){
							for(curfloor+=1;curfloor<=aimfloor;curfloor++){
								curtime += 0.5;
								flag = 0;
								str = null;
								for(int j=k+1;j<=reqlist.size()-1;j++){			//捎带请求
									String str2 = reqlist.get(j).replaceAll("\\(","");
									str2 = str2.replaceAll("\\)","");
									List<String> list1 = new ArrayList<>();
									list1 = Arrays.asList(str2.split(","));
									
									if(list1.size()==4){
										int floor = Integer.parseInt(list1.get(1));
										int direction = (list1.get(2).equals("UP")) ? 0 : 1;
										int time1 = Integer.parseInt(list1.get(3));
										/*判断是否为SAME或者时间顺序是否正确*/
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(floor==curfloor && state==direction && time1<curtime){
												flag = 1;
												str = str2;
												infloor = floor;
												if(state == 0){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}else if(state == 1){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}
											}
											if(time1 >= curtime){
												break;
											}
										}
									}else if(list1.size()==3){
										int floor = Integer.parseInt(list1.get(1));
										int time1 = Integer.parseInt(list1.get(2));
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(floor==aimfloor && time1 <= curtime){
												System.out.println("SAME " + "[" + str2 + "]");
												reqlist.remove(j);
												continue;
											}else{
												if(time1<curtime && floor==curfloor){
													flag = 1;
													str = str2;
													infloor = floor;
													if(state == 0){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}else if(state == 1){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}
												}
												if(time1 >= curtime){
													break;
												}
											}
										}
									}
						
								}//for j	

								if(flag == 1){
									curtime ++;
								}
							}//for
							
							if(flag == 1 && infloor == aimfloor){
								curtime --;
							}

							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"UP"+","+curtime+")");
							reqlist.remove(k);
							curtime ++;
							curfloor --;

						}else if(state == 1){
							for(curfloor-=1;curfloor>=aimfloor;curfloor--){
								curtime += 0.5;
								flag = 0;
								str = null;
								for(int j=k+1;j<=reqlist.size()-1;j++){			//捎带请求
									String str2 = reqlist.get(j).replaceAll("\\(","");
									str2 = str2.replaceAll("\\)","");
									List<String> list1 = new ArrayList<>();
									list1 = Arrays.asList(str2.split(","));
									
									if(list1.size()==4){
										int floor = Integer.parseInt(list1.get(1));
										int direction = (list1.get(2).equals("UP")) ? 0 : 1;
										int time1 = Integer.parseInt(list1.get(3));
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
										
											if(floor==curfloor && state==direction && time1<curtime){
												flag = 1;
												str = str2;
												infloor = floor;
												if(state==0){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}else if(state == 1){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}
											}
											if(time1 >= curtime){
												break;
											}
										}
									}else if(list1.size()==3){
										int floor = Integer.parseInt(list1.get(1));
										int time1 = Integer.parseInt(list1.get(2));
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(floor==aimfloor && time1 <= curtime){
												System.out.println("SAME " + "[" + str2 + "]");
												reqlist.remove(j);
												continue;
											}else{
												if(time1<curtime && floor==curfloor){
													flag = 1;
													str = str2;
													infloor = floor;
													if(state==0){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}else if(state == 1){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}
												}
												if(time1 >= curtime){
													break;
												}
											}
										}
									}
						
								}//for j	

								if(flag == 1){
									curtime ++;
								}
							}
							
							if(flag == 1 && infloor == aimfloor){
								curtime --;
							}

							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"DOWN"+","+curtime+")");
							reqlist.remove(k);
							curtime ++;
							curfloor ++;

						}else if(state==2){
							curtime ++;
							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"STILL"+","+curtime+")");
							reqlist.remove(k);
							
							for(int j=k;j<=reqlist.size()-1;j++){
								String str2 = reqlist.get(j).replaceAll("\\(","");
								str2 = str2.replaceAll("\\)","");
								List<String> list1 = new ArrayList<>();
								list1 = Arrays.asList(str2.split(","));
								
								if(list1.size()==4){
									int time1 = Integer.parseInt(list1.get(3));
									if(time1 < time){
										System.out.println("INVALID " + "[" + str2 + "]");
										reqlist.remove(j);
										continue;
									}
								}else if(list1.size()==3){
									int time1 = Integer.parseInt(list1.get(2));
									if(time1 < time){
										System.out.println("INVALID " + "[" + str2 + "]");
										reqlist.remove(j);
										continue;
									}
								}
							}
						}
					}
				}else if(list.size()==4){
					int aimfloor = Integer.parseInt(list.get(1));
					int time = Integer.parseInt(list.get(3));
					int Dir = list.get(2).equals("UP") ? 0 : 1;
					
					int flag1 = 0;
					if(Dir != state && reqlist.size() > 1){
						String str4 = reqlist.get(k+1).replaceAll("\\(","");
						str4 = str4.replaceAll("\\)","");
						List<String> list3 = new ArrayList<>();
						list3 = Arrays.asList(str4.split(","));
						
						if(list3.size()==4){
							flag1 = 1;
							aimfloor = Integer.parseInt(list3.get(1));
							time = Integer.parseInt(list3.get(3));
							Dir = list3.get(2).equals("UP") ? 0 : 1;
							
						}else if(list3.size()==3){
							aimfloor = Integer.parseInt(list3.get(1));
							time = Integer.parseInt(list3.get(2));
							if(Dir==1)	Dir = 0;
							else if(Dir==0)	Dir = 1;
						}
						
						if(time >= curtime-1){
							str4 = str1;
							List<String> list4 = new ArrayList<>();
							list4 = Arrays.asList(str4.split(","));
							
							if(list4.size()==4){
								aimfloor = Integer.parseInt(list4.get(1));
								time = Integer.parseInt(list4.get(3));
								Dir = list4.get(2).equals("UP") ? 0 : 1;
								
							}else if(list4.size()==3){
								aimfloor = Integer.parseInt(list4.get(1));
								time = Integer.parseInt(list4.get(2));
								if(Dir==1)	Dir = 0;
								else if(Dir==0)	Dir = 1;
							}
							
						}else{
							k++;
						}
						
						str1 = str4;
					}
					
					if((aimfloor>10 || aimfloor < 1 || aimfloor==10&&Dir==0 || aimfloor==1&&Dir==1) && flag1 == 1){
						System.out.println("INVALID " + "[" + str1 + "]");
						reqlist.remove(k);
						continue;
					}else if(aimfloor == curfloor && time <= curtime && Dir==state){
						System.out.println("SAME " + "[" + str1 + "]");
						reqlist.remove(k);
						continue;
					}else{
					
						if(aimfloor > curfloor){
							state = 0;
						}else if(aimfloor < curfloor){
							state = 1;
						}else{
							state = 2;
						}
						
						if(time > curtime)	curtime = time;
						
						if(state==0){
							for(curfloor+=1;curfloor<=aimfloor;curfloor++){
								curtime += 0.5;
								flag = 0;
								str = null;
								for(int j=k+1;j<=reqlist.size()-1;j++){			//捎带请求
									String str2 = reqlist.get(j).replaceAll("\\(","");
									str2 = str2.replaceAll("\\)","");
									List<String> list1 = new ArrayList<>();
									list1 = Arrays.asList(str2.split(","));
									
									if(list1.size()==4){
										int floor = Integer.parseInt(list1.get(1));
										int direction = (list1.get(2).equals("UP")) ? 0 : 1;
										int time1 = Integer.parseInt(list1.get(3));
										
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(floor==aimfloor && direction==Dir && time1<curtime){
												System.out.println("SAME " + "[" + str2 + "]");
												reqlist.remove(j);
												continue;
											}else{
												if(floor==curfloor && state==direction && time1<curtime){
													flag = 1;
													str = str2;
													infloor = floor;
													if(state==0){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}else if(state == 1){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}
												}
												if(time1 >= curtime){
													break;
												}
											}
										}
									}else if(list1.size()==3){
										int floor = Integer.parseInt(list1.get(1));
										int time1 = Integer.parseInt(list1.get(2));
										
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(time1<curtime && floor==curfloor){
												flag = 1;
												str = str2;
												infloor = floor;
	
												if(state==0){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}else if(state == 1){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}
											}
											if(time1 >= curtime){
												break;
											}
										}
									}
						
								}//for j	
								if(flag == 1){
									curtime ++;
								}
							}//for
							
							if(flag == 1 && infloor == aimfloor){
								curtime --;
							}
	
							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"UP"+","+curtime+")");
							reqlist.remove(k);
							curtime ++;
							curfloor --;
	
						}else if(state == 1){
							for(curfloor-=1;curfloor>=aimfloor;curfloor--){
								curtime += 0.5;
								flag = 0;
								str = null;
								for(int j=k+1;j<=reqlist.size()-1;j++){			//捎带请求
									String str2 = reqlist.get(j).replaceAll("\\(","");
									str2 = str2.replaceAll("\\)","");
									List<String> list1 = new ArrayList<>();
									list1 = Arrays.asList(str2.split(","));
									
									if(list1.size()==4){
										int floor = Integer.parseInt(list1.get(1));
										int direction = (list1.get(2).equals("UP")) ? 0 : 1;
										int time1 = Integer.parseInt(list1.get(3));
										
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{
											if(floor==aimfloor && direction==Dir && time1<curtime){
												System.out.println("SAME " + "[" + str2 + "]");
												reqlist.remove(j);
												continue;
											}else{
												if(floor==curfloor && state==direction && time1<curtime){
													flag = 1;
													str = str2;
													infloor = floor;
													if(state==0){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}else if(state == 1){
														System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
														reqlist.remove(j);
														j--;
													}
												}
												if(time1 >= curtime){
													break;
												}
											}
										}
									}else if(list1.size()==3){
										int floor = Integer.parseInt(list1.get(1));
										int time1 = Integer.parseInt(list1.get(2));
										
										if(time1 < time){
											System.out.println("INVALID " + "[" + str2 + "]");
											reqlist.remove(j);
											continue;
										}else{									
											if(time1<curtime && floor==curfloor){
												flag = 1;
												str = str2;
												infloor = floor;
												if(state==0){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"UP"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}else if(state == 1){
													System.out.println("["+str+"]"+" / "+"("+curfloor+","+"DOWN"+","+curtime+")");
													reqlist.remove(j);
													j--;
												}
											}
											if(time1 >= curtime){
												break;
											}
										}
									}
						
								}//for j	
	
								if(flag == 1){
									curtime ++;
								}
							}
							
							if(flag == 1 && infloor == aimfloor){
								curtime --;
							}
	
							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"DOWN"+","+curtime+")");
							reqlist.remove(k);
							curtime ++;
							curfloor ++;
	
						}else if(state==2){
							curtime ++;
							System.out.println("["+str1+"]"+" / "+"("+aimfloor+","+"STILL"+","+curtime+")");
							reqlist.remove(k);
							
							for(int j=k;j<=reqlist.size()-1;j++){
								String str2 = reqlist.get(j).replaceAll("\\(","");
								str2 = str2.replaceAll("\\)","");
								List<String> list1 = new ArrayList<>();
								list1 = Arrays.asList(str2.split(","));
								
								if(list1.size()==4){
									int time1 = Integer.parseInt(list1.get(3));
									if(time1 < time){
										System.out.println("INVALID " + "[" + str2 + "]");
										reqlist.remove(j);
										continue;
									}
								}else if(list1.size()==3){
									int time1 = Integer.parseInt(list1.get(2));
									if(time1 < time){
										System.out.println("INVALID " + "[" + str2 + "]");
										reqlist.remove(j);
										continue;
									}
								}
							}
						}
					}
				}
			}
			k = 0;
		}//while
	}
}

 