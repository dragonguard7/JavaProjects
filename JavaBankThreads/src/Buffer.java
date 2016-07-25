/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 1 – Synchronized, And Cooperating Threads
 * Due Date: June 1, 2012
*/

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer implements Buffer_Interface
{
   // Lock to control synchronization with this buffer   
   private Lock accessLock = new ReentrantLock();
  

   // conditions to control reading and writing
   private Condition canWithdraw = accessLock.newCondition();

   private int balance = 0; // shared by producer and consumer threads
   //private boolean occupied = false; // whether buffer is occupied
   
   // place int value into buffer
   public void deposit( int valueDeposit, String name )
   {
      accessLock.lock(); // lock this object
               
      balance += valueDeposit; // set new buffer value
   
         System.out.print( name  + " deposits $" + valueDeposit );
         System.out.println("\t\t\t\t\t\t\tBalance is $"+ balance);

         // signal thread waiting to read from buffer
         canWithdraw.signalAll();
         accessLock.unlock(); // unlock this object	
       
         
   
   } // end method set
    
   // return value from buffer
   public int withdraw(int valueWithdraw, String name)
   {
      int readValue = 0; // initialize value read from buffer
      accessLock.lock(); // lock this object
      
      readValue = balance;
      
      // output thread information and buffer information, then wait
      try
      {
         // while no data to read, place thread in waiting state
         while ( balance < valueWithdraw ) 
         {
            System.out.println("\t\t\t\t\t" + name + " tries to withdraw $" + valueWithdraw +
            		" Withdrawal - Blocked - Insufficient funds");
            System.out.println("\t\t\t\t\t\t\t\t\tBalance is $" + balance);
				canWithdraw.await(); // wait until buffer is full
         } // end while


         balance =  balance - valueWithdraw; // retrieve value from buffer
         System.out.print( "\t\t\t\t\t" + name + " withdraws $" + valueWithdraw );
         System.out.println("\t\tBalance is $" + balance);
         Thread.yield();

 
      } // end try
      // if waiting thread interrupted, print stack trace
      catch ( InterruptedException exception ) 
      {
         exception.printStackTrace();
      } // end catch
      finally
      {
         accessLock.unlock(); // unlock this object
      } // end finally

      return readValue;
   } // end method get
    
} // end class SynchronizedBuffer

