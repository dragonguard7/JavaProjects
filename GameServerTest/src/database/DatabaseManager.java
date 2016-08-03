package database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class DatabaseManager extends JFrame{
	
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	   static final String DATABASE_URL = "jdbc:mysql://localhost:3306/gameusers";
	   static final String USERNAME = "root";
	   static final String PASSWORD= "root";
	   
	// default query retrieves all data from bikes table
	   private String DEFAULT_QUERY = "SELECT * FROM users";
	   
	   private dbResultSet tableModel;
	   private JScrollPane result;
	   
	   private JTextArea queryArea;
	   private JLabel exe = new JLabel("SQL Execution Result");
	   private JTextArea filler = new JTextArea(24,70);
	   private JLabel commandLabel = new JLabel("Enter a SQL Command");
	   private JButton submitButton = new JButton( "Submit Query" );
	   private JButton clearCommand = new JButton( "Clear Command" );
	   private JButton clearResult = new JButton("Clear Result");
	   
	   private char[] string;
	   
	   public DatabaseManager(){
		   dbGUI();
	   }
	   public DatabaseManager(String query){
		   //DEFAULT_QUERY = query;
		   //dbGUI();
		   query(query);
	   }
	   public dbResultSet query(String query){
			   
			   try{
					tableModel = new dbResultSet( JDBC_DRIVER, DATABASE_URL, USERNAME, PASSWORD, query );
	
					}//end try
					catch ( ClassNotFoundException classNotFound ) 
				      {
						System.out.println("Driver not found");
				      } // end catch
				      catch ( SQLException sqlException ) 
				      {
				         System.out.println("Database error");
				               
				         // ensure database connection is closed
				         tableModel.disconnectFromDatabase();
				      } // end catch
			   
			   return tableModel;
	}

	   // create ResultSetTableModel and GUI
	   private void dbGUI() 
	   {   
		  //this.setLayout(new GridLayout(2,1,20,10));
		   this.setLayout(new BorderLayout());
		   setTitle("SQL Client GUI");
	       setSize( 850, 700 ); // set window size
	       setVisible( true ); // display window 
	      

	      JPanel p2 = new JPanel();
	      JPanel commandLayout = new JPanel();

	      JPanel center = new JPanel();
	      JPanel centerP1 = new JPanel();
	      JPanel centerP2 = new JPanel();
	      final JPanel south = new JPanel();
	      
	      p2.setLayout(new GridLayout(1,1,5,5));
	      commandLayout.setLayout(new GridLayout(4,1,5,5));
	      centerP1.setLayout(new FlowLayout());
	      centerP2.setLayout(new FlowLayout());
	      center.setLayout(new GridLayout(2,1,5,5));
	      
	      filler.setBackground(Color.LIGHT_GRAY);
	      south.setLayout(new FlowLayout());

	      
	         queryArea = new JTextArea( "", 3, 100 );
	         queryArea.setWrapStyleWord( true );
	         queryArea.setLineWrap( true );
	         queryArea.setText(DEFAULT_QUERY);
	        
	         JScrollPane scrollPane = new JScrollPane( queryArea,
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
	         //p2.add(p1);
	         p2.add(commandLayout);
	         
	         commandLayout.add(commandLabel);
	         commandLayout.add(scrollPane);
	         	         
	      centerP1.add(clearCommand);
	      centerP1.add(submitButton);
	      commandLayout.add(centerP1);
	      
	      centerP2.add(exe);
	      centerP2.add(clearResult);
	      commandLayout.add(centerP2);
	   
	      south.add(filler);
	      add(p2, BorderLayout.NORTH);
	      add(south, BorderLayout.SOUTH);

	      try{
			tableModel = new dbResultSet( JDBC_DRIVER, DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY );
			
	         JTable resultTable = new JTable( tableModel );
	         result = new JScrollPane( resultTable );

			}//end try
			catch ( ClassNotFoundException classNotFound ) 
		      {
		         JOptionPane.showMessageDialog( null, 
		            "MySQL driver not found", "Driver not found",
		            JOptionPane.ERROR_MESSAGE );
		         
		         System.exit( 1 ); // terminate application
		      } // end catch
		      catch ( SQLException sqlException ) 
		      {
		         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
		            "Database error", JOptionPane.ERROR_MESSAGE );
		               
		         // ensure database connection is closed
		         tableModel.disconnectFromDatabase();
		         
		         System.exit( 1 );   // terminate application
		      } // end catch
	      
	      string = (queryArea.getText()).toCharArray();
   	   //System.out.println(string[0]);
         // perform a new query
         try{
       	  
       	 if(string[0] == 'I' || string[0] == 'i' || string[0] == 'D' || string[0] == 'd'){
       	 tableModel.setUpdate( queryArea.getText() );
       	     filler.setText("Update Complete");
                south.remove(result);
                south.add(filler);
                south.revalidate();
       	 }
       	 else{
       		 tableModel.setQuery( queryArea.getText() );
                south.removeAll();
                south.add(result);
                south.revalidate();

       	 }
           
         } // end try
         catch ( SQLException sqlException ){
            JOptionPane.showMessageDialog( null, 
               sqlException.getMessage(), "Database error", 
               JOptionPane.ERROR_MESSAGE );
            
            // try to recover from invalid user query 
            // by executing default query
            try{
               tableModel.setQuery( DEFAULT_QUERY );
               queryArea.setText( DEFAULT_QUERY );
            } // end try
            catch ( SQLException sqlException2 ){
               JOptionPane.showMessageDialog( null, 
                  sqlException2.getMessage(), "Database error", 
                  JOptionPane.ERROR_MESSAGE );

               // ensure database connection is closed
               tableModel.disconnectFromDatabase();

               System.exit( 1 ); // terminate application
            } // end inner catch     
            
         } // end outer catch
	  
	   	            
	          submitButton.addActionListener(new ActionListener(){
	               // pass query to table model
	               public void actionPerformed( ActionEvent event )
	               {
	            	   
	            	   
	            	   string = (queryArea.getText()).toCharArray();
	            	   //System.out.println(string[0]);
	                  // perform a new query
	                  try{
	                	  
	                	 if(string[0] == 'I' || string[0] == 'i' || string[0] == 'D' || string[0] == 'd'){
	                	 tableModel.setUpdate( queryArea.getText() );
	                	     filler.setText("Update Complete");
	                         south.remove(result);
	                         south.add(filler);
	                         south.revalidate();
	                	 }
	                	 else{
	                		 tableModel.setQuery( queryArea.getText() );
	                         south.removeAll();
	                         south.add(result);
	                         south.revalidate();
	    
	                	 }
	                     //south.add(new JScrollPane( resultTable ), BorderLayout.SOUTH);
	                     
	                  } // end try
	                  catch ( SQLException sqlException ){
	                     JOptionPane.showMessageDialog( null, 
	                        sqlException.getMessage(), "Database error", 
	                        JOptionPane.ERROR_MESSAGE );
	                     
	                     // try to recover from invalid user query 
	                     // by executing default query
	                     try{
	                        tableModel.setQuery( DEFAULT_QUERY );
	                        queryArea.setText( DEFAULT_QUERY );
	                     } // end try
	                     catch ( SQLException sqlException2 ){
	                        JOptionPane.showMessageDialog( null, 
	                           sqlException2.getMessage(), "Database error", 
	                           JOptionPane.ERROR_MESSAGE );
	         
	                        // ensure database connection is closed
	                        tableModel.disconnectFromDatabase();
	         
	                        System.exit( 1 ); // terminate application
	                     } // end inner catch     
	                     
	                  } // end outer catch
	                  
	                  
	                  
	               } // end actionPerformed
	            }  // end ActionListener inner class          
	         ); // end call to addActionListener
	      
	      // ensure database connection is closed when user quits application
	      addWindowListener(
	      
	         new WindowAdapter() 
	         {
	            // disconnect from database and exit when window has closed
	            public void windowClosed( WindowEvent event )
	            {
	               tableModel.disconnectFromDatabase();
	               System.exit( 0 );
	            } // end method windowClosed
	         } // end WindowAdapter inner class
	      ); // end call to addWindowListener
	  
	      
	      clearResult.addActionListener(new ActionListener(){
	    		
	  		public void actionPerformed(ActionEvent e){
	  			
	  			south.remove(result);
	  			filler.setText("");
	  			south.add(filler);
	  			south.revalidate();
	  			}
	  			});      
	      
	      clearCommand.addActionListener(new ActionListener(){
	  		
	  		public void actionPerformed(ActionEvent e){
	  					
	  			queryArea.setText(" ");
	  			}
	  			});
	      
	      
	      
	   } // end DisplayQueryResults constructor

}
