import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySQLAccess 
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
		} // End of the try-catch set up connection to the CeraIT MySQL Data Base.
		// If connection not successful return null.
		return null;
	} // End of the getConnection method.
	

	    		  
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		
		// Local Variables:
	    String page_no = "";
	    String subcategory_id = "";
	    String url = "";
	    String name = "";
	    String sell_price = "";
	    String list_price = "";
	    String image = "";
	    String product_dimensions = "";
	    String item_weight = "";
	    String shipping_weight = "";
	    String ASIN = "";
	    String item_model_number = "";
	    String star_rating = "";
	    String short_description = "";
	    String detailed_description = "";
	    
	    String fileName = "C:\\Users\\Stefan\\workspace\\Crawler\\results8.txt"; // Holds the name of the file to be read.
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
            	
                page_no = contents[0];
                url = contents[1];
                name = contents[2];
                sell_price = contents[3];
                list_price = contents[4];
                image = contents[5];
                product_dimensions = contents[6];
                item_weight = contents[7];
                shipping_weight = contents[8];
                ASIN = contents[9];
                item_model_number = contents[10];
                star_rating = contents[11];
                short_description = contents[12];  
                detailed_description = contents[13];
                
            }
            catch(ArrayIndexOutOfBoundsException ar1)
            {
            	System.out.println(ar1.getMessage());
            }

            /*
            // Print values for checking:
            System.out.println(url + " " + name + " " + sell_price + " " + list_price + " " + image
            		+ " " + product_dimensions + " " + item_weight + " " + shipping_weight + " " +
            		ASIN + " " + item_model_number + " " + star_rating + " " + short_description);
            */
            System.out.println(product_dimensions);
            // Insert the values into the product table:
            query = "insert into product (url, name, sellPrice, listPrice, imgSrs, product_dimensions, "
            		+ "item_weight, shipping_weight, ASIN, item_model_number, customer_reviews, "
            		+ "short_description, page_no, detailed_description)"
            		+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Prepare the query:
            preparedStatement = connect.prepareStatement(query);
            // insert values:
            preparedStatement.setString (1, url);
            preparedStatement.setString (2, name);
            preparedStatement.setString (3, sell_price);
            preparedStatement.setString (4, list_price);
            preparedStatement.setString (5, image);
            preparedStatement.setString (6, product_dimensions);
            preparedStatement.setString (7, item_weight);
            preparedStatement.setString (8, shipping_weight);
            preparedStatement.setString (9, ASIN);
            preparedStatement.setString (10, item_model_number);
            preparedStatement.setString (11, star_rating);
            preparedStatement.setString (12, short_description);
            preparedStatement.setString (13, page_no);
            preparedStatement.setString (14, detailed_description);
            
            // execute the preparedStatement
            preparedStatement.execute();
            
            /*
            for(String c : contents)
            {
            	System.out.println(c);
            }
            */
		} // End while read the file.
		
		connect.close();
        
	} // End of main.

} // End of class MySQLAccess.
