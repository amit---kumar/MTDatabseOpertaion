/*
 * This sample is a  multi-threaded JDBC program.
 */

import java.sql.*;
//import oracle.jdbc.driver.OracleStatement;

public class JdbcMTSampleShared extends Thread
{
  // Default no of threads to 10
  private static int NUM_OF_THREADS = 5;

  int m_myId;

  static  int c_nextId = 1;
  static  Connection s_conn = null;
  static  boolean   share_connection = true;

  synchronized static int getNextId()
  {
      return c_nextId++;
  }

  public static void main (String []args)
  {
    try  
    {  
      /* Load the JDBC driver */
      //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      Class.forName("com.mysql.jdbc.Driver");
      // If NoOfThreads is specified, then read it
      if ((args.length > 2)  || 
           ((args.length > 1) && !(args[1].equals("share"))))
      {
         System.out.println("Error: Invalid Syntax. ");
         System.out.println("java JdbcMTSampleShared [NoOfThreads] [share]");
         System.exit(0);
      }

      if (args.length > 1) 
      {
         share_connection = true;
         System.out.println
                ("All threads will be sharing the same connection");
      }
  
      // get the no of threads if given
      if (args.length > 0)
         NUM_OF_THREADS = Integer.parseInt (args[0]);
  
      // get a shared connection
      if (share_connection)
          s_conn = DriverManager.getConnection
                                 ("jdbc:mysql://localhost:3306/user","root","amit");
  
      // Create the threads
      Thread[] threadList = new Thread[NUM_OF_THREADS];

      // spawn threads
      for (int i = 0; i < NUM_OF_THREADS; i++)
      {
          threadList[i] = new JdbcMTSampleShared();
          threadList[i].start();
      }
    
      // Start everyone at the same time
      //setGreenLight ();

      // wait for all threads to end
      for (int i = 0; i < NUM_OF_THREADS; i++)
      {
          threadList[i].join();
      }

      if (share_connection)
      {
          s_conn.close();
          s_conn = null;
      }
          
    }
    catch (Exception e)
    {
       e.printStackTrace();
    }
  
  }  

  public JdbcMTSampleShared()
  {
     super();
     // Assign an Id to the thread
     m_myId = getNextId();
  }

  public void run()
  {
    Connection conn = null;
    ResultSet     rs   = null;
    Statement  stmt = null;

    try
    {    
      // Get the connection

      if (share_connection)
        stmt = s_conn.createStatement (); // Create a Statement
      else
      {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","amit");
        stmt = conn.createStatement (); // Create a Statement
      }

/*      while (!getGreenLight())
        yield();*/
          
      // Execute the Query
      rs = stmt.executeQuery ("select * from emp");
          
      // Loop through the results
      while (rs.next())
      {
        System.out.println("Thread " + m_myId + 
                           " Employee Id : " + rs.getInt(1) + 
                           " Name : " + rs.getString(2));
       // yield();  // Yield To other threads
      }
          
      // Close all the resources
      rs.close();
      rs = null;
  
      // Close the statement
      stmt.close();
      stmt = null;
  
      // Close the local connection
      if ((!share_connection) && (conn != null))
      {
         conn.close();
         conn = null;
      }
      System.out.println("Thread " + m_myId +  " is finished. ");
    }
    catch (Exception e)
    {
      System.out.println("Thread " + m_myId + " got Exception: " + e);
      e.printStackTrace();
      return;
    }
  }

  static boolean greenLight = false;
  static synchronized void setGreenLight () { greenLight = true; }
  synchronized boolean getGreenLight () { return greenLight; }

}