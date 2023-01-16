import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ReviewMySQL 
{
	// Instance Variables:
	private static Connection connect = null; // Used to establish the connection to the Data Base.
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	
	/**
	 * 
	 * Used to establish connection to MySQL Data Base
	 * 
	 * data base name: dbname
	 * user name: username
	 * password: password
	 * 
	 * @return Connection to MySQL Data Base, or null if connection unsuccessful.
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception
	{
		// Local Variables:

		
	    // This will load the MySQL driver, each DB has its own driver:
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch(ClassNotFoundException cn1)
		{
			System.out.println("Class Not Found");
		} // End of the try-catch load the MySQL Driver.
		// Setup the connection with the DB:
		try
		{
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname", "username", "password");
		    return connect;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		} // End of the try-catch set up connection to the dbname MySQL Data Base.
		// If connection not successful return null.
		return null;
	} // End of the getConnection method.
	
	
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
        // Local Variables:
		String product_id = "";
		String pagination_no = "";
		String product_name = "";
		String pagination_url = "";
		String comment_author = "";
		String author_account = "";
		String comment_title = "";
		String comment_date = "";
		String star_rating = "";
		String review_text = "";
		
	    String fileName = "C:\\Users\\Stefan\\workspace\\Crawler\\ActionPie-VJJB-V1S-High-Resolution-Heavy-Bass-In-ear-Headphones-with-Mic-for-SmartPhones.txt"; // Holds the name of the file to be read.
	    String line = ""; // Used to read the file.
	    String [] contents = null; // Will contain individual separated file contents.
	    String delimiter = "\\|"; // Contains the delimiter to split the String read from file.
	    String query = null; // Will contain the insertion SQL query.
	    BufferedReader bf = null; // BufferedReader instance to read the file.
	    PreparedStatement preparedStatement = null;
        
		// Get connection to the CeraIT MySQL Database:
		getConnection();
		// Insert the values into the table from the result.txt files:
		bf = new BufferedReader(new FileReader(fileName));
		while((line = bf.readLine()) != null)
		{
			//System.out.println(line);
			
			// Split the line:
            contents = line.trim().split(delimiter);
            // Get file contents into different variables:
            try
            {
            	product_id = "3";
            	pagination_no = contents[0];
            	product_name = contents[1];
            	pagination_url = contents[2];
            	comment_author = contents[3];
            	author_account = contents[4];
            	comment_title = contents[5];
            	comment_date = contents[6];
            	star_rating = contents[7];
            	review_text = contents[8];
            	

            	System.out.println(comment_author);
            	// Insert the values into the product table:
            	query = "insert into crawler_product_reviews (product_id, pagination_no, product_name, pagination_url, "
            			+ "comment_author, author_account, comment_title, comment_date, star_rating, review_text)"
            			+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            	
                // Prepare the query:
                preparedStatement = connect.prepareStatement(query);
                // insert values:
                preparedStatement.setString (1, product_id);
                preparedStatement.setString (2, pagination_no);
                preparedStatement.setString (3, product_name);
                preparedStatement.setString (4, pagination_url);
                preparedStatement.setString (5, comment_author);
                preparedStatement.setString (6, author_account);
                preparedStatement.setString (7, comment_title);
                preparedStatement.setString (8, comment_date);
                preparedStatement.setString (9, star_rating);
                preparedStatement.setString (10, review_text);
                
                // execute the preparedStatement
                preparedStatement.execute();
  
            } 
            catch(ArrayIndexOutOfBoundsException ar1)
            {
            	System.out.println(ar1.getMessage());
            }
		} // End while read file.
		connect.close();
		
	} // End of the main method.

} // End of the ReviewMySQL class.
