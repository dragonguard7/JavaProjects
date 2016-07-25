/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 1 – Synchronized, And Cooperating Threads
 * Due Date: June 1, 2012
*/
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank_Driver
{
   public static void main( String[] args )
   {
      // create new thread pool with two threads
      ExecutorService application = Executors.newFixedThreadPool( 11 );

      // create SynchronizedBuffer to store ints
      Buffer sharedLocation = new Buffer();
	
      System.out.printf( "%-40s%s\t\t%s\n%-40s%s\n\n", "Deposit Threads", 
         "Withdrawal Threads", "Balance", "---------", "---------------\t\t        --------" );

      try // try to start producer and consumer
      {  	  
         application.execute( new Deposit( sharedLocation, "Thread 1") );
         application.execute( new Deposit( sharedLocation, "Thread 2") );
         application.execute( new Deposit( sharedLocation, "Thread 3") );
         application.execute( new Withdraw( sharedLocation, "Thread 4" ) );
         application.execute( new Withdraw( sharedLocation, "Thread 5" ) );
         application.execute( new Withdraw( sharedLocation, "Thread 6" ) );
         application.execute( new Withdraw( sharedLocation, "Thread 7" ) );
         //application.execute( new Withdraw( sharedLocation, "Thread 8" ) );
         //application.execute( new Withdraw( sharedLocation, "Thread 9" ) );
         //application.execute( new Withdraw( sharedLocation, "Thread 10" ) );
         //application.execute( new Withdraw( sharedLocation, "Thread 11" ) );
      } // end try
      catch ( Exception exception )
      {
         exception.printStackTrace();
      } // end catch

      application.shutdown();
   } // end main
} // end class SharedBufferTest2



