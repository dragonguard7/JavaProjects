/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 1 – Synchronized, And Cooperating Threads
 * Due Date: June 1, 2012
*/
import java.util.Random;

public class Withdraw implements Runnable 
{ 
   private static Random generator = new Random();
   private Buffer_Interface sharedLocation; // reference to shared object
   private String threadName;

   // constructor
   public Withdraw( Buffer_Interface shared, String name )
   {
      sharedLocation = shared;
      threadName = name;
      
   } // end Consumer constructor

   // read sharedLocation's value four times and sum the values
   public void run()
   {
      for (;;) 
      {      
    	  sharedLocation.withdraw(generator.nextInt(50), threadName);
          try {
        	//***********************************************************************************
        	//Processor was ignoring .yeild() command and ran multiple, so i had to sleep randomly
        	//***********************************************************************************
        	  
			Thread.sleep(generator.nextInt(4));
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

      } // end for

   } // end method run
} // end class Consumer




