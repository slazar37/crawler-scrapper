import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class ReviewExtractor 
{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		// TODO Auto-generated method stub
		
		//Document docProd = Jsoup.connect("https://www.amazon.com/Audio-Technica-ATH-M30-Closed-Back-Headphones/dp/B00007E7C8/ref=lp_6466980011_1_37/178-9336997-6344811?s=musical-instruments&ie=UTF8&qid=1468017904&sr=1-37#customerReviews ").userAgent("Mozilla/5.0 (X11; NetBSD amd64; rv:16.0) Gecko/20121102 Firefox/16.0").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get(); // connect to the URL.
		//Document docProd = Jsoup.connect("https://www.amazon.com/Sennheiser-202-Professional-Headphones-Black/dp/B003LPTAYI/ref=lp_6466980011_1_1/190-5321938-1363651?s=musical-instruments&ie=UTF8&qid=1468352698&sr=1-1").userAgent("Mozilla/5.0 (X11; NetBSD amd64; rv:16.0) Gecko/20121102 Firefox/16.0").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get(); // connect to the URL.
		Document docProd = Jsoup.connect("https://www.amazon.com/Audio-Technica-ATH-M50x-Professional-Monitor-Headphones/dp/B00HVLUR86/ref=lp_6466980011_1_3?s=musical-instruments&ie=UTF8&qid=1468447184&sr=1-3").userAgent("Mozilla/5.0 (X11; U; Linux x86_64; it; rv:1.9.2.20) Gecko/20110805 Ubuntu/10.04 (lucid) Firefox/3.6.20").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get(); // connect to the URL.

		
		//System.out.println(docProd.title());
		
		// Get the elements containing the reviews from the url:
		Elements reviews = docProd.select("a[class=a-link-emphasis a-text-bold]");
		// Extract the url containing all the reviews:
		org.jsoup.nodes.Element reviews_el = reviews.first();
        //System.out.println(reviews_el.absUrl("href"));
        // Connect to the url:
        org.jsoup.nodes.Document docReview = Jsoup.connect(reviews_el.absUrl("href")).userAgent("Mozilla/5.0 (X11; U; OpenBSD i386; en-US; rv:1.9.2.20) Gecko/20110803 Firefox/3.6.20").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get();  
        Elements last_page = null;
        
        // Get the url of the element of the pagination:
        Elements page_elem = docReview.select("li[class=a-last] a");
        org.jsoup.nodes.Element new_page = page_elem.first();

        // Elements to be collected about the user reviews:
        String paginationURL = ""; // Holds the URL to the user comment pagination pages.
        Elements authors = null; // Holds the name of the comment's author.
        Elements titles = null; // Holds the title the customer created for his comment.
        Elements dates = null; // Holds the date when the comment was made.
        Elements star_ratings = null; // Holds the star ratings assigned to product by users.
        Elements review_texts = null; // Holds the actual customer review (comment).
        
        String product_name = ""; // Holds the name of the reviewed product.
        String author = "";
        String author_account = "";
        String title = "";
        String date = "";
        String star_rating = "";
        String review_text = "";
       
        int min = 1;
        int max = 15;
        Random rand = new Random();
        // Get the name of the product:
        product_name = docReview.title();
    	// Get the url of pagination from where the comment was taken:
		paginationURL = new_page.absUrl("href");
		// Get the user titles, and also the urls to their accounts of the comments:
		authors = docReview.select("a[class=a-size-base a-link-normal author]");
		// Get the titles of the comments:
		titles = docReview.select("a[class=a-size-base a-link-normal review-title a-color-base a-text-bold]");
		// Get the dates when the comments were made:
		dates = docReview.select("span[class=a-size-base a-color-secondary review-date]");
		// Get the star ratings of the reviews:
		star_ratings = docReview.select("span[class=a-icon-alt]");
		// Get the review user comments of the reviews:
		review_texts = docReview.select("span[class=a-size-base review-text]");
        
		
        String fileName = "Audio-Technica-ATH-M50x-Professional-Studio-Monitor-Headphones.txt"; // Name of the file to be generated with the reviews.
        File f = new File(fileName); // File to contain all the reviews.
        PrintWriter outputStream = new PrintWriter(f); // Make the prontWriter to write reviews to a file.
			
		int counter = 1; // Counts the number of processed pages in the pagination:
        // Traverse the pagination, until last page reached:
        while(true)
        {

    		//System.out.println(counter + ". " + new_page.absUrl("href"));
    		// Establish connection to the next page:
    		org.jsoup.nodes.Document docReview_new = Jsoup.connect(new_page.absUrl("href")).userAgent("Mozilla/5.0 (X11; U; Linux i686; de; rv:1.9.2.21) Gecko/20110830 Ubuntu/10.10 (maverick) Firefox/3.6.21").timeout(10000).followRedirects(true).maxBodySize(1024*1024*3).referrer("http://www.yahoo.com").get();  ;
    		
    		// Get the elements from the new pages in the pagination:
			
    	   	// Get the url of pagination from where the comment was taken:
    		paginationURL = new_page.absUrl("href");
    		// Get the user titles, and also the urls to their accounts of the comments:
    		authors = docReview_new.select("a[class=a-size-base a-link-normal author]");
    		// Get the titles of the comments:
    		titles = docReview_new.select("a[class=a-size-base a-link-normal review-title a-color-base a-text-bold]");
    		// Get the dates when the comments were made:
    		dates = docReview_new.select("span[class=a-size-base a-color-secondary review-date]");
    		// Get the star ratings of the reviews:
    		star_ratings = docReview_new.select("span[class=a-icon-alt]");
    		// Get the review user comments of the reviews:
    		review_texts = docReview_new.select("span[class=a-size-base review-text]");

    		// Get all the customer reviews, and print them:
    		// Print out the results of the rest of the pagination:
    		for(int i = 0; i < authors.size(); i++)
    		{
    			// Get the needed elements into the variables:
    			author = authors.get(i).text();
    			author_account = authors.get(i).absUrl("href");
    			title = titles.get(i).text();
    			date = dates.get(i).text();
    			star_rating = star_ratings.get(i).text();
    			if(star_rating.equals("|"))
    			{
    				star_rating = star_ratings.get(i - 1).text();
    			}
    			review_text = review_texts.get(i).text();
    			
    			// Print out the individual comments and their information:
    			System.out.println(counter + " | " + product_name + " | " + paginationURL + " | " + author + " | " + author_account + " | " + title + " | " + date + " | " + star_rating + " | " + review_text);
    		    // Print results to file:
    			outputStream.println(counter + " | " + product_name + " | " + paginationURL + " | " + author + " | " + author_account + " | " + title + " | " + date + " | " + star_rating + " | " + review_text);
    		} // End for get all the customer reviews.
    		
    		
    		// Traverse the rest of the pagination:
    		page_elem = docReview_new.select("li[class=a-last] a");
    		new_page = page_elem.first();

    		// Get the html element present only on the last page:
    		try
    		{
    			last_page = docReview_new.select("li[class=a-disabled a-last]");
    			//System.out.println(counter + ". " + last_page.first().absUrl("href"));
    		}
    		catch(NullPointerException npe1)
    		{

    		}
    		
    		// Stop when last page reached.
    		if(last_page.size() > 0)
    		{
    			break;
    		}
	    	Thread.sleep((rand.nextInt((max - min) + 1) + min) * 1000); 
    		counter++;
    		System.out.println("Finished the pagination page -------------");
        } // end while process pagination.
        // Close the PrintWriter.
       outputStream.close();
		
	}

}
