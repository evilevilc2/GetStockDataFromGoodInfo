package tw.com.JsoupTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ParseDatra {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/STUDENTS";

	// static final String[] strArray = { "stockId", "stockName", "stockInfo",
	// "stockPBRInfo" };
	static final String[] strArray = { "stockId", "stockName", "stockInfo",
			"stockPBRInfo","stockPrice", "netchangePrice"
//"stockDateTime"
			, "downPerson", "yesStockPrices", "openingPric", "highPrice",
			"lowPrice", "totalVolume"

			, "totalPrice", "stockCount", "stockAvgvolume", "stockAvgprice",
			"PER", "PBR"

			, "yVolume", "yPrice", "yCount", "yAvgvolume", "yAvgprice",
			"yNetchange" };

	static final String[] changeVauleArray = { "totalPrice", "stockAvgvolume",
			"stockAvgprice", "yPrice", "yAvgvolume", "yAvgprice", "yNetchange" };

	// LOG.info(" 股票ID= " + stockStr.get(0));
	// LOG.info(" 股票名稱= " + stockStr.get(1));
	// LOG.info(" 股票資訊= " + stockStr.get(2));
	// LOG.info(" PBR資訊= " + stockStr.get(3));

	// Database credentials
	static SQLBridge db;
	static Connection conn = null;
	static Statement stmt = null;

	private final static Logger LOG = Logger.getLogger(ParseDatra.class);

	public ParseDatra(){
		init();
//		public TestGetStock(){
//			init();
//		}
//		public void init(){
//			GetStockHtml getStock = new GetStockHtml();
//		}
//		public static void main(String[] args) {
//			// TODO Auto-generated method stub
//			new TestGetStock();
//			}
	}
	public void init(){
//		GetStockHtml getStock = new GetStockHtml();
	}
	public static void main(String[] args) throws IOException {
		new TestGetStock();
		// writerToFile("D:/Stock/test_" + stockIDArray[i] + "_"
		// + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
		// Locale.ENGLISH).format(new Date()) + ".html",
File stockPath = new File("D:\\Stock");
if(stockPath.isDirectory()){
	File[] fileList = stockPath.listFiles();
	Document docTest;
	ArrayList<String> chineList = new ArrayList<String>();
	ArrayList<String> valueList = new ArrayList<String>();
	Map<String, String> stockStrorMap = new HashMap();
	for(File f : fileList){
		LOG.info("\n"+ f);
		docTest = Jsoup.parse(f, "UTF-8");
		// 取評語
				Elements ele = docTest.select("span");// .attr("bgcolor");
//				Elements eleStr = doc
//						.select("span[style=cursor:default;font-size:9pt;color:red]");
				for (Element tds : ele) {
					LOG.info(" 評語1, " + tds.text());
				}
				// 取股票代號、股票中文名稱
				getStockIDAndName(
						docTest.select(
								"span[style=color:blue;font-size:14pt;font-weight:bold;]")
								.text(), stockStrorMap);

				// 股價評語
				stockStrorMap.put(
						strArray[2],
						docTest.select("span[style=cursor:default;font-size:9pt;color:red]")
								.attr("title"));

				// PBR評語
				LOG.info("PBR評語：　"
						+ docTest.select(
								"span[style=cursor:default;font-size:9pt;color:green]")
								.attr("title"));
				stockStrorMap.put(
						strArray[3],
						docTest.select(
								"span[style=cursor:default;font-size:9pt;color:green]")
								.attr("title"));

//				月日
//				</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
//				<span style="font-size:9pt;color:gray;">資料日期: 05/26</span></td> 
				// PBR評語
				LOG.info("dateTime：　"
						+ docTest.select(
								"span[style=font-size:9pt;color:gray;]")
								.text());
				StringBuilder dateString = new StringBuilder().append( docTest.select(
						"span[style=font-size:9pt;color:gray;]")
						.text() );
				String[] sssArray = dateString.toString().split(" ");
				for(String ss : sssArray){
					LOG.info("ss = " + ss);
				}
				dateString.setLength(0);
				dateString.append("2017/").append(sssArray[1]);
				stockStrorMap.put("getStockDateTime" ,dateString.toString());
//				SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
//				LOG.info("test date　" + date.format(date));
//				stockStrorMap.put(
//						strArray[3],
//						docTest.select(
//								"span[style=cursor:default;font-size:9pt;color:green]")
//								.attr("title"));
				
				
				
				// 取成股票的ID、中文名稱
				// tr元素 屬性align="center" bgcolor="white" height="24px" 股價成交價的tr標千 屬性
				// 取值
				Elements getValue = docTest
						.select("tr[align=center]tr[bgcolor=white]tr[height=24px]");// ,
																					// tr[bgcolor=white]");
				// 取中文名
				Elements getPriceChie = docTest
						.select("tr[align=center]tr[valign=middle]tr[bgcolor=#e7f3ff]tr[height=24px]");

				// 取成交價的值
				for (Element tds : getValue) {
					LOG.info("原始數值  =  " + tds.text());
					String[] valueStrArr = tds.text().split(" ");
					setValue(valueStrArr, valueList);
				}
				// 取成交價的中文說明
				for (Element tds : getPriceChie) {
					LOG.info("\n原始中文, " + tds.text());
					String chineStr = tds.text();
					String[] chineStrArr = chineStr.split(" ");
					setValue(chineStrArr, chineList);
				}

				for (Object keyname : stockStrorMap.keySet()) {
					LOG.info("\nMAP key=" + keyname + " : " + stockStrorMap.get(keyname));
				}

				// 印出來
				LOG.info("chineList.size() = " + chineList.size() 
						+ "\nstrArray.length = " + strArray.length);
				for (int e = 0; e < chineList.size(); e++) {
					// stockMap.put(myChineList.get(e), myValueList.get(e));
					LOG.info("\n" + chineList.get(e) + " = " + valueList.get(e));
					// 存入map
					// stockMap.put(myChineList.get(e), myValueList.get(e));
					stockStrorMap.put(strArray[e + 4], valueList.get(e));
				}
				
				chineList.clear();
				valueList.clear();
				
				// 更換數值裡的"&nbsp;"
				for (int i = 0; i < changeVauleArray.length; i++) {
					updateStockValue(changeVauleArray[i],
							stockStrorMap.get(changeVauleArray[i]), stockStrorMap);
				}

				try {
					db = new SQLBridge();
					db.openMSSQLDB();

					saveStockDataToDBTest(stockStrorMap);
					stockStrorMap.clear();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}
//		// File input = new File("./temp/test_20170424.txt");
//		File inputHTML = new File("./temp/input.html");//正確
//		// 儲存的ArrayList
//		ArrayList<String> myChineList = new ArrayList<String>();
//		ArrayList<String> myValueList = new ArrayList<String>();
//		ArrayList<String> stockStr = new ArrayList<String>();
//		Map<String, String> stockMap = new HashMap();
//
//		Document doc = Jsoup
//				.parse(inputHTML, "UTF-8",
//						"http://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022");
//		// Elements links = doc.select("a[href]");
//		// Elements pngs = doc.select("img[src$=.png]");
//		// Elements masthead = doc.select("div.masthead");
//
//		// 取評語
//		Elements ele = doc.select("span");// .attr("bgcolor");
////		Elements eleStr = doc
////				.select("span[style=cursor:default;font-size:9pt;color:red]");
//		for (Element tds : ele) {
//			LOG.info(" 評語1, " + tds.text());
//		}
//		// test
//		// 取股票代號、股票中文名稱
//		// tr bgcolor="#cde2e5" valign="bottom" height="30px" align="left"
//		// bgcolor=\"#cde2e5\" valign=\"bottom\" height=\"30px\"
//		// align=\"left\"
//
//		// doc.select(
//		// // <span style="color:blue;font-size:14pt;font-weight:bold;">
//		// "span[style=color:blue;font-size:14pt;font-weight:bold;]")
//		// .text();
//		getStockIDAndName(
//				doc.select(
//						"span[style=color:blue;font-size:14pt;font-weight:bold;]")
//						.text(), stockMap);
//
//		// 股價評語
//		// LOG.info("股價評語：　"
//		// + doc.select(
//		// "span[style=cursor:default;font-size:9pt;color:red]")
//		// .attr("title"));
//		// stockStr.add(doc.select(
//		// "span[style=cursor:default;font-size:9pt;color:red]").attr(
//		// "title"));
//		stockMap.put(
//				strArray[2],
//				doc.select("span[style=cursor:default;font-size:9pt;color:red]")
//						.attr("title"));
//
//		// PBR評語
//		LOG.info("PBR評語：　"
//				+ doc.select(
//						"span[style=cursor:default;font-size:9pt;color:green]")
//						.attr("title"));
//		// stockStr.add(doc.select(
//		// "span[style=cursor:default;font-size:9pt;color:green]").attr(
//		// "title"));
//		stockMap.put(
//				strArray[3],
//				doc.select(
//						"span[style=cursor:default;font-size:9pt;color:green]")
//						.attr("title"));
//
//		// 取成股票的ID、中文名稱
//		// static final String[] strArray = { "stockId", "stockName",
//		// "stockInfo",
//		// "stockPBRInfo" };
//
//		// LOG.info(" 股票ID= " + stockMap.get("stockId"));
//		// LOG.info(" 股票名稱= " + stockStr.get(1));
//		// LOG.info(" 股票資訊= " + stockStr.get(2));
//		// LOG.info(" PBR資訊= " + stockStr.get(3));
//
//		// tr元素 屬性align="center" bgcolor="white" height="24px" 股價成交價的tr標千 屬性
//		// 取值
//		Elements getValue = doc
//				.select("tr[align=center]tr[bgcolor=white]tr[height=24px]");// ,
//																			// tr[bgcolor=white]");
//		// 取中文名
//		Elements getPriceChie = doc
//				.select("tr[align=center]tr[valign=middle]tr[bgcolor=#e7f3ff]tr[height=24px]");
//
//		// 取成交價的值
//		for (Element tds : getValue) {
//			// if(tds.text().equals(anObject)())
//			LOG.info("原始數值  =  " + tds.text());
//			String valueStr = tds.text();
//			String[] valueStrArr = valueStr.split(" ");
//			setValue(valueStrArr, myValueList);
//		}
//		// 取成交價的中文說明
//		for (Element tds : getPriceChie) {
//			LOG.info("\n原始中文, " + tds.text());
//			String chineStr = tds.text();
//			String[] chineStrArr = chineStr.split(" ", -1);
//			setValue(chineStrArr, myChineList);
//		}
//
//		// 印出來
//		for (int e = 0; e < myChineList.size(); e++) {
//			// stockMap.put(myChineList.get(e), myValueList.get(e));
//			LOG.info("\n" + myChineList.get(e) + " = " + myValueList.get(e));
//			// 存入map
//			// stockMap.put(myChineList.get(e), myValueList.get(e));
//			stockMap.put(strArray[e + 4], myValueList.get(e));
//		}
//
//		for (Object keyname : stockMap.keySet()) {
//			LOG.info("\nMAP key=" + keyname + " : " + stockMap.get(keyname));
//		}
//
//		// 更換數值裡的"&nbsp;"
//		for (int i = 0; i < changeVauleArray.length; i++) {
//			updateStockValue(changeVauleArray[i],
//					stockMap.get(changeVauleArray[i]), stockMap);
//		}
//
//		try {
//			db = new SQLBridge();
//			db.openMSSQLDB();
//
//			saveStockDataToDB(stockMap);
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public long getlist(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
	public static void saveStockDataToDBTest(Map map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
		try {
			db.insertStockData(map.get("stockId"), map.get("stockName"), map
					.get("stockInfo"), map.get("stockPBRInfo"), map
					.get("stockPrice"), map.get("netchangePrice"), map
					.get("downPerson"), map.get("yesStockPrices"), map
					.get("openingPric"), map.get("highPrice"), map
					.get("lowPrice"), map.get("totalVolume"), map
					.get("totalPrice"), map.get("stockCount"), map
					.get("stockAvgvolume"), map.get("stockAvgprice"), map
					.get("PER"), map.get("PBR"), map.get("yVolume"), map
					.get("yPrice"), map.get("yCount"), map.get("yAvgvolume"),
					map.get("yAvgprice"), map.get("yNetchange"), "create new stock_datetime欄位", "Y",
					sdf.format(new Date() ), "no update" ,map.get("getStockDateTime"));
			LOG.info("\n\n\n  |||||||||||||Seccuss|||||||||||||||");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void saveStockDataToDB(Map map) {
		try {
			// stockPrice
			// netchangePrice
			//
			// downPerson
			// yesStockPrices
			// openingPric
			// highPrice
			// lowPrice
			// totalVolume
			//
			// totalPrice
			// stockCount
			// stockAvgvolume
			// stockAvgprice
			// PER
			// PBR
			//
			// yVolume
			// yPrice
			// yCount
			// yAvgvolume
			// yAvgprice
			// yNetchange
			db.insertStockData(map.get("stockId"), map.get("stockName"), map
					.get("stockInfo"), map.get("stockPBRInfo"), map
					.get("stockPrice"), map.get("netchangePrice"), map
					.get("downPerson"), map.get("yesStockPrices"), map
					.get("openingPric"), map.get("highPrice"), map
					.get("lowPrice"), map.get("totalVolume"), map
					.get("totalPrice"), map.get("stockCount"), map
					.get("stockAvgvolume"), map.get("stockAvgprice"), map
					.get("PER"), map.get("PBR"), map.get("yVolume"), map
					.get("yPrice"), map.get("yCount"), map.get("yAvgvolume"),
					map.get("yAvgprice"), map.get("yNetchange"), "test", "Y",
					"time", "no update",map.get("getStockDateTime"));
//					正確new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
//						正確	.format(new Date()), "no update");

			// db.insertStockData(map.get("stockId"), map.get("stockName"), map
			// .get("stockInfo"), map.get("stockPBRInfo")
			// , map.get("成交價"),
			// map.get("漲跌價"),
			// map.get("漲跌幅"),
			// map.get("昨收"),
			// map.get("開盤價"), map.get("最高價"), map.get("最低價"), map
			// .get("成交張數"), map.get("成交金額"), map.get("成交筆數"), map
			// .get("成交均張"), map.get("成交均價"), map.get("PER"), map
			// .get("PBR"), map.get("昨日張數"), map.get("昨日金額"), map
			// .get("昨日筆數"), map.get("昨日均張"), map.get("昨日均價"), map
			// .get("昨漲跌價 (幅)"), "test", "Y",
			// new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
			// .format(new Date()), "no update");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void setValue(String[] ss, ArrayList<String> arrlist) {
		for (int i = 0; i < ss.length; i++) {
			arrlist.add(ss[i]);
			// LOG.info("給值: " + ss[i]);
		}
	}

	private static void print(String msg, Object... args) {
		LOG.info(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

	// "&nbsp;" to "|" 如 3266&nbsp;聯強電 ==>3266|聯強電
	public static String changeString(String changefrom, String changetostr) {
		final byte bytes[] = { (byte) 0xC2, (byte) 0xA0 };
		try {
			LOG.info("\n================changefromstr = " + changefrom + " , changetostr=" + changetostr);
			return changefrom.replaceAll(new String(bytes, "utf-8"),
					changetostr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "0";
	}

	// "3266"、"聯強電" 如 "3266|聯強電" ==> "3266"、"聯強電"
	public static String[] strSplitByBar(String ssb) {
		final String tempStr = ssb;
		return tempStr.split("\\|", -1);
	}

	// ("stockId", "3266")、("stockName","聯強電")
	public static void getStockIDAndName(String stockidstr, Map savemap) {
		int count = 0;
		for (String saveValue : strSplitByBar(changeString(stockidstr, "|"))) {
			savemap.put(strArray[count], saveValue);
			count++;
			LOG.info("\nhere" + strArray[count] + " = " + saveValue);
		}
	}

	public static String[] strSplitByDsu(String ssb) {
		final String tempStr = ssb;
		return tempStr.split("-", -1);
	}

	public static void updateStockValue(String keystr, String value, Map savemap) {
		int count = 0;
		StringBuilder ssb = new StringBuilder(30);
		for (String saveValue : strSplitByDsu(changeString(value, "-"))) {
			ssb.append(saveValue);

			count++;
			// LOG.info("\nhere" + strArray[count] + " = " + saveValue);
		}
		savemap.put(keystr, ssb.toString());
	}
}
// 問題產生
//
//  最近遇到一個這樣的問題，在生成的報文中，某個字段信息後面有一個空格，在代碼中修剪（）下，它仍然存在。到底什麼原因呢？
//
//  問題的根源
//
//  經過多番查證，是由於UTF-8中的特俗字符造成的。
//
//  問題的根源，在於UTF-8這種編碼裡面，存在一個特殊的字符，其編碼是“0xC2 0xA0”，轉換成字符的時候，表現為一個空格，跟一般的半角空格
//  為0x20）一樣，唯一的不同是它的寬度不會被壓縮，因此比較多的被用於網頁排版（如首行縮進之類）。而其他的編碼方式如GB2312，Unicode的之類並沒有這樣的字符，因此如果簡單地進行編碼轉換，生成地GB2312
// / Unocode字符串中，這個字符就會被替換成為問號（ASCII
//  ox3F）。
//
//  使用UTF-8進行HTMLDecode的時候，對於語句開頭的（NBSP），就會被自動轉換成為這個特殊的空格，可能是判斷為放在開頭的空格，一定是用來排版的在轉換為其他。編碼之前，這個特殊的空格受到的待遇與普通的半角空格是一致的，甚至也會被修剪（）去掉。
//
//  因此，碰到這個問題的原因有兩種：一種是在UTF-8編碼下進行了轉換，產生了這個字符;還有一種就是網頁中直接採用了這個字符進行排版。
//
//  問題解決之法
//
//  複製代碼
//  C＃代碼如下：
//
//
// byte[] space = new byte[]{0xc2,0xa0};
// 　string UTFSpace = Encoding.GetEncoding("UTF-8").GetString(space);
// 　HtmlStr = HtmlStr.Replace(UTFSpace,"&nbsp;");
//
// Java版：
// byte bytes[] = {(byte) 0xC2,(byte) 0xA0};
// String UTFSpace = new String(bytes,"utf-8");
// html = html.replaceAll(UTFSpace, "&nbsp;");
// 复制代码
// 注意：
// 需要强调的是，替换之前不能进行编码转换，一定要继续使用UTF-8编码。如果已经转换成其他编码，那么错误就已经不可逆转了。没有办法再区分这个错误的问号和正常的问号之间的差别了。

// // JDBC driver name and database URL
// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
// static final String DB_URL = "jdbc:mysql://localhost/STUDENTS";
//
// // Database credentials
// static final String USER = "username";
// static final String PASS = "password";
//
// public static void main(String[] args) {
// Connection conn = null;
// Statement stmt = null;
// try{
// //STEP 2: Register JDBC driver
// Class.forName("com.mysql.jdbc.Driver");
//
// //STEP 3: Open a connection
// LOG.info("Connecting to a selected database...");
// conn = DriverManager.getConnection(DB_URL, USER, PASS);
// LOG.info("Connected database successfully...");
//
// //STEP 4: Execute a query
// LOG.info("Creating table in given database...");
// stmt = conn.createStatement();
//
// String sql = "CREATE TABLE REGISTRATION " +
// "(id INTEGER not NULL, " +
// " first VARCHAR(255), " +
// " last VARCHAR(255), " +
// " age INTEGER, " +
// " PRIMARY KEY ( id ))";
//
// stmt.executeUpdate(sql);

// jsoup know how
// 其中baseUri表示檢索到的相對URL是相對於baseUriURL的
// 其中charsetName表示字符集</code></code>先介紹這個 [attr]，這個意思是含有attr的屬性，

// 舉個例像 [title]，這個結果有218個，第一個符合的是
//
// 1
// <link rel="alternate" type="....." href="....."
// title="PIXNET oEmbed Profile">
// 看出來為什麼了嗎? 這跟直接搜尋 title 是不一樣的結果，
//
// 直接搜尋的話:
//
// 1
// <meta property="og:title" content="【JAVA】使用JSOUP讓JAVA取得網頁上的...." />
// 有加[ ]表示找的是屬性，沒有加的是找純文字，
//
// 如果前面加上a[title] (a 表超連結)，出來的結果是 tagname 為<a> 且含有屬性title 的標籤，

// <span class=\"class1\" >36</span>
// <span class=\"class2\" >22</span>
// doc.select("span[class=class1]span[id=id1]");

// <img class="xx" title="xx" alt="xx" onerror="img_error(this);"
// src="http://ww2.sinaimg.cn/bmiddle/xxx.jpg">
// 目标是img标签中的src图片地址
//
// Document doc = Jsoup.parse(content);
// Elements elements = doc.select("img[src$=.jpg]");
// for (Element src : elements) {
// Log.d(TAG, src.attr("abs:src"));
// ｝

// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
// String url = args[0];
// print("Fetching %s...", url);
//
// Document doc = Jsoup.connect(url).get();
// Elements links = doc.select("a[href]");
// Elements media = doc.select("[src]");
// Elements imports = doc.select("link[href]");
//
// print("\nMedia: (%d)", media.size());
// for (Element src : media) {
// if (src.tagName().equals("img"))
// print(" * %s: <%s> %sx%s (%s)",
// src.tagName(), src.attr("abs:src"), src.attr("width"),
// src.attr("height"),
// trim(src.attr("alt"), 20));
// else
// print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
// }
//
// print("\nImports: (%d)", imports.size());
// for (Element link : imports) {
// print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"),
// link.attr("rel"));
// }
//
// print("\nLinks: (%d)", links.size());
// for (Element link : links) {
// print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(),
// 35));
// }

// add sql table
// 對應表
// 股票代號 stockID
// 股票名稱 stockName
// 股價評論 stockPriceStr
// PBR評論 PBRStrＳ
// 成交價 = 46.15 stockPrice
// 漲跌價 = +0.3 netchange_price
// 漲跌幅 = +0.65% downPerson
// 昨收 = 45.85 yStock
// 開盤價 = 45.85 opening_price
// 最高價 = 46.15 highPrice
// 最低價 = 45.5 lowPrice
// 成交張數 = 285 totalVolume
// 成交金額 = 1,309.3 萬 totalPrice
// 成交筆數 = 223 totalCount
// 成交均張 = 1.3 張/筆 avgVolume
// 成交均價 = 45.89 元 avgPrice
// PER = 11.07 PER
// PBR = 1.91 PBR
// 昨日張數 = 432 yVolume
// 昨日金額 = 1,978.0 萬 yPrice
// 昨日筆數 = 320 yVolume
// 昨日均張 = 1.4 張/筆 yAvgVolume
// 昨日均價 = 45.73 元 yAvgPrice
// 昨漲跌價 (幅) = +0.5 (+1.1%) y_netchange
//
// USE [StockDB]
// GO
// SET ANSI_NULLS ON
// GO
// SET QUOTED_IDENTIFIER ON
// GO
// SET ANSI_PADDING ON
// GO
// CREATE TABLE [dbo].[StockTbl](
// [id] [int] IDENTITY(1,1) NOT NULL,
// [stock_id] [varchar](50) NOT NULL,
// [stock_name] [varchar](50) NOT NULL,
// [stock_info] [varchar](200) NOT NULL,
// [stock_pbr_info] [varchar](200) NOT NULL,
//
// [stock_price] [varchar](50) NOT NULL,
// [netchange_price] [varchar](50) NOT NULL,
// [down_person] [varchar](50) NOT NULL,
// [yes_stock_price] [varchar](50) NOT NULL,
// [opening_price] [varchar](50) NOT NULL,
// [highprice] [varchar](50) NOT NULL,
// [lowprice] [varchar](50) NOT NULL,
//
// [total_volume] [varchar](20) NOT NULL,
// [total_price] [varchar](20) NOT NULL,
// [stock_count] [varchar](50) NOT NULL,
// [stock_avgvolume] [varchar](50) NOT NULL,
// [stock_avgprice] [varchar](50) NOT NULL,
//
// [PER] [varchar](50) NOT NULL,
// [PBR] [varchar](50) NOT NULL,
// [y_volume] [varchar](50) NOT NULL,
// [y_price] [varchar](50) NOT NULL,
// [y_count] [varchar](50) NOT NULL,
// [y_avgvolume] [varchar](50) NOT NULL,
// [y_avgprice] [varchar](50) NOT NULL,
// [y_netchange] [varchar](50) NOT NULL,
// [extend] [varchar](20) NOT NULL,
// [data_time] [varchar](20) NOT NULL,
// [status] [varchar](1) NOT NULL,
// [create_time] [nvarchar](24) NULL,
// [upload_time] [nvarchar](24) NULL
// ) ON [PRIMARY]

// insert
// USE [StockDB]
// GO
// SET ANSI_NULLS ON
// GO
// SET QUOTED_IDENTIFIER ON
// GO
// SET ANSI_PADDING ON
// GO
// CREATE TABLE [dbo].[StockTbl](
// [id] [int] IDENTITY(1,1) NOT NULL,
// [stock_id] [varchar](50) NOT NULL,
// [stock_name] [varchar](50) NOT NULL,
// [stock_info] [varchar](200) NOT NULL,
// [stock_pbr_info] [varchar](200) NOT NULL,
//
// [stock_price] [varchar](50) NOT NULL,
// [netchange_price] [varchar](50) NOT NULL,
// [down_person] [varchar](50) NOT NULL,
// [yes_stock_price] [varchar](50) NOT NULL,
// [opening_price] [varchar](50) NOT NULL,
// [highprice] [varchar](50) NOT NULL,
// [lowprice] [varchar](50) NOT NULL,
//
// [total_volume] [varchar](20) NOT NULL,
// [total_price] [varchar](20) NOT NULL,
// [stock_count] [varchar](50) NOT NULL,
// [stock_avgvolume] [varchar](50) NOT NULL,
// [stock_avgprice] [varchar](50) NOT NULL,
//
// [PER] [varchar](50) NOT NULL,
// [PBR] [varchar](50) NOT NULL,
// [y_volume] [varchar](50) NOT NULL,
// [y_price] [varchar](50) NOT NULL,
// [y_count] [varchar](50) NOT NULL,
// [y_avgvolume] [varchar](50) NOT NULL,
// [y_avgprice] [varchar](50) NOT NULL,
// [y_netchange] [varchar](50) NOT NULL,
// [extend] [varchar](50) NOT NULL,
// [status] [varchar](1) NOT NULL,
// [create_time] [nvarchar](50) NULL,
// [upload_time] [nvarchar](50) NULL
// ) ON [PRIMARY]

