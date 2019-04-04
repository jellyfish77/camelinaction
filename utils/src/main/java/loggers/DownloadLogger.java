/**
 * 
 */
package loggers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DownloadLogger implements Processor {
    public void process(Exchange exchange) throws Exception {
        System.out.println("Received message... "        		
        		+  "exchangeID: " + exchange.getIn().getExchange().getExchangeId().toString() + "; "
        		+ "breadcrumbId: " + exchange.getIn().getHeader("breadcrumbId") + "; "
        		+ "JMSMessageID: " + exchange.getIn().getHeader("JMSMessageID") + "; "        		
        		+ "CamelFilePath: " + exchange.getIn().getHeader("CamelFilePath") + "; "        		
        		+ "JMSDestination: " + exchange.getIn().getHeader("JMSDestination") + "; "
  				+ "CamelFileNameProduced: " + exchange.getIn().getHeader("CamelFileNameProduced") + "; "  				
  				+ "CamelFileContentType: " + exchange.getIn().getHeader("CamelFileContentType") + "; "
  				+ "BodyType: " + exchange.getIn().getHeader("BodyType") + "; "
  				+ "Body: " + exchange.getIn().getBody().toString()  	
        );
    }
}
