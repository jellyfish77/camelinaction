package camelinaction;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import annotations.Author;
import loggers.DownloadLogger;
import loggers.MessageLogger;

@Author(name = "jellyfish77")
public class TransformFileXslt {

	public static void main(String args[]) throws Exception {
		// create CamelContext
		CamelContext context = new DefaultCamelContext();

		// connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		
		// add our route to the CamelContext
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				System.out.print("Configuring route... ");

				from("file:src/data/input?delay=60s&fileName=message.xml&maxMessagesPerPoll=1&noop=true&idempotent=true")
				.wireTap("jms:0-inputFileAudit")
				.to("jms:incomingMessages")
				;
				
				from("jms:incomingMessages")
				.wireTap("jms:1-incomingMessagesAudit")
				.to("xslt://camelinaction/transform.xslt")				
				.to("jms:transformedMessages")
				;
				
				from("jms:transformedMessages")
				.wireTap("jms:2-transformedMessagesAudit")				
				.to("file://src/data/output?fileName=transformed_message-${header.Date}.xml&readLockMarkerFile=false")
				.wireTap("jms:3-outputFileAudit")
				;
				
				from("jms:0-inputFileAudit")
				.process(new MessageLogger()).process(new DownloadLogger())
				;
				
				from("jms:1-incomingMessagesAudit")
				.process(new MessageLogger()).process(new DownloadLogger())
				;
				
				from("jms:2-transformedMessagesAudit")
				.process(new MessageLogger()).process(new DownloadLogger())				
				;
				
				from("jms:3-outputFileAudit")
				.process(new MessageLogger()).process(new DownloadLogger())
				.to("log:camelinaction?showAll=true&multiline=true")
				;
								
				System.out.println("Route configured.");
			}
		});
			

		// start the route and let it do its work
		context.start();
		Thread.sleep(10000);

		// stop the CamelContext
		context.stop();

	}
}
