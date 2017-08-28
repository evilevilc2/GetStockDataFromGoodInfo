

//funtion 1 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 




import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;



//funtion 2

import java.util.Locale;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;



//抓下來
public class Test {
	public static void main(String[] args ){
			// 設定要抓取的網頁
		StringBuilder urlStr = new StringBuilder(128);
//		urlStr.append("http://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022");
		urlStr.append("http://www.cwb.gov.tw/V7/forecast/week/week.htm");	//test
		//威強電代號3002 ，中鋼是2002 change
//		urlStr.delete(62, 66);
//		urlStr.append("2002");
//		System.out.println(urlStr.toString());
//			String url = "http://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022";
	       try {
	    	   	//將url所顯示的原始碼取下來後，轉為物件Document doc
	            Document doc = Jsoup.connect( urlStr.toString() ).get();
	            
//	            System.out.println("1 " + doc.title());
//	            System.out.println("2 " + doc.toString());
	            
	            //取下來的網頁資料存放入這個檔案
//	            Writer("D:/input_20170531.html", doc.toString());
	            // new Date() May 31 23:18:26 CST 2017
	            writerToFile("D:/test_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH).format(new Date())+ ".html", doc.toString());//test
	            
	            Elements h1s = doc.select("span.titletext");
	 
	            Element thisOne = null;
	            for(Iterator it = h1s.iterator(); it.hasNext();)
	            {
	                thisOne = (Element)it.next();
	                System.out.println(thisOne.html());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	public static void writerToFile(String pathName, String data){
        java.io.FileWriter writer;
		try {
			writer = new java.io.FileWriter(pathName);
			 // 寫入檔案
	        writer.write(data);
	        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public static void changeNumber(String newnumber , StringBuilder urlstr){
		urlstr.delete(62, 66);
		urlstr.append(newnumber);
	}
}


