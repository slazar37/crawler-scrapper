import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLGenerator 
{

	public static void main(String[] args) throws FileNotFoundException 
	{
		
		// Local Variables:
		
		String sitemap; // contains path to the sitemap.html file.
		String content = ""; // obj. to read the contents of sitemap.html
		String urlcontent = ""; // Will contain the extracted url from the content.
		String absURL = ""; // Holds the URL of each page in the pagination.
        String fileName = "urls.txt"; // Name of the file that will contain the pagination URLs
		
		BufferedReader r1; // Will be used to read the sitemap.html file.
		ArrayList<String> arr = new ArrayList<String>(); // will contain the list of all extracted urls.
		ArrayList<String> pagination = new ArrayList<String>(); // Will hold all the individual URLs for the pagination.
		Element page = null; // Used to hold the URL of the next page in pagination.	
		org.jsoup.nodes.Document doc = null; // Holds the connection to the first page in Amazon website.
		org.jsoup.nodes.Document doc_new = null; // Document obj. that holds the connection of the individual pages while traversing the pagination.
		File f = null; // File that will contain all the pagination URLs.
		PrintWriter writer = null; // PrintWriter object to write pagination URLs to the File f.
		
		//=========================================================================================================================================== 
		// 1. Read the contents of sitemap.html, extract all URLs, and place them in ArrayList arr:	
		
		sitemap = "C:\\Users\\Stefan\\workspace\\Crawler\\src\\sitemap.html"; // Assign generated sitemap file to a local variable.
		try
		{
			r1 = new BufferedReader(new FileReader(sitemap)); // Create a BufferedReader object to read a file.
			try
			{
				// Reas the file line by line, extract the URLs, and place them in the ArrayList arr:
				while((content = r1.readLine()) != null)
				{
					//outputStream1.println(content);
					
					// Extract URL links from the content:
					if(content.contains("<a href=")) 
					{
						urlcontent = content.replaceAll("\" title=\"Amazon.com:.*", "");
						urlcontent = urlcontent.replaceAll("<tr><td class=\"lpage\"><a href=\"", "");
						if(!(urlcontent.contains("<h3><a href=")))
						{
							arr.add(urlcontent); // Add URL to ArrayList arr;
						}
						//System.out.println(urlcontent);
						//System.out.println(content);
					} // End if.
				} // End while(read file sitemap.html).
			}
			catch(FileNotFoundException fn2)
			{
				System.out.println(fn2.getMessage());
			} 
			catch(IOException i1)
			{
				System.out.println(i1.getMessage());
			}// End of the try-catch read file sitemap.html, extract URLs, and place in ArrayList arr.	
		}
		catch(FileNotFoundException fn1)
		{
			System.out.println(fn1.getMessage());
		} // End try-catch read in the sitemap.html file.
		
		//=======================================================================================================================================================
		// 2. Connect to the root URL, get all the URLs from its pagination, and store them in the ArrayList structure called pagination:
        
		try 
		{
			// Establish connection to the root URL:
			doc = Jsoup.connect(arr.get(0)).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.lego.com/en-us/").get();
			// Fetch the URL of the next page in pagination:
			page = doc.getElementById("pagnNextLink"); 
			// Add the element to the pagination structure.
			pagination.add(arr.get(0)); 
			
			// Traverse all links of the pagination, extract their URLs, and store them in the ArrayList pagination object:
			int counter = 1;
			while(true)
			{
				try
				{
					 // Get the URL of the page.
					absURL = page.absUrl("href");
					System.out.println(counter + ". " + absURL); 
					// Add the URL to the structure:
					pagination.add(absURL); 
					// Connect to the URL of the next page in the pagination:
					doc_new = Jsoup.connect(page.absUrl("href")).userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:21.0.0) Gecko/20121011 Firefox/21.0.0").timeout(20000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.youtube.com").get();
					// Put the URL element of the next page in the pagination in the variable page:
					page = doc_new.getElementById("pagnNextLink");
					counter++;
					
					// Check if reached the pagination end (only the last page contains the class lastPageRightArrow):
					Elements marker = doc_new.getElementsByClass("lastPageRightArrow");	
					if(!(marker.isEmpty()))
					{
						// Add the last pagination to the ArrayList:
						pagination.add(absURL); 
						// End the while loop:
						break;
					} // End if(exit).
				}
				catch(NullPointerException np1)
				{
					System.out.println(np1.getMessage());
				} // End try-catch if the file exists.
			} // End while(traverse the pagination).
		} 
		catch (IOException i2) 
		{
			// TODO Auto-generated catch block
			System.out.println(i2.getMessage());
		} // Establish connection to the URL.
		
		//=========================================================================================================================================================
		// 3. Traverse the ArrayList pagination, get individual URLs, and print them on the file urls.txt
		
		// Create the file object:
		f = new File(fileName); 
        try 
        {
        	// Create the writer object:
			writer = new PrintWriter(f, "UTF-8");
		} 
        catch (UnsupportedEncodingException ue1)
        {
			// TODO Auto-generated catch block
            System.out.println(ue1.getMessage());
        } // End of the try-catch instantiate PrintWriter writer.
        
        // Traverse the ArrayList pagination:
		for(String s : pagination)
        {
        	// Write URL to the file urls.txt:
			writer.println(s);
        } // End for(traverse ArrayList pagination).
		writer.close(); // Close the writer object.
		
		
		System.out.println("=======================================================================");	
	   // Test if the urls.txt file generated:
		String line = "";
		try
		{
			BufferedReader b2 = new BufferedReader(new FileReader(f));
			while((line = b2.readLine()) != null)
			{
				System.out.println(line);
			} // End while(read file.)
		}
		catch(FileNotFoundException fe2)
		{
			System.out.println(fe2.getMessage());
		} 
		catch (IOException io2) 
		{
			// TODO Auto-generated catch block
			System.out.println(io2.getMessage());
		}
		
		
	} // End of the main method.

} // End of the class URLGenerator.
