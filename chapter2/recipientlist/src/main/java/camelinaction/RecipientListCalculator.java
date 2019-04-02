package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/*
 * OQ
 * 
 * Add the list of recipients (jms queues) to message header
 * 
 */
public class RecipientListCalculator implements Processor {
	
	public void process(Exchange exchange) throws Exception {
		
		String recipients = "jms:accounting";
        String customer = exchange.getIn().getHeader("customer", String.class);
        if (customer.equals("honda")) {
            recipients += ",jms:production";
        }
        exchange.getIn().setHeader("recipients", recipients);
		
	}

}
