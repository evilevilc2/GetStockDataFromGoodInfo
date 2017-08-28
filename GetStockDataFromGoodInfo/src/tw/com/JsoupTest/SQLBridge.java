package tw.com.JsoupTest;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author chuck
 * 
 *         1.�{���\��G�s����Ʈw�ΰ���SQL��O�����O 2.�{���W�١GSQLBridge 3.�إߤ���G2004.2.25
 */

public class SQLBridge {
	private static Logger log = Logger.getLogger(SQLBridge.class);
	private Connection conn;
	private Connection conn_his;
	ResultSet rs;
	private ResultSet rs_his;
	private ResultSetMetaData rsmd;
	private Statement stmt;

	private String sybaseDrvName;
	private String SybaseUrl;

	private String SQLsevDrvName;
	private String SQLsevUrl;

	private String sybaseUsrName;
	private String sybasePW;

	private String SQLsevUsrName;
	private String SQLsevPW;

	private String PGSQLDrvName;
	private String PGSQLUrl;
	private String PGSQLUsrName;
	private String PGSQLPW;

	private String SQLiteDrvName;
	private String SQLiteUrl;

	private Properties readPara;

	String dday;

	public SQLBridge() throws SQLException, InstantiationException,
			IllegalAccessException, IOException {/* SQLBridge�غc�l�]�w�w�]�� */
		Properties prob = new Properties();
		try {
			prob.load(SQLBridge.class.getClassLoader().getResourceAsStream(
					"./log4j.properties"));
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
		PropertyConfigurator.configure(prob);

		conn = null;
		rs = null;
		rsmd = null;
		stmt = null;
		readPara = getProperties();

		SQLsevDrvName = readPara.getProperty("SQLsevDrvName");
		SQLsevUrl = readPara.getProperty("SQLsevUrl");
		SQLsevUsrName = readPara.getProperty("SQLsevUsrName");
		SQLsevPW = readPara.getProperty("SQLsevPW");

		PGSQLDrvName = readPara.getProperty("PGSQLDrvName");
		PGSQLUrl = readPara.getProperty("PGSQLUrl");
		PGSQLUsrName = readPara.getProperty("PGSQLUsrName");
		PGSQLPW = readPara.getProperty("PGSQLPW");

		SQLiteDrvName = readPara.getProperty("SQLiteDrvName");
		SQLiteUrl = readPara.getProperty("SQLiteUrl");

		dday = readPara.getProperty("dday");
	}

	/**
	 * Method name :openDB�s����Ʈw��k
	 * 
	 * @param drvName
	 *            Class�n��J���X�ʵ{��(sun.jdbc.odbc.Jdbc0dbcDriver)
	 * @param url
	 *            ��ƨӷ�(jdbc:odbc:<sourcename>)
	 * @param uname
	 *            ��Ʈw���ϥΪ̦W��
	 * @param passwd
	 *            ��Ʈw���ϥΪ̱K�X
	 * @throws SQLException
	 */

	/* �s��sybase��Ʈw��k */
	public void openSybaseDB() throws SQLException {

		// String drvName ="com.sybase.jdbc2.jdbc.SybDriver";
		String drvName = "net.sourceforge.jtds.jdbc.Driver";
		// String url ="jdbc:sybase:Tds:MICU2:5000/cis_local";
		// String url ="jdbc:jtds:sybase://MICU2:5000/cis_local";
		String url = "jdbc:jtds:sybase://OR:5000/cis_local";
		String uname = "sa";
		String passwd = "";
		if (conn != null && !conn.isClosed()) {/*
												 * �Yconnection�������Bconn�ܼƻݭn�A�s�A
												 * �h����SQLException
												 */
			throw new SQLException(
					"The connection has been established already.");
		}
		clearResult();
		try {
			Class.forName(sybaseDrvName);// �NJDBC�X�ʵ{�������O��J��JVM���C
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex.toString());
		}
		System.out.println("�s��sybase��Ʈw��");
		conn = DriverManager.getConnection(SybaseUrl, sybaseUsrName, sybasePW);// ��o�P��Ʈw�����s������
		System.out.println("sybase��Ʈw�s������");
	}

	/* �s��ms_sql��Ʈw��k */
	/* �s��SQL SERVER��Ʈw */
	public void openMSSQLDB() throws SQLException, InstantiationException,
			IllegalAccessException {
		// String drvName ="com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String drvName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // "net
		String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=StockDB;user=sa;password=1234";

//		String drvName = "net.sourceforge.jtds.jdbc.Driver";
		// String drvName ="sun.jdbc.odbc.JdbcOdbcDriver";
		// String url
		// ="jdbc:microsoft:sqlserver://ADMSQL:1433;DatabaseName=DB_ADM";
		// String url = "jdbc:jtds:sqlserver://localhost:1433/QSDB";
//		String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=VIPTempDB;user=sa;password=mayasa";
		// String url ="jdbc:odbc:ADMSQL";
		if (conn_his != null && !conn_his.isClosed()) {/*
														 * �Yconnection�������Bconn�ܼƻݭn�A�s
														 * �A�h����SQLException
														 */
			throw new SQLException(
					"The connection has been established already.");
		}
		clearResult();
		try {
			Class.forName(SQLsevDrvName);// �NJDBC�X�ʵ{�������O��J��JVM���C
		} catch (ClassNotFoundException ex) {
			// throw new SQLException(ex.toString());
			log.error(ex.getMessage(), ex);
		}
		log.info("\n連結MSSQL資料庫中");
		// conn_his =
		// DriverManager.getConnection(SQLsevUrl,SQLsevUsrName,SQLsevPW);//��o�P��Ʈw�����s������
		conn = DriverManager.getConnection(SQLsevUrl);// ��o�P��Ʈw�����s������
		log.info("MSSQL資料庫連結完成");
	}

	/**
	 * �䥦�����ǤJ�ɸ��d�߯f�wID
	 * 
	 * @param chICUBedNo
	 *            ICU�ɸ�
	 * @return chPatientID(�f�wID)
	 * @throws SQLException
	 */
	public String getPAID(String chICUBedNo) throws SQLException {
		String chPatientID = null;

		/*
		 * if(conn_his == null || conn_his.isClosed()){ throw new
		 * SQLException("This HIS connection has not been established yet."); }
		 * String chPatientID=null; //�d�߯f�wID(chPatientID)
		 * 
		 * String sql_his
		 * ="SELECT chAd1MrNo from AdmBasicTbl where chAd1Bid= '"+
		 * chICUBedNo+"'"; //String sql_his ="SELECT * from AdmBasicTbl ";
		 * conn_his.setAutoCommit(true); Statement stmt_his =
		 * conn_his.createStatement(); rs_his =stmt_his.executeQuery( sql_his);
		 * while(rs_his.next()){ chPatientID=rs_his.getString("chAd1MrNo"); }
		 */

		return chPatientID;

	}

	/**
	 * ��o���W�Ǹ��
	 * 
	 * @return rs
	 */
	public ResultSet getNotUploadData() {
		// ResultSet rs = null;
		String selSql = "SELECT * FROM VIPBPTbl WHERE status='N' ORDER BY id ASC";
		try {
			execSQL(selSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}
		return rs;
	}

	public boolean isConnected() {
		boolean b = false;
		String sql = "SELECT COUNT(*) FROM VIPBPTbl WHERE 1=1";
		try {
			if (execSQL(sql) == -1) {
				b = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}
		return b;
	}

	/**
	 * �ǤJ�}�M�Щи��Τ������A��o�}�M�Яf�wID
	 * 
	 * @param chICUBedNo
	 *            �}�M�Щи�
	 * @param todate
	 *            ������(����榡���T��ƥ���~yyymmdd)
	 * @return chPatientID(�f�wID)
	 * @throws SQLException
	 */
	public String getORPAID(String chICUBedNo, String todate)
			throws SQLException {
		String chPatientID = null;
		/*
		 * if(conn_his == null || conn_his.isClosed()){ throw new
		 * SQLException("This HIS connection has not been established yet."); }
		 * String chPatientID=null; �q�}�M�Ьd�߯f�wID(chPatientID) �Ntodate�ഫ���r�� String
		 * sql_his
		 * ="SELECT top 1 chOrMrNo from AdmORSchTbl where chOrRoom='"+chICUBedNo
		 * +
		 * "' and substring(chOrChkIn,1,7) >= '"+todate+"' and (chOrChkOut IS NULL)"
		 * ; conn_his.setAutoCommit(true); Statement stmt_his =
		 * conn_his.createStatement(); rs_his =stmt_his.executeQuery( sql_his);
		 * while(rs_his.next()){ chPatientID=rs_his.getString("chOrMrNo");
		 * 
		 * }
		 */
		return chPatientID;

	}

	public int updateBPData_by_ID(String id) {
		int i = 0;
		String sql = "UPDATE VIPBPTbl SET status='2' WHERE id='" + id + "'";
		try {
			i = execSQL(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return i;
	}

	public void deleteDataBefore() {
		Date d = new Date();
		long l = 24*24*60*60*1000;
		int quo = Integer.parseInt(dday) / 24;
		long ll = (Integer.parseInt(dday) - (quo * 24))*24*60*60*1000;
//		d.setTime(new Date().getTime() - Integer.parseInt(dday) * 24 * 60 * 60 * 1000);
		d.setTime(d.getTime() - (quo * l) - ll);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(d);
//		log.debug("date = " + date);
		String delSql = "DELETE FROM VIPBPTbl WHERE data_time <= '" + date + "'";
		log.info(delSql);
		try {
			int i = execSQL(delSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	/* �s��MySql��Ʈw��k */
	public void openMySqlDB() throws SQLException {
		String drvName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/instrument";
		String uname = "root";
		String passwd = "";
		if (conn != null && !conn.isClosed()) {/*
												 * �Yconnection�������Bconn�ܼƻݭn�A�s�A
												 * �h����SQLException
												 */
			throw new SQLException(
					"The connection has been established already.");
		}
		clearResult();
		try {
			Class.forName(drvName);// �NJDBC�X�ʵ{�������O��J��JVM���C
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex.toString());
		}
		conn = DriverManager.getConnection(url, uname, passwd);// ��o�P��Ʈw�����s������

	}

	/* �s��PostgreSQL��Ʈw��k */
	public void openPGSQLDB() throws SQLException {
		String drvName = "org.postgresql.Driver";
		// String url ="jdbc:postgresql://192.168.127.1:5432";
		String url = "jdbc:postgresql://localhost:5432/DRDB";
		String uname = "sa";
		String passwd = "mayasa";
		if (conn != null && !conn.isClosed()) {/*
												 * �Yconnection�������Bconn�ܼƻݭn�A�s�A
												 * �h����SQLException
												 */
			throw new SQLException(
					"The connection has been established already.");
		}
		clearResult();
		try {
			// Class.forName(drvName);//�NJDBC�X�ʵ{�������O��J��JVM���C
			Class.forName(PGSQLDrvName);// �NJDBC�X�ʵ{�������O��J��JVM���C
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex.toString());
		}
		System.out.println("�s��PostgreSQL��Ʈw��..");
		// conn = DriverManager.getConnection(url,uname,passwd);//��o�P��Ʈw�����s������
		conn = DriverManager.getConnection(PGSQLUrl, PGSQLUsrName, PGSQLPW);// ��o�P��Ʈw�����s������
		System.out.println("PostgreSQL��Ʈw�s������..");
	}

	/* �s��SQLite��Ʈw��k */
	public void openSqlite() throws SQLException {
		if (conn != null && !conn.isClosed()) {/*
												 * �Yconnection�������Bconn�ܼƻݭn�A�s�A
												 * �h����SQLException
												 */
			throw new SQLException(
					"The connection has been established already.");
		}
		clearResult();
		try {
			Class.forName(SQLiteDrvName);// �NJDBC�X�ʵ{�������O��J��JVM���C
		} catch (ClassNotFoundException ex) {
			throw new SQLException(ex.toString());
		}
		System.out.println("connecting sqlite db..");
		conn = DriverManager.getConnection(SQLiteUrl);// ��o�P��Ʈw�����s������
		System.out.println("sqlite db connected..");
	}

	/**
	 * Ū�Jproperties�ɮת�method
	 * 
	 * @return
	 * @throws IOException
	 */
	public Properties getProperties() throws IOException {
		Properties readHisPara = new Properties();
		FileInputStream fin = new FileInputStream("./connDB.ini");
		readHisPara.load(fin);
		fin.close();

		return readHisPara;
	}

	/**
	 * Method
	 * clearResult.�M��rs(ResultSet)�Bstmt(Statement)��rsmd(ResultSetMetaData)���T�Ӫ����
	 * 
	 * @throws SQLException
	 */
	private void clearResult() throws SQLException {
		if (rs != null) {
			rs.close();
		}
		rs = null;
		if (stmt != null) {
			stmt.close();
		}
		stmt = null;
		rsmd = null;
	}

	/**
	 * Method name:execSQL����SQL��O(�浧)
	 * 
	 * @param sqlStmt
	 *            SQL��O
	 * @return int -1��select��O,�䥦���ק�B�s�W������
	 * @throws SQLException
	 */
	/* �ǰeSQL��O */
	public int execSQL(String sqlStmt) throws SQLException {
		// if(conn == null || conn.isClosed()){
		// throw new
		// SQLException("This connection has not been established yet.");
		// }
		if (sqlStmt == null) {
			throw new SQLException("SQL statement is null.");
		}
		clearResult();
		// conn.setAutoCommit(true); //��connection����O�_�@��B�z�@��transactions
		stmt = conn.createStatement();
		if (sqlStmt.toUpperCase().startsWith("SELECT")) {
			rs = stmt.executeQuery(sqlStmt);
			rsmd = rs.getMetaData(); // �i�o��ResultSet���󤺪�����T(�p�G������ӼơB��쫬�A��)
			if (stmt != null) {
				stmt.cancel();
				stmt = null;
			}
			return -1;
		} else {
			System.out.println(sqlStmt);
			int numRow = stmt.executeUpdate(sqlStmt);
			clearResult();
			if (stmt != null) {
				stmt.cancel();
				stmt = null;
			}
			return numRow;
		}

	}

	/**
	 * Method name:execSQL����SQL��O(�h��) �@���ǻ��h�խק�R�O����Ʈw
	 * 
	 * @param sqlStmts
	 *            ���n���檺SQL�}�C
	 * @throws SQLException
	 */
	/* �ǰeSQL��O---�@���ǻ��h�խק�R�O����Ʈw */
	public void execUpdate(String[] sqlStmts) throws SQLException {
		if (conn == null || conn.isClosed()) {
			throw new SQLException(
					"This connection has not been established yet.");
		}
		if (sqlStmts == null || sqlStmts.length == 0) {
			throw new SQLException("SQL statement is null.");
		}
		clearResult();
		conn.setAutoCommit(false);
		try {
			for (int i = 0; i < sqlStmts.length; i++) {
				stmt = conn.createStatement();
				stmt.executeUpdate(sqlStmts[i]);
				stmt.close();
			}
			conn.commit();
		} catch (SQLException ex) {
			conn.rollback();
			throw ex;
		}
	}

	/**
	 * ��o��檺���Ӽ�
	 * 
	 * @return int ��檺���Ӽ�
	 * @throws SQLException
	 */
	/* ��o��檺���Ӽ� */
	public int getColumnCount() throws SQLException {
		if (rsmd == null) {
			throw new SQLException("ResultSet is null");
		}
		return rsmd.getColumnCount();
	}

	/**
	 * ��o�����쪺�W�٨öǦ^�W�ٰ}�C
	 * 
	 * @return String[]���W�ٰ}�C
	 * @throws SQLException
	 */
	/* ��o�����쪺�W�٨öǦ^�W�ٰ}�C */
	public String[] getColumnNames() throws SQLException {
		if (rsmd == null) {
			throw new SQLException("ResultSet is null");
		}
		// System.out.println(rsmd.getColumnCount());
		String[] columnNames = new String[rsmd.getColumnCount()];

		for (int i = 0; i < columnNames.length; i++) {
			// System.out.println(rsmd.getColumnName(i+1));
			columnNames[i] = rsmd.getColumnName(i + 1);
		}
		return columnNames;
	}

	
	// (<stock_name, varchar(50),>
	// ,<stock_info, varchar(200),>
	// ,<stock_pbr_info, varchar(200),>
	// ,<stock_price, varchar(50),>
	// ,<netchange_price, varchar(50),>
	// ,<down_person, varchar(50),>
	// ,<yes_stock_price, varchar(50),>
	// ,<opening_price, varchar(50),>
	// ,<highprice, varchar(50),>
	// ,<lowprice, varchar(50),>
	// ,<total_volume, varchar(20),>
	// ,<total_price, varchar(20),>
	// ,<stock_count, varchar(50),>
	// ,<stock_avgvolume, varchar(50),>
	// ,<stock_avgprice, varchar(50),>
	// ,<PER, varchar(50),>
	// ,<PBR, varchar(50),>
	// ,<y_volume, varchar(50),>
	// ,<y_price, varchar(50),>
	// ,<y_count, varchar(50),>
	// ,<y_avgvolume, varchar(50),>
	// ,<y_avgprice, varchar(50),>
	// ,<y_netchange, varchar(50),>
	// ,<extend, varchar(20),>
	// ,<data_time, varchar(20),>
	// ,<status, varchar(1),>
	// ,<create_time, nvarchar(24),>
	// ,<upload_time, nvarchar(24),>)
	public int insertStockData(
			Object stock_id, Object stock_name, Object stock_info,
			Object stock_pbr_info, Object stock_price, Object netchange_price,
			Object down_person, Object yes_stock_prices,Object opening_price,
			Object highprice, Object lowprice, Object total_volume, 
			Object total_price, Object stock_count, Object stock_avgvolume,
			Object stock_avgprice,Object PER , Object PBR,
			Object y_volume, Object y_price, Object y_count,
			Object y_avgvolume, Object y_avgprice, Object y_netchange,
//			String stock_id, String stock_name, String stock_info,
//			String stock_pbr_info, String stock_price, String netchange_price,
//			String down_person, String yes_stock_prices,String opening_price,
//			String highprice, String lowprice, String total_volume, 
//			String total_price, String stock_count, String stock_avgvolume,
//			String stock_avgprice,String PER , String PBR,
//			String y_volume, String y_price, String y_count,
//			String y_avgvolume, String y_avgprice, String y_netchange,
			
			String extend, String status,Object create_time
			, String upload_time, Object stock_datetime) throws SQLException {

		String ivpumpSql = "INSERT INTO StockTbl VALUES ( "
				+ "'" + stock_id+ "','" 
				+ stock_name + "', '" 
				+ stock_info + "', '"
				+ stock_pbr_info + "', '" 
				+ stock_price + "', '" 
				+ netchange_price + "', '"
				
				+ down_person + "', '" 
				+ yes_stock_prices + "', '"
				+ opening_price + "', '" 
				+ highprice + "', '" 
				+ lowprice + "', '"
				+ total_volume +"', '"
				
				+ total_price + "', '" 
				+ stock_count + "', '"
				+ stock_avgvolume + "', '" 
				+ stock_avgprice + "', '" 
				+ PER + "', '"
				+ PBR +"', '"
				
				+ y_volume + "', '" 
				+ y_price + "', '"
				+ y_count + "', '" 
				+ y_avgvolume + "', '" 
				+ y_avgprice + "', '"
				+ y_netchange +"', '"
				
				+ extend + "', '" 
				+ status + "', '" 
				+ create_time + "', '" 
				+ upload_time + "', '" 
				
				+ stock_datetime + "'  )";
		int insetNum = execSQL(ivpumpSql);
		
		return insetNum;
	}
	
	
	/**
	 * �ǤJiv pump���Ѽƨ�inset��local database
	 * 
	 * @param chPatientID
	 * @param chDataDateTime
	 * @param chParaID
	 * @param chICUBedNo
	 * @param chDeviceNo
	 * @param rlValue
	 * @param chStatus
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */
	public int insertIVPData(String chPatientID, String chDataDateTime,
			String chParaID, String chICUBedNo, String chDeviceNo,
			String rlValue, String chStatus) throws SQLException {

		String ivpumpSql = "INSERT INTO CISIVPDataTbl VALUES ( '" + chPatientID
				+ "','" + chDataDateTime + "', '" + chParaID + "', '"
				+ chICUBedNo + "', '" + chDeviceNo + "', '" + rlValue + "', '"
				+ chStatus + "'  )";
		int insetNum = execSQL(ivpumpSql);

		return insetNum;
	}

	/**
	 * �ǤJIABP���Ѽƨ�inset��local database_�Ὤ�Ϊ�
	 * 
	 * @param chPatientID
	 * @param chDataDateTime
	 * @param chParaID
	 * @param chICUBedNo
	 * @param chDeviceNo
	 * @param rlValue
	 * @param chStatus
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */

	public int insertIABPData(String chPatientID, String chDataDateTime,
			String chParaID, String chICUBedNo, String chDeviceNo,
			String rlValue, float rlHIValueLimit, float rlLOValueLimitf,
			String chStatus) throws SQLException {

		String ivpumpSql = "INSERT INTO dbo.CISIABPDataTbl   VALUES ( '"
				+ chPatientID + "','" + chDataDateTime + "', '" + chParaID
				+ "', '" + chICUBedNo + "', '" + chDeviceNo + "', '" + rlValue
				+ "', " + rlHIValueLimit + ", " + rlLOValueLimitf + ", '"
				+ chStatus + "'  )";
		int insetNum = execSQL(ivpumpSql);

		return insetNum;
	}
	
	public boolean isRepeat(String patient_id, String data_time) {
		boolean b = false;
		String sqlstr = "select count(*) from VIPBPTBL where patient_id='"
				+ patient_id + "' and data_time='" + data_time + "'";
		log.info("SQL_isRepeat = " + sqlstr);
		try {
			execSQL(sqlstr);
			if(rs.next()){
				String s = rs.getString(1);
				if(s != null && !s.equals("0")){
					b = true;
				}
			}
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * �ǤJIABP���Ѽƨ�inset��local database_�j�L�Ϊ�
	 * 
	 * @param chPatientID
	 * @param chDataDateTime
	 * @param chParaID
	 * @param chICUBedNo
	 * @param chDeviceNo
	 * @param rlValue
	 * @param chStatus
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */

	public int insertIABPData(String chPatientID, String chDataDateTime,
			String chParaID, String chICUBedNo, String chDeviceNo,
			String rlValue, String chStatus) throws SQLException {

		String ivpumpSql = "INSERT INTO dbo.CISIABPDataTbl   VALUES ( '"
				+ chPatientID + "','" + chDataDateTime + "', '" + chParaID
				+ "', '" + chICUBedNo + "', '" + chDeviceNo + "', '" + rlValue
				+ "', '" + chStatus + "'  )";
		int insetNum = execSQL(ivpumpSql);

		return insetNum;
	}

	/**
	 * �ǤJ�I�l�����Ѽƨ�inset��local database
	 * 
	 * @param chPatientID
	 * @param chDataDateTime
	 * @param chParaID
	 * @param chICUBedNo
	 * @param chDeviceNo
	 * @param rlValue
	 * @param chStatus
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */
	public int insertRTData(String chPatientID, String chDataDateTime,
			String chParaID, String chICUBedNo, String chDeviceNo,
			String rlValue, String chStatus) throws SQLException {

		String ivpumpSql = "INSERT INTO CISRTDataTbl  VALUES ( '" + chPatientID
				+ "','" + chDataDateTime + "', '" + chParaID + "', '"
				+ chICUBedNo + "', '" + chDeviceNo + "', '" + rlValue + "', '"
				+ chStatus + "'  )";
		int insetNum = execSQL(ivpumpSql);

		return insetNum;
	}

	/**
	 * �ǤJ�¾K�����Ѽƨ�inset��local database
	 * 
	 * @param chPatientID
	 * @param chDataDateTime
	 * @param chParaID
	 * @param chICUBedNo
	 * @param chDeviceNo
	 * @param rlValue
	 * @param chStatus
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */
	public int insertAnesData(String chPatientID, String chDataDateTime,
			String chParaID, String chICUBedNo, String chDeviceNo,
			String rlValue, String chStatus) throws SQLException {

		String ivpumpSql = "INSERT INTO CISAnesDataTbl VALUES ( '"
				+ chPatientID + "','" + chDataDateTime + "', '" + chParaID
				+ "', '" + chICUBedNo + "', '" + chDeviceNo + "', '" + rlValue
				+ "', '" + chStatus + "'  )";
		int insetNum = execSQL(ivpumpSql);

		return insetNum;
	}

	/**
	 * �ǤJFetal Monitor���Ѽƨ�inset��local database
	 * 
	 * @param med_no
	 * @param data_dt
	 * @param data_fhr1
	 * @param data_fhr2
	 * @param data_ua
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */
	public int insertFMData(String med_no, String data_dt, String data_fhr1,
			String data_fhr2, String data_ua) throws SQLException {

		String fmSql = "INSERT INTO ogd_t_d_med_data (med_no, data_dt, data_fhr1, data_fhr2, data_ua) "
				+ " VALUES ('"
				+ med_no
				+ "', '"
				+ data_dt
				+ "', '"
				+ data_fhr1
				+ "', '" + data_fhr2 + "', '" + data_ua + "')";

		System.out.println("fmsql:" + fmSql + "..");
		int insetNum = execSQL(fmSql);

		return insetNum;
	}

	/**
	 * �ǤJProcare���Ѽƨ�inset��local database
	 * 
	 * @param med_no
	 * @param data_dt
	 * @param data_systolic
	 * @param data_diastolic
	 * @param data_map
	 * @param data_spo2
	 * @param data_pulserate
	 * @return int �^�зs�W������
	 * @throws SQLException
	 */
	public int insertProcareData(String med_no, String data_dt,
			String data_systolic, String data_diastolic, String data_map,
			String data_spo2, String data_pulserate) throws SQLException {

		String proSql = "INSERT INTO procare_data (med_no, data_dt, data_systolic, data_diastolic, data_map, data_spo2, data_pulserate) "
				+ " VALUES ('"
				+ med_no
				+ "','"
				+ data_dt
				+ "', '"
				+ data_systolic
				+ "', '"
				+ data_diastolic
				+ "', '"
				+ data_map
				+ "', '" + data_spo2 + "', '" + data_pulserate + "')";
		int insetNum = execSQL(proSql);

		return insetNum;
	}

	// for maya db
	// id, op_id, patient_id, id_source, location, sys, dia, map, spo2, pulse,
	// height, weight, temp, rr, vip_ip, data_time, status
	public int insertProcareDataSqlserver(String op_id, String patient_id,
			String id_source, String location, String data_sys,
			String data_dia, String data_map, String data_spo2,
			String data_pulse, String data_height, String data_weight,
			String data_temp, String data_rr, String vip_ip, String data_time,
			String status, String upload_time) throws SQLException {
		// int insetNum = -1;
		// String sql = "SELECT COUNT(*) FROM VIPBPTbl WHERE patient_id='"
		// + patient_id + "' AND data_time='" + data_time + "'";
		// log.info("select repeat data : " + sql);
		// execSQL(sql);
		// if(rs.next()){
		// if(rs.getString(1).equals("0")){
		String proSql = "INSERT INTO VIPBPTbl (op_id, patient_id, id_source, location, sys, dia, map, spo2, pulse, height, weight, temp, rr, vip_ip, data_time, status, upload_time) "
				+ " VALUES ('"
				+ op_id
				+ "','"
				+ patient_id
				+ "', '"
				+ id_source
				+ "', '"
				+ location
				+ "', '"
				+ data_sys
				+ "', '"
				+ data_dia
				+ "', '"
				+ data_map
				+ "', '"
				+ data_spo2
				+ "', '"
				+ data_pulse
				+ "', '"
				+ data_height
				+ "', '"
				+ data_weight
				+ "', '"
				+ data_temp
				+ "', '"
				+ data_rr
				+ "', '"
				+ vip_ip
				+ "', '"
				+ data_time
				+ "', '" + status + "', '" + upload_time + "')";
		int insetNum = execSQL(proSql);
		// }
		// }
		// String proSql =
		// "INSERT INTO VIPBPTbl (op_id, patient_id, id_source, location, sys, dia, map, spo2, pulse, height, weight, temp, rr, vip_ip, data_time, status, upload_time) "
		// + " VALUES ('"
		// + op_id
		// + "','"
		// + patient_id
		// + "', '"
		// + id_source
		// + "', '"
		// + location
		// + "', '"
		// + data_sys
		// + "', '"
		// + data_dia
		// + "', '"
		// + data_map
		// + "', '"
		// + data_spo2
		// + "', '"
		// + data_pulse
		// + "', '"
		// + data_height
		// + "', '"
		// + data_weight
		// + "', '"
		// + data_temp
		// + "', '"
		// + data_rr
		// + "', '"
		// + vip_ip
		// + "', '"
		// + data_time
		// + "', '" + status + "', '" + upload_time + "')";
		// int insetNum = execSQL(proSql);

		return insetNum;
	}

	public int updateUpload(String last_id, String upload_time)
			throws SQLException {
		String updSql = "UPDATE VIPBPTbl set status='1', upload_time='"
				+ upload_time + "' WHERE id='" + last_id + "'";
		int u = execSQL(updSql);
		return u;
	}

	public int updateUploadError(String last_id, String msg)
			throws SQLException {
		String updSql = "UPDATE VIPBPTbl set status='0', upload_time='" + msg
				+ "' WHERE id='" + last_id + "'";
		int u = execSQL(updSql);
		return u;
	}

	public int updateUpload_N(String last_id) throws SQLException {
		String updSql = "UPDATE VIPBPTbl set status='N', upload_time=''"
				+ " WHERE id='" + last_id + "'";
		int u = execSQL(updSql);
		return u;
	}

	public String getLastID() throws SQLException, InterruptedException {
		String selSql = "SELECT MAX(id) as maxid FROM VIPBPTbl";
		int i = execSQL(selSql);
		String last_id = null;
		while (rs.next()) {
			// last_id = rs.getString("maxid");
			last_id = rs.getString(1);
			Thread.sleep(100);
		}
		return last_id;
	}
	
	public String getLastID(String pid, String dtime) throws SQLException, InterruptedException {
//		if(stmt == null){
//			stmt = conn.createStatement();
//		}
		String selSql = "SELECT id FROM VIPBPTbl WHERE patient_id='" + pid + "' AND data_time='" + dtime + "'";
		int i = execSQL(selSql);
		String last_id = null;
		if (rs.next()) {
			// last_id = rs.getString("maxid");
			last_id = rs.getString(1);
//			Thread.sleep(100);
		}else{
			last_id = "";
		}
		return last_id;
	}

	// for vicbmp sqlite
	public int insertProcareDataSqllite(String op_id, String patient_id,
			String id_source, String location, String data_sys,
			String data_dia, String data_map, String data_spo2,
			String data_pulse, String data_height, String data_weight,
			String data_temp, String data_rr, String vip_ip, String data_time)
			throws SQLException {

		String proSql = "INSERT INTO MeasureData (ID, PID, MDT, SYS, DIA, MAP, Pulse, SPO2, Height, Weight, Temperature, RespiratoryRate, op_id, id_source, location, vip_ip, status) "
				+ " VALUES (NULL, '"
				+ patient_id
				+ "', '"
				+ data_time
				+ "', '"
				+ data_sys
				+ "', '"
				+ data_dia
				+ "', '"
				+ data_map
				+ "', '"
				+ data_pulse
				+ "', '"
				+ data_spo2
				+ "', '"
				+ data_height
				+ "', '"
				+ data_weight
				+ "', '"
				+ data_temp
				+ "', '"
				+ data_rr
				+ "', '"
				+ op_id
				+ "','"
				+ id_source
				+ "', '"
				+ location
				+ "', '"
				+ vip_ip
				+ "', '0')";
		int insetNum = execSQL(proSql);

		return insetNum;
	}

	/* ������Ʈw�s�� */
	public void closeDB() throws SQLException {
		clearResult();
		if (conn == null) {
			throw new SQLException("This connection has been closed already");
		}
		if (conn.isClosed()) {
			throw new SQLException("This connection has been closed,");
		}
		conn.close();
		conn = null;
	}

//	public void insertStockData(Object object, Object object2, Object object3,
//			Object object4, Object object5, Object object6, Object object7,
//			Object object8, Object object9, Object object10, Object object11,
//			Object object12, Object object13, Object object14, Object object15,
//			Object object16, Object object17, Object object18, Object object19,
//			Object object20, Object object21, Object object22, Object object23,
//			Object object24, String extend, String status, String format,
//			String upload_time) {
////		int insetNum = execSQL(ivpumpSql);
//
////		return insetNum;
//		// TODO Auto-generated method stub
//		
//	}

}