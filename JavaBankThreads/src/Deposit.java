/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 1 – Synchronized, And Cooperating Threads
 * Due Date: June 1, 2012
*/
import java.util.Random;

public class Deposit implements Runnable 
{
   private static Random generator = new Random();
   private Buffer_Interface sharedLocation; // reference to shared object
   private String threadName;

   // constructor
   public Deposit( Buffer_Interface shared, String name )
   {
      sharedLocation = shared;
      threadName = name;
  
   } // end Producer constructor

   // store values from 1 to 10 in sharedLocation
   public void run()
   {
      for (;;) 
      {  
         try // sleep 0 to 3 seconds, then place value in Buffer
         {
            sharedLocation.deposit(generator.nextInt(200), threadName); // set value in buffer
            Thread.sleep( generator.nextInt(20)); // sleep thread 
          
      
         } // end try
         // if sleeping thread interrupted, print stack trace
         catch ( InterruptedException exception ) 
         {
            exception.printStackTrace();
         } // end catch
      } // end for

   } // end method run
   
} // end class Producer
