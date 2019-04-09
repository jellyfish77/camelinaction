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
				.routeId("inputCsvFileRoute")
				.wireTap("jms:ch03.movies.tap00.inputFileAudit")
				// split CSV, streaming one line at a time to reduce mem req
				.split(body().tokenize(System.getProperty("line.separator"))).streaming()
					.process(new MovieCsvToXMLProcessor())
					.to("jms:transformedMessage")				
				// end split
				.end()
				.log("Finished transformation of inbound CSV file: ${header.CamelFileName}")
				;
								
				from("jms:transformedMessage")				
				.routeId("transformedMessageRoute")
				.wireTap("jms:ch03.movies.tap01.tranformedMesageAudit")
				.choice()
					.when(xpath("/Movie/ImdbScore>=8.5")) 
						//.log("Great movie found")						
						.to("jms:ch03.movies.queue.greatMovie")						
					.when(xpath("/Movie/ImdbScore < 8.5 and /Movie/ImdbScore>=7.5")) 
						//.log("Good movie found")						
						.to("jms:ch03.movies.queue.goodMovie")
					.when().xpath("/Movie/ImdbScore < 7.5 and /Movie/ImdbScore>=6")
						//.log("OK movie found")
						.to("jms:ch03.movies.queue.okMovie")
					.when().xpath("/Movie/ImdbScore < 6 and /Movie/ImdbScore>=5")
						//.log("Poor movie found")
						.to("jms:ch03.movies.queue.poorMovie")				
					.otherwise()						
						.to("jms:ch03.movies.queue.badMovie")
				.end() //choice
				;				
				
				from("jms:ch03.movies.tap00.inputFileAudit")
				.routeId("inputFileAuditRoute")
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.tap01.tranformedMesageAudit")
				.routeId("tranformedMesageAuditRoute")
				.process(new MessageLogger())
				;
								
				from("jms:ch03.movies.queue.greatMovie")
				.routeId("greatMovieRoute")				
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.queue.goodMovie")
				.routeId("goodMovieRoute")				
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.queue.okMovie")
				.routeId("okMovieRoute")				
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.queue.poorMovie")
				.routeId("poorMovieRoute")				
				.process(new MessageLogger())
				;
				
				from("jms:ch03.movies.queue.badMovie")
				.routeId("badMovieRoute")				
				.process(new MessageLogger())
				;
								
				//.to("file:src/data/output?fileName=movies.xml")
				
				LOG.info("Route configured.");
			}
		});
        context.start();
		Thread.sleep(10000);
		context.stop();
	}
}