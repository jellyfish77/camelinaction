package csvtoxml.javadsl;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;

import annotations.Author;
import loggers.DownloadLogger;
import loggers.MessageLogger;

@Author(name = "jellyfish77")
public class CSVToXMLTransformation {
	public static void main(String[] args) throws Exception {
		
		CamelContext context = new DefaultCamelContext();
		
		// connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
				
        final Logger LOG = LoggerFactory.getLogger(CSVToXMLTransformation.class);
        
        context.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				
				LOG.info("Configuring route... ");
				
				from("file:src/data/input?delay=60s&fileName=movie_metadata.csv&maxMessagesPerPoll=1&noop=true&idempotent=true")
				.wireTap("jms:ch03.movies.tap00.inputFileAudit")
				.process(new TransformCsvMoviesToXml())
				.to("jms:transformedMessage")				
				;
								
				from("jms:transformedMessage")
				.wireTap("jms:ch03.movies.tap01.tranformedMesageAudit")
				.to("file:src/data/output?fileName=movies.xml")
				;
				
				
				from("jms:ch03.movies.tap00.inputFileAudit")
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.tap01.tranformedMesageAudit")
				.process(new MessageLogger())
				;
								
				LOG.info("Route configured.");
			}
		});
        context.start();
		Thread.sleep(5000);
		context.stop();
	}
}