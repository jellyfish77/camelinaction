package otto.utils;

import java.sql.*;
//import java.util.Calendar;
import java.text.DateFormat;
//import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
//import java.time.format.DateTimeFormatter;
//import java.util;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MessageLogger implements Processor {
	public void process(Exchange exchange) throws Exception {
		String exchangeQuery = "INSERT INTO exchanges (" + " server," + " application," + " pattern," + " exchange_id," + " created_timestamp"
				+ " ) VALUES (" + "?, ?, ?, ?, ?)";
		
		String messageQuery = "INSERT INTO messages (" + " exchange_id," + " message_id," + " direction," + " payload"
				+ " ) VALUES (" + "?, ?, ?, ?)";
		
		String headerQuery = "INSERT INTO headers (" + " message_id," + " name," + " value"
				+ " ) VALUES (" + "?, ?, ?)";
				
		Map props = exchange.getProperties();
				
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");		
		java.util.Date dt = formatter.parse(props.get("CamelCreatedTimestamp").toString());						
		Timestamp ts = Timestamp.valueOf(LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault()));
				
		try {		
			String myUrl = "jdbc:mysql://localhost/integration?autoReconnect=true&useSSL=false";
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection(myUrl, "root", "root");
			conn.setAutoCommit(false);
			
			PreparedStatement st = conn.prepareStatement(exchangeQuery);
			st.setString(1, InetAddress.getLocalHost().getHostName());
			st.setString(2, "Apache Camel");
			st.setString(3, exchange.getIn().getExchange().getPattern().toString());
			st.setString(4, exchange.getIn().getExchange().getExchangeId().toString());
			//st.setDate(5, java.sql.Date.valueOf(dt.toString()));
			st.setTimestamp(5, ts);
			st.addBatch();
			st.executeBatch();			
			System.out.println("Executing statements as batch: " + st);
			
			st = conn.prepareStatement(messageQuery);
			st.setString(1, exchange.getIn().getExchange().getExchangeId());
			st.setString(2, exchange.getIn().getMessageId());
			st.setString(3, "in");
			st.setString(4, exchange.getIn().getBody(String.class));
			st.addBatch();			
			System.out.println("Executing statements as batch: " + st);
			st.executeBatch();			
			
			st = conn.prepareStatement(headerQuery);
			st.setString(1, exchange.getIn().getMessageId());
			st.setString(2, "CamelFilePath");
			st.setString(3, exchange.getIn().getHeader("CamelFilePath").toString());
			st.addBatch();			
			System.out.println("Executing statements as batch: " + st);
			st.executeBatch();					
			
			st = conn.prepareStatement(headerQuery);
			st.setString(1, exchange.getIn().getMessageId());
			st.setString(2, "CamelFileLength");
			st.setString(3, exchange.getIn().getHeader("CamelFileLength").toString());
			st.addBatch();			
			System.out.println("Executing statements as batch: " + st);
			st.executeBatch();			
			
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(Long.parseLong(exchange.getIn().getHeader(Exchange.FILE_LAST_MODIFIED).toString()));			
		    final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

			st = conn.prepareStatement(headerQuery);
			st.setString(1, exchange.getIn().getMessageId());
			st.setString(2, "CamelFileLastModified");
			st.setString(3, dateFormat.format(c.getTime()));
			st.addBatch();			
			System.out.println("Executing statements as batch: " + st);
			st.executeBatch();			
			
			conn.commit();
			st.close();
			conn.close();
		} catch (UnknownHostException e) {		 
            e.printStackTrace();        
		} catch (SQLException se) {
			// log exception
			throw se;
		}
	}
}