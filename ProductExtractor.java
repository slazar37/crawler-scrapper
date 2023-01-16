import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ProductExtractor
{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		
		// Local Variables:
		int fileCounter = 3; // Used to determine which file needs to be read.
		int max = 15; // Maximum time period for pausing execution.
		int min = 1; // Minimum time for pausing execution.
		
		String fileName = "results"; // Used to hold the path of the files to crawl.
		String fileResults = ""; // Used to hold the name of the file where the product information is stored.
		String line = ""; // Used to read the files.
		String name = ""; // Holds the product name.
		String sellPrice = ""; // Holds the product Sell Price.
		String listPrice = ""; // Holds the product List Price.
		String shortDescription = ""; // Holds the product short description.
		String imgSrc = ""; // Holds the URL for the product's image.
		String product_dimensions = ""; // Holds the product dimensions.
		String item_weight = ""; // Holds the item weight.
		String shipping_weight = ""; // Holds the shipping weight.
		String ASIN = ""; // Holds the ASIN.
		String item_model_number = ""; //Holds the Item Model Number.
		String star_rating = ""; // Holds the star rating.
		String detailed_descr = ""; // Used to detailed description.
	    String script = "";
        String decodedString = "";
		String decoded = "";
		String description = "";
        String splitScript [] = null;
		boolean isTable = false; // Used to check if the Product Information is stored in the HTML table.
		
		File f = null; // File that will contain all the product information.
		PrintWriter outputStream = null; // Used to write product information to the file.
		Random rand = null; // Used to generate random times for pausing execution.
		BufferedReader read = null; // Used to read the files.
		org.jsoup.nodes.Document docProd = null; // Document obj. that holds the connection of the individual products.
		Elements priceSell = null; // Holds the product Sell Price.
		Elements priceList = null; // Holds the product List Price.
		Elements descDiv = null; // Holds the product short description.
		Elements img = null; // Holds the image URLs.
		Elements tables = null; // Holds the tables with Product Information.
		Elements desc_holder = null; // Used to store the detailed decription.
		Elements scriptElements = null;
		//==========================================================================================================================
		// 1. Read the files generated in HTMLGenerator class, fetch to URLs of individual products, and extract their information:
		
		// Instantiate Random object to generate execution pause:
		rand = new Random();
		// Instantiate the file to hold the information:
		f = new File(fileName + fileCounter + ".txt");
		// Instantiate the PrintWriter object to hold the information.
		outputStream = new PrintWriter(f);
		// Get the path of the file to crawl:
		fileName = "C:\\Users\\Stefan\\workspace\\Crawler\\links3.txt";
		try 
		{
			// Instantiate the BufferedReader read to read files generated in HTMLGenerator:
			read = new BufferedReader(new FileReader(fileName));
			while((line = read.readLine()) != null)
			{
				//System.out.println(line);
				// Fetch HTTP connection:
		    	docProd = Jsoup.connect(line).userAgent("Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.6) Gecko/20100628 Ubuntu/10.04 (lucid) Firefox/3.6.6").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get(); // connect to the URL.
		    	// Pause for a random amount of time.
		    	//Thread.sleep((rand.nextInt((max - min) + 1) + min) * 1000); 
                
		    	
		    	// Fetch the information about the product:
		    	
		    	// Get the product name:
		    	try
		    	{
		    		name = docProd.title();
		    	}
				catch(NullPointerException ne1)
		    	{
					name = "No Name;";
		    	} // End of try-catch get product name.
		    	
		    	
		    	// Get the product Sell Price:
			    priceSell = docProd.getElementsByClass("a-color-price");
			    // check if element actually contains items; Purpose of priceSell.size() > 0 is because statements in Jsoup will return an empty Elements list;
			    if(priceSell!=null && priceSell.size() > 0)
			    {
				    try
				    {
				    	 sellPrice = priceSell.get(0).toString().replaceAll("\\<[^>]*>","");
				    }
				    catch(NullPointerException ne2)
				    {
				    	sellPrice = "No Sell Price";
				    } 
			    }
			    else
			    {
			    	sellPrice = "No Sell Price";
			    } // End else get Sell Price.
		    	
		    	
			    // Get the list price of the product:
			      priceList = docProd.getElementsByClass("a-text-strike"); // Get the List Price.
			      if(priceList!=null && priceList.size() > 0)
			      {
				      try
				      {
				    	  listPrice = priceList.get(0).toString().replaceAll("\\<[^>]*>","");
				    	  if(listPrice.equals(sellPrice))
				    	  {
				    	        Elements tr = docProd.select("span[class=a-size-medium a-color-price]");
				    	        if(!(tr.size() == 0))
				    	        {
				    	        	try
				    	        	{
				    	        		listPrice = tr.get(1).text();
				    	        	}
				    	        	catch(IndexOutOfBoundsException i1)
				    	        	{
				    	        		listPrice = "No List Price";
				    	        	}
				    	        }
				    	        else
				    	        {
				    	        	listPrice = "No List Price";
				    	        }
				    	  }
				      }
				      catch(NullPointerException ne3)
				      {
				    	  listPrice = "No List Price";
				      }
			      }
			      else
			      {
			    	  listPrice = "No List Price";
			      } // End else get List Price.
			      
			      
			      // Get the product short description:
			      descDiv = docProd.select("div[id=\"feature-bullets\"]");
			      if(descDiv!=null && descDiv.size() > 0)
			      {
				      try
				      {
				    	  shortDescription = descDiv.text();
				      }
				      catch(NullPointerException ne4)
				      {
				    	  shortDescription = "No Description";
				      }
			      }
			      else
			      {
			    	  listPrice = "No List Price";
			      } // End if-else get short description.
			      
			      
			    	// Get the product Image:
					// Get the Inage of the product:
					img = docProd.select("img[id=landingImage]");
					String src[] = null;
					String src2[] = null;
			        for(org.jsoup.nodes.Element e : img)
			        {
			        	//String imgSrc = img.attr("data-old-hires");
			            imgSrc = img.attr("data-a-dynamic-image");
			        	src = imgSrc.split(",");
			           	src2 = src[0].split("\\[");
			         	imgSrc = src2[0].replaceAll("\"", "");
			         	imgSrc = imgSrc.replaceAll("\\{", "");
			         	imgSrc = imgSrc.substring(0, imgSrc.length()-1);
			        	//System.out.println(imgSrc);
			        } // End for get image.
			    	
			    	// Get the Product Information:
			    	tables = docProd.select("table"); // Get all the tables from the file.
			    	int finder = 0;    
				    for(org.jsoup.nodes.Element table : tables)
				    {
				    	if(table.className().equals("a-keyvalue prodDetTable"))
				    	{
					    	//System.out.println(table.text());
					    	Elements tableElements = table.getElementsByClass("a-size-base");
					    	for(org.jsoup.nodes.Element element : tableElements)
					    	{
					    		try
					    		{
						    		if(element.text().equals("Product Dimensions"))
						    		{
						    			product_dimensions = tableElements.get(finder + 1).text();
						    		}
					    		}
						        catch(IndexOutOfBoundsException i1)
						        {
						        	product_dimensions = "No Info";
						        }
						        try
						        {
						    		if(element.text().equals("Item Weight"))
						    		{
						    			item_weight = tableElements.get(finder + 1).text();
						    		}
						        }
						        catch(IndexOutOfBoundsException i2)
						        {
						        	item_weight = "No Info";
						        }
						        try
						        {
						    	    if(element.text().equals("Shipping Weight"))
						    		{
						    			shipping_weight = tableElements.get(finder + 1).text();
						    		}
						        }
						        catch(IndexOutOfBoundsException i3)
						        {
						        	shipping_weight = "No Info";
						        }
						        try
						        {
						    		if(element.text().equals("ASIN"))
						    		{
						    			ASIN = tableElements.get(finder + 1).text();
						    		}
						        }
						        catch(IndexOutOfBoundsException i4)
						        {
						        	ASIN = "No Info";
						        }
						        try
						        {

						    		if(element.text().equals("Item model number"))
						    		{
						    			item_model_number = tableElements.get(finder + 1).text();
						    		}
						        }
						        catch(IndexOutOfBoundsException i5)
						        {
						        	item_model_number = "No Info";
						        }
						        try
						        {
						    		if(element.text().equals("Customer Reviews"))
						    		{
						    			star_rating = tableElements.get(finder + 1).text();
						    		}
						        }
						        catch(IndexOutOfBoundsException i6)
						        {
						        	star_rating = "No Info";
						        }
					    		finder++;
					    	}
				    	} // End if.
				    } // End for fetc Product Information.
			      
				    
				    
			    	// Get the Detailed Description of the product:
			    	try
			    	{
			    		// Get the script with the encoded detailed description:
			    		scriptElements = docProd.getElementsByTag("script");
			    		// decode the detailed description, and store it in the variable:
			            for (org.jsoup.nodes.Element element : scriptElements )
			            {    
			            	// Get all the scripts into nodes:
			                for (DataNode node : element.dataNodes()) 
			                {
			                	// Get only the script containing detailed description:
			                	if(node.getWholeData().contains("obj.initialize = function (onloadCallback, needWidthAdjust)"))
			                	{
			                		// Get the data from the script:
			                		script = node.getWholeData();
	                                // Get the pattern to iolate the encoded String containing detailed description:
			                		Pattern pattern = Pattern.compile("\"([^\"]*)\"");
			                		Matcher matcher = pattern.matcher(script);
			                	    // Traverse until encoded description found:
			                    	while(matcher.find())
			                    	{
			                    		// Get the pattern containing the Detailed Description:
			                        	   if(matcher.group(1).contains("%") && matcher.group(1).length() > 20)
			                        	   {
			                        		   // Extract individual element with the description:
			                        		    decoded = matcher.group(1);
			                        		    // Decode the element to get pure HTML.
			                        		    decoded = URLDecoder.decode(decoded, "UTF-8");
			                        		    // Parse the decoded element:
			                        		    Document doc = Jsoup.parse(decoded);
	                                            // Extract only the Detailed Description from encoded String HTML:
	 		                        		    Elements links = doc.select("div[class=productDescriptionWrapper]");
			                        		    for(org.jsoup.nodes.Element link : links)
			                        		    {
			                        		    	//System.out.println(link.text());
			                        		    	// Get the detailed description into the variable:
			                        		    	detailed_descr = link.text();
			                        		    } // End for get Detailed Description.
			                        	   } // End if isolate only the matched pattern that is the description.
			                         } // End while -- find encoded description
			                		
			                        
			                	} // End if - find script with detailed description.
			                } // End for get the individual needed script.
			          } // End for get the script into variable.
			    	}
			    	catch(NullPointerException descEx)
			    	{
			    		System.out.println(descEx.getMessage());
			    	}
			      
                  // Print out all results separated by pipe (|) on the files:       
				  if(name != "No Name" && sellPrice != "No Sell Price")
				  {
					  //System.out.println(detailed_descr);
					  outputStream.println(fileCounter + " | " + line + " | " + name + " | " + sellPrice + " | " + listPrice + " | " + imgSrc + " | " +product_dimensions + " | " + item_weight + " | " + shipping_weight + " | " + ASIN + " | " + item_model_number + " | " + star_rating + " | " +shortDescription + " | " + detailed_descr);
					  System.out.println(fileCounter + " | " + line + " | " + name + " | " + sellPrice + " | " + listPrice + " | " +  imgSrc + " | " +product_dimensions + " | " + item_weight + " | " + shipping_weight + " | " + ASIN + " | " + item_model_number + " | " + star_rating + " | " + shortDescription + " | " + detailed_descr);
				  } // End if(write results to file).
                  
			} // End while(read files).
			outputStream.close(); // Close the PrintWriter outputStream.
		} 
		catch (FileNotFoundException fn1) 
		{
			// TODO Auto-generated catch block 
            System.out.println(fn1.getMessage());
        } // End of try-catch read files.
		
		
		//String fileName = "C:\\Users\\Stefan\\workspace\\Crawler\\links";
	} // End of main.

} // End of class ProductExtractor.
