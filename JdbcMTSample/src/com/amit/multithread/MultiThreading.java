package com.amit.multithread;

/*
 *
GOAL TO FIND THE BENEFIT OF MULTITHREADING

Single Thread

Batch of 4
Insertion Completed Fri Mar 03 12:04:40 IST 2017 Fri Mar 03 12:05:15 IST 2017 = 35 sec

Individual query
Insertion Completed Fri Mar 03 12:07:15 IST 2017 Fri Mar 03 12:08:00 IST 2017 = 45 sec

Batch of all
Insertion Completed Fri Mar 03 12:08:39 IST 2017 Fri Mar 03 12:09:24 IST 2017 = 45 sec



2 Thread
Final Completion Fri Mar 03 12:32:30 IST 2017 Fri Mar 03 12:33:04 IST 2017    = 34 sec 

4 thread
Final Completion Fri Mar 03 12:41:45 IST 2017 Fri Mar 03 12:42:03 IST 2017    = 18 sec 

 * */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MultiThreading {
	public static void main(String[] args) {
		List<String> query=new ArrayList<String>();
		query.add("insert into  employeeThread(id , name, sex) values(12,'xyz','m')");
		query.add("insert into  employeeThread(id , name, sex) values(13,'xya','m')");
		query.add("insert into  employeeThread(id , name, sex) values(14,'xyb','m')");
		query.add("insert into  employeeThread(id , name, sex) values(15,'xyc','m')");
		query.add("insert into  employeeThread(id , name, sex) values(16,'xyd','m')");
		query.add("insert into  employeeThread(id , name, sex) values(17,'xye','m')");
		
		String q="insert into  employeeThread(id , name, sex) values(17,'xye','m')";
		System.out.println("Prepairing Query List");

/*		for (int i = 1; i < 1000; i++) {
			query.add(q);
		}*/
		
		for (int i = 1; i < 250; i++) {
			query.add(q);
		}
		List<String> query2=new ArrayList<String>();
		for (int i = 250; i < 500; i++) {
			query2.add(q);
		}
		List<String> query3=new ArrayList<String>();
		for (int i = 500; i < 750; i++) {
			query3.add(q);
		}
		List<String> query4=new ArrayList<String>();
		for (int i = 750; i < 1000; i++) {
			query4.add(q);
		}
		System.out.println("size of query is "+ query.size());
		//InsertThread ob=new InsertThread(q);
		
/*		InsertThread ob=new InsertThread(query);
		Thread t1=new Thread(ob);
		t1.start();*/
		
		Date start=new Date();
		System.out.println("Final Completion "+ start);
		
		InsertThread ob=new InsertThread(query);
		Thread t1=new Thread(ob);
		t1.start();
		InsertThread ob2=new InsertThread(query2);
		Thread t2=new Thread(ob2);
		t2.start();
		InsertThread ob3=new InsertThread(query3);
		Thread t3=new Thread(ob3);
		t3.start();
		InsertThread ob4=new InsertThread(query4);
		Thread t4=new Thread(ob4);
		t4.start();
		
		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date end=new Date();
		System.out.println("Final Completion "+end);
	}
}

class InsertThread implements	Runnable{
	Connection conn=null;
	Statement  stmt = null;
	List<String>  query;
	//String q;
/*	public InsertThread(String q){
		this.q=q;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","amit"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public InsertThread(List<String> query){
		this.query=query;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","amit"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public void run() {
		try {
			stmt = conn.createStatement();
			stmt.execute(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}*/
	
/*	public void run() {
		try {
			Date start=new Date();
			stmt = conn.createStatement();
			for (String q : query) {
				stmt.addBatch(q);
			}
			stmt.executeBatch();
			Date end =new Date();
			System.out.println("Insertion Completed "+ start +" "+end);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	
/*	public void run() {
		try {
			Date start=new Date();
			stmt = conn.createStatement();
			int c=0;
			for (String q : query) {
				stmt.execute(q);
				System.out.println("Query executed so far "+ ++c);
			}
			Date end =new Date();
			System.out.println("Insertion Completed "+ start +" "+end);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	public void run() {
		try {
			Date start=new Date();
			stmt = conn.createStatement();
			for (int i = 0,c = 1; i < query.size(); i++,c++) {
				if(c!=5){
					stmt.addBatch(query.get(i));
				}
				else{
					stmt.executeBatch();
					stmt.clearBatch();
					c=1;
					System.out.println("query completed so far "+ i);
				}
			}
			Date end =new Date();
			System.out.println("Insertion Completed "+Thread.currentThread()+" "+ start +" "+end);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
} 
