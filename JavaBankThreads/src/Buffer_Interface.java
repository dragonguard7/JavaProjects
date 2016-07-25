/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 1 – Synchronized, And Cooperating Threads
 * Due Date: June 1, 2012
*/
public interface Buffer_Interface
{
   public void deposit( int valueDeposit, String name ); // place int value into Buffer
   public int withdraw(int valueWithdraw, String name); // return int value from Buffer
} // end interface Buffer


