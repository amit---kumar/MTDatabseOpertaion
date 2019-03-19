
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
class Employee{
	int id;
	String name,sex;
	Employee(int id, String name, String sex){
		this.id=id;
		this.name=name;
		this.sex=sex;
	}
	public int getEmpid() {
		return id;
	}
	public void setEmpid(int i) {
		this.id = id;
	}
	public String getEname() {
		return name;
	}
	public void setEname(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}	
}
class JdbcConnection {
    String selectSQL = "insert into  employeeThread(id , name, sex) values(?,?,?)";
    static Connection connection;
    PreparedStatement preparedStatement=null;
    int incr=0;
    int totSize=0;
    static{
         try{
        System.out.println("Connecting to the database...");  
        Class.forName("com.mysql.jdbc.Driver");
         connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","amit"); 
         connection.setAutoCommit(false);
         } catch(Exception e){
             e.printStackTrace();
         }
    }
     
    public void insertValues(Employee employee, int size){
         
        synchronized (this) {
/*        incr++;
        totSize++;*/
         try{
         System.out.println("Inside insert "+Thread.currentThread().getName()+" : "+employee);
         preparedStatement = connection.prepareStatement(selectSQL);        
         preparedStatement.setInt(1, employee.getEmpid());
         preparedStatement.setString(2, employee.getEname());
         preparedStatement.setString(3, employee.getSex());
         preparedStatement.addBatch();
         
/*         System.out.println("inside tyr "+totSize+" : "+employee.getEmpid());
         System.out.println(Thread.currentThread().getName());
         if(incr==5){
             System.out.println("incr=5 "+totSize+" : "+employee.getEmpid());
             System.out.println(Thread.currentThread().getName());
             preparedStatement.executeBatch();
             connection.commit();
            incr=0;
         }else if (size==totSize){
        	 System.out.println("size=totsize");
        	 System.out.println(Thread.currentThread().getName());
             preparedStatement.executeBatch();
             connection.commit();
         }*/
         preparedStatement.executeBatch();
         connection.commit();
         
         } catch(Exception e){
             e.printStackTrace();
         }
        }
    }
}
 
public class EmployeeThread implements Runnable {
 
    JdbcConnection jdbcConnection = new JdbcConnection();
    private List<Employee> employeeList;
          
    EmployeeThread(){
        employeeList = new ArrayList<Employee>();      
    }
    public void run(){       
        Iterator<Employee> itr =  employeeList.iterator();
        while(itr.hasNext()){
        	Employee emp = itr.next();
        	System.out.println("Inside run "+Thread.currentThread().getName()+" : "+emp);   
        	jdbcConnection.insertValues(emp,employeeList.size());
        }
    }
    public static void main(String[] args) {
        EmployeeThread employeeThread =  new EmployeeThread();
        Employee employee = null;
         employee = new Employee(123,"ewr","wer");
        employeeThread.employeeList.add(employee);
         employee = new Employee(124,"ewr","xg");
        employeeThread.employeeList.add(employee);
         employee = new Employee(125,"dfg","rew");
        employeeThread.employeeList.add(employee);
         employee = new Employee(126,"iu","e");
        employeeThread.employeeList.add(employee);
         employee = new Employee(127,"sdf","sdf");
        employeeThread.employeeList.add(employee);
         employee = new Employee(128,"sd","xvcx");
        employeeThread.employeeList.add(employee);
         employee = new Employee(129,"xcv","xcv");
        employeeThread.employeeList.add(employee);
         employee = new Employee(130,"dsf","sdfs");
        employeeThread.employeeList.add(employee);
         employee = new Employee(131,"sdf","sdfsd");
        employeeThread.employeeList.add(employee);
         employee = new Employee(132,"ewr","ewr");
        employeeThread.employeeList.add(employee);
         employee = new Employee(133,"were","ewr");
        employeeThread.employeeList.add(employee);
         employee = new Employee(134,"wer","ewr");
        employeeThread.employeeList.add(employee);       
        Thread thread1;
        Thread thread2;
        Thread thread3;
         thread1 = new Thread(employeeThread,"Thread1");
         thread2 = new Thread(employeeThread,"Thread2");
         thread3 = new Thread(employeeThread,"Thread3");
         thread1.start();
         thread2.start();
         thread3.start();
         
    }	
}

class Test{
	int a=0;
}