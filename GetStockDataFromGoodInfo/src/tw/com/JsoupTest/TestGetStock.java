package tw.com.JsoupTest;
import org.apache.log4j.Logger;

public class TestGetStock {
	private final static Logger LOG = Logger.getLogger(TestGetStock.class);
	public TestGetStock(){
		init();
	}
	public void init(){
		GetStockHtml getStock = new GetStockHtml();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestGetStock();
		}

}
