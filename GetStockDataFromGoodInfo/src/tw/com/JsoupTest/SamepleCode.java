package tw.com.JsoupTest;



//處理
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SamepleCode {
 public static void main(String[] args) throws IOException {
	 int num = 0;
  BufferedReader br = new BufferedReader(new FileReader("D:/SampleTest.html"));
  StringBuilder html = new StringBuilder();
  try {

   String line = br.readLine();
   while (line != null) {
    html.append(line);
    html.append(System.lineSeparator());
    line = br.readLine();
   }
  } finally {
   br.close();
  }
  Document doc = Jsoup.parse(html.toString());
  Element body = doc.body();
  System.out.println("body= " + body);
  
  for (Element tds : doc.select("tr")) {
//   if(tds.text().equals(anObject)())
	  num++;
	  System.out.println(num +" , " +  tds.text());
   
  }
 }
}
