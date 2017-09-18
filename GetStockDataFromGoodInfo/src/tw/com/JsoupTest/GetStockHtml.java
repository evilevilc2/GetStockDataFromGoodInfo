package tw.com.JsoupTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//funtion 2

import java.util.Locale;

import org.apache.log4j.Logger;

//抓下來
public class GetStockHtml extends Thread {
	boolean running = false;
	// String pathName = ""
	private final static Logger LOG = Logger.getLogger(GetStockHtml.class);

	public GetStockHtml() {
		init();
		this.start();
	}

	public void init() {
		LOG.info("ENTER init()");
		running = true;
	}

//	javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: 
//		PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: 
//		unable to find valid certification path to requested target 
//	public void validator() {
//		try {
//			trustAllHttpsCertificates();
//		
//		HostnameVerifier hv = new HostnameVerifier() {
////			public boolean verify(String urlHostName, SSLSession session) {
////				System.out.println("Warning: URL Host: " + urlHostName + " vs. "
////						+ session.getPeerHost());
////				return true;
////			}
//
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		};
//		HttpsURLConnection.setDefaultHostnameVerifier(hv);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//	}
//
//	
//	private static void trustAllHttpsCertificates() throws Exception {
//		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
//		javax.net.ssl.TrustManager tm = new miTM();
//		trustAllCerts[0] = tm;
//		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
//				.getInstance("SSL");
//		sc.init(null, trustAllCerts, null);
//		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
//				.getSocketFactory());
//	}
//
//	static class miTM implements javax.net.ssl.TrustManager,
//			javax.net.ssl.X509TrustManager {
//		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//			return null;
//		}
//
//		public boolean isServerTrusted(
//				java.security.cert.X509Certificate[] certs) {
//			return true;
//		}
//
//		public boolean isClientTrusted(
//				java.security.cert.X509Certificate[] certs) {
//			return true;
//		}
//
//		public void checkServerTrusted(
//				java.security.cert.X509Certificate[] certs, String authType)
//				throws java.security.cert.CertificateException {
//			return;
//		}
//
//		public void checkClientTrusted(
//				java.security.cert.X509Certificate[] certs, String authType)
//				throws java.security.cert.CertificateException {
//			return;
//		}
//	}
public void netValidator(){
	Validator v = new Validator();
	v.validator();
}
	public void run() {
		LOG.info("ENTER run()");
		// while(running){
		// try {
		// sleep(1000);
		// LOG.info("\nENTER run()");
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		// 1 捉取的網址 ，一次抓取二十個，如: x101、x102
		// 1從列表取出並轉成物件如: 陣列
		// 2用迴圈或其它去跑執行換網址的動作
		// 2 儲成html檔
		// 3 同時存入資料庫
		// 設定要抓取的網頁
		String[] stockIDArray = { "0050", "3022", "5880", "2455" };
		StringBuilder urlStr = new StringBuilder(70);
		Document doc;
		urlStr.append("https://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022");
		LOG.info("\nurSTR= " + urlStr.toString() + "\n");
		try {
			netValidator();
			for (int i = 0; i < stockIDArray.length; i++) {
				LOG.info("\nurlSTR= " + urlStr.toString());
			
				doc = Jsoup.connect(urlStr.toString()).get();
				writerToFile("D:/Stock/test_"
						+ stockIDArray[i]
						+ "_"
						+ new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
								Locale.ENGLISH).format(new Date()) + ".html",
						doc.toString());// test
				if (i + 1 < stockIDArray.length) {
					urlStr.replace(62, 66, stockIDArray[i + 1]);
				}
//				Parse p = new Parse();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// urlStr.append("http://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022");
		// urlStr.append("http://www.cwb.gov.tw/V7/forecast/week/week.htm");
		// //test
		// 威強電代號3002 ，中鋼是2002 change
		// urlStr.delete(62, 66);
		// urlStr.append("2002");
		// System.out.println(urlStr.toString());
		// String url =
		// "http://goodinfo.tw/StockInfo/StockDividendPolicy.asp?STOCK_ID=3022";
		// try {
		// 將url所顯示的原始碼取下來後，轉為物件Document doc
		// Document doc = Jsoup.connect(urlStr.toString()).get();

		// System.out.println("1 " + doc.title());
		// System.out.println("2 " + doc.toString());

		// 取下來的網頁資料存放入這個檔案
		// Writer("D:/input_20170531.html", doc.toString());
		// new Date() May 31 23:18:26 CST 2017
		// writerToFile("D:/test_"
		// + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
		// Locale.ENGLISH).format(new Date()) + ".html",
		// doc.toString());// test
		//
		// Elements h1s = doc.select("span.titletext");
		//
		// Element thisOne = null;
		// for (Iterator it = h1s.iterator(); it.hasNext();) {
		// thisOne = (Element) it.next();
		// System.out.println(thisOne.html());
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public static void writerToFile(String pathName, String data) {
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

	public static void changeNumber(String newnumber, StringBuilder urlstr) {
		urlstr.delete(62, 66);
		urlstr.append(newnumber);
	}
}
