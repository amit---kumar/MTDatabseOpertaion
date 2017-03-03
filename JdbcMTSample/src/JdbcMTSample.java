import java.sql.*;


public class JdbcMTSample extends Thread
{
    // Set default number of threads to 10
    private static int NUM_OF_THREADS = 10;

    int m_myId;

    static     int c_nextId = 1;
    static  Connection s_conn = null;

    synchronized static int getNextId()
    {
        return c_nextId++;
    }

    public static void main (String args [])
    {
        try  
        {  
            // Load the JDBC driver //
        	Class.forName("com.mysql.jdbc.Driver");
    
            // If NoOfThreads is specified, then read it
            if (args.length > 1) {
                System.out.println("Error: Invalid Syntax. ");
                System.out.println("java JdbcMTSample [NoOfThreads]");
                System.exit(0);
            }
            else if (args.length == 1)
                NUM_OF_THREADS = Integer.parseInt (args[0]);
    
            // Create the threads
            Thread[] threadList = new Thread[NUM_OF_THREADS];

            // spawn threads
            for (int i = 0; i < NUM_OF_THREADS; i++)
            {
                threadList[i] = new JdbcMTSample();
                threadList[i].start();
            }

            // wait for all threads to end
            for (int i = 0; i < NUM_OF_THREADS; i++)
            {
                    threadList[i].join();
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }  
 
    public JdbcMTSample()
    {
       super();
       // Assign an ID to the thread
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
          conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","amit");

            // Create a Statement
            stmt = conn.createStatement ();
            
            // Execute the Query
            rs = stmt.executeQuery ("SELECT * FROM emp");
            
            // Loop through the results
            while (rs.next())
                System.out.println("Thread " + m_myId + 
                                    " Employee Id : " + rs.getInt(1) + 
                                    " Name : " + rs.getString(2));
            
            // Close all the resources
            rs.close();
              stmt.close();
            if (conn != null)
                conn.close();
            System.out.println("Thread " + m_myId +  " is finished. ");
      }
      catch (Exception e)
      {
          System.out.println("Thread " + m_myId + " got Exception: " + e);
          e.printStackTrace();
          return;
      }
  }

}