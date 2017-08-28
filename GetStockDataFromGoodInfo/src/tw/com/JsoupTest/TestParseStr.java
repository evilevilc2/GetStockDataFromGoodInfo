package tw.com.JsoupTest;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParseStr {
	public static void main(String[] args) {

		String html = "<tr bgcolor=\"#cde2e5\" valign=\"bottom\" height=\"30px\" align=\"left\">"
				+ " <td class=\"head_td\" style=\"padding-bottom:2px;\" colspan=\"7\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
				+ "<span style=\"color:blue;font-size:14pt;font-weight:bold;\">"
				+ "<a class=\"link_blue\" href=\"StockDetail.asp?STOCK_ID=3022\">"
				+ "3022&nbsp;威強電"
				+ "</a>"
				+ "</span>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<span "
				+ 	"style=\"cursor:default;font-size:9pt;color:red\" "
				+ 	"title=\"股價接近歷年平均高點, 已達平均高點的83%(歷年股價平均最高:55.75元, 最低:32.3元)\">"
				+ "	股價近高"
				+ "</span>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<span "
				+ 	"	style=\"cursor:default;font-size:9pt;color:green\" "
				+ 	"title=\"PBR低於歷年平均值, 高於平均低點28%(歷年PBR平均最高:2.58倍, 最低:1.49倍)\">"
				+ 	"PBR低"
				+ "</span>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "
				+ "<span style=\"font-size:9pt;color:gray;\">"
				+ "資料日期: 05/05"
				+ "</span>"
				+ "</td> " + "</tr>";
		Document doc = Jsoup.parse(html);
		Elements link = doc.select("span");// .first();
		Elements link2 = doc.select("span[style=cursor:default;font-size:9pt;color:green]");
		for (Element tds : link) {
			System.out.println(" 評語1, " + tds.text());
			System.out.println( " str = " +tds.attr("title"));
		}
		String str = " hell o ";

		String str2 = str.replace(" ", "|");

		System.out.println(str2 +" , ");
		
		
//		System.out.println( " str = " +doc.select("span").attr("title"));
		String text = doc.body().text(); // "An example link"
//		String linkHref = link.attr("title"); // "http://example.com/"
		
		String linkText = link.text(); // "example""
		System.out.println("title= " + link.attr("title"));
		String linkOuterH = link.outerHtml();
		// "<a href="http://example.com"><b>example</b></a>"
		String linkInnerH = link.html(); // "<b>example</b>"
	}
}
