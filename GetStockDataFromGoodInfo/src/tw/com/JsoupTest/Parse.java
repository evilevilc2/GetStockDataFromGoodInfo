package tw.com.JsoupTest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parse extends Thread{
	static SQLBridge db;
	static Connection conn = null;
	static Statement stmt = null;
	boolean running = false;
	static final String[] changeVauleArray = { "totalPrice", "stockAvgvolume",
		"stockAvgprice", "yPrice", "yAvgvolume", "yAvgprice", "yNetchange" };

	
	static final String[] strArray = { "stockId", "stockName",
		"stockInfo",
		"stockPBRInfo",
		"stockPrice",
		"netchangePrice"
		// "stockDateTime"
		, "downPerson", "yesStockPrices", "openingPric", "highPrice",
		"lowPrice", "totalVolume"

		, "totalPrice", "stockCount", "stockAvgvolume", "stockAvgprice",
		"PER", "PBR"

		, "yVolume", "yPrice", "yCount", "yAvgvolume", "yAvgprice",
		"yNetchange" };

	private final static Logger LOG = Logger.getLogger(ParseDatra.class);
	public Parse(){
		init();
		this.start();
	}
	
	public void init() {
		LOG.info("ENTER init()");
		running = true;
		// GetStockHtml getStock = new GetStockHtml();
	}
	
	// "3266"、"聯強電" 如 "3266|聯強電" ==> "3266"、"聯強電"
		public static String[] strSplitByBar(String ssb) {
			final String tempStr = ssb;
			return tempStr.split("\\|", -1);
		}
	
	// ("stockId", "3266")、("stockName","聯強電")
	public  void getStockIDAndName(String stockidstr, Map savemap) {
		int count = 0;
		for (String saveValue : strSplitByBar(changeString(stockidstr, "|"))) {
			savemap.put(strArray[count], saveValue);
			count++;
			LOG.info("\nhere" + strArray[count] + " = " + saveValue);
		}
	}
	
	// "&nbsp;" to "|" 如 3266&nbsp;聯強電 ==>3266|聯強電
		public static String changeString(String changefrom, String changetostr) {
			final byte bytes[] = { (byte) 0xC2, (byte) 0xA0 };
			try {
				LOG.info("\n================changefromstr = " + changefrom
						+ " , changetostr=" + changetostr);
				return changefrom.replaceAll(new String(bytes, "utf-8"),
						changetostr);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "0";
		}
	
		private static void setValue(String[] ss, ArrayList<String> arrlist) {
			for (int i = 0; i < ss.length; i++) {
				arrlist.add(ss[i]);
				// LOG.info("給值: " + ss[i]);
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
		
	public void run(){
		File stockPath = new File("D:\\Stock");
		if (stockPath.isDirectory()) {
			File[] fileList = stockPath.listFiles();
			Document docTest;
			ArrayList<String> chineList = new ArrayList<String>();
			ArrayList<String> valueList = new ArrayList<String>();
			Map<String, String> stockStrorMap = new HashMap();
			for (File f : fileList) {
				LOG.info("\n" + f);
				try {
					docTest = Jsoup.parse(f, "UTF-8");

				// 取評語
				Elements ele = docTest.select("span");// .attr("bgcolor");
				// Elements eleStr = doc
				// .select("span[style=cursor:default;font-size:9pt;color:red]");
				for (Element tds : ele) {
					LOG.info(" 評語1, " + tds.text());
				}
				// 取股票代號、股票中文名稱
				getStockIDAndName(
						docTest.select(
								"span[style=color:blue;font-size:14pt;font-weight:bold;]")
								.text(), stockStrorMap);

				// 股價評語
				stockStrorMap
						.put(strArray[2],
								docTest.select(
										"span[style=cursor:default;font-size:9pt;color:red]")
										.attr("title"));

				// PBR評語
				LOG.info("PBR評語：　"
						+ docTest
								.select("span[style=cursor:default;font-size:9pt;color:green]")
								.attr("title"));
				stockStrorMap
						.put(strArray[3],
								docTest.select(
										"span[style=cursor:default;font-size:9pt;color:green]")
										.attr("title"));

				// 月日
				// </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				// &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				// <span style="font-size:9pt;color:gray;">資料日期:
				// 05/26</span></td>
				// PBR評語
				LOG.info("dateTime：　"
						+ docTest.select(
								"span[style=font-size:9pt;color:gray;]").text());
				StringBuilder dateString = new StringBuilder()
						.append(docTest.select(
								"span[style=font-size:9pt;color:gray;]").text());
				String[] sssArray = dateString.toString().split(" ");
				for (String ss : sssArray) {
					LOG.info("ss = " + ss);
				}
				dateString.setLength(0);
				dateString.append("2017/").append(sssArray[1]);
				stockStrorMap.put("getStockDateTime", dateString.toString());
				
				// 取成股票的ID、中文名稱
				// tr元素 屬性align="center" bgcolor="white" height="24px"
				// 股價成交價的tr標千 屬性
				// 取值
				Elements getValue = docTest
						.select("tr[align=center]tr[bgcolor=white]tr[height=26px]");// ,
																					// tr[bgcolor=white]");
				// 取中文名
				Elements getPriceChie = docTest
						.select("tr[align=center]tr[valign=middle]tr[bgcolor=#e7f3ff]tr[height=26px]");

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
					LOG.info("\nMAP key=" + keyname + " : "
							+ stockStrorMap.get(keyname));
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
							stockStrorMap.get(changeVauleArray[i]),
							stockStrorMap);
				}

//				try {
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
					Locale.ENGLISH);
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
					map.get("yAvgprice"), map.get("yNetchange"), "20170917test", "Y",
					sdf.format(new Date()), "no update",map.get("getStockDateTime"));
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
			// .get("昨漲跌價 (幅)"), "test", "Y",
			// new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
			// .format(new Date()), "no update");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void saveStockDataToDBTest(Map map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
				Locale.ENGLISH);
		try {
			db.insertStockData(map.get("stockId"), map.get("stockName"),
					map.get("stockInfo"), map.get("stockPBRInfo"),
					map.get("stockPrice"), map.get("netchangePrice"),
					map.get("downPerson"), map.get("yesStockPrices"),
					map.get("openingPric"), map.get("highPrice"),
					map.get("lowPrice"), map.get("totalVolume"),
					map.get("totalPrice"), map.get("stockCount"),
					map.get("stockAvgvolume"), map.get("stockAvgprice"),
					map.get("PER"), map.get("PBR"), map.get("yVolume"),
					map.get("yPrice"), map.get("yCount"),
					map.get("yAvgvolume"), map.get("yAvgprice"),
					map.get("yNetchange"), "hello", "Y",
					sdf.format(new Date()), "no update",
					map.get("getStockDateTime"));
			LOG.info("\n\n\n  |||||||||||||Seccuss|||||||||||||||");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNotEnp(String checkStr) {
		boolean check = false;
		if (!"".equals(checkStr)) {
			check = true;
		}
		check = false;
		return check;
	}
}
