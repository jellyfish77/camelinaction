package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import camelinaction.OrderToCsvProcessor;

import annotations.Author;

import loggers.MessageLogger;
import loggers.DownloadLogger;

/*
 * Router using quartz scheduler
 * 
 */
@Author(name = "OQ")
public class OrderToCsvRouter {

	public static void main(String args[]) throws Exception {
		// create CamelContext
		CamelContext context = new DefaultCamelContext();

		// connect to embedded ActiveMQ JMS broker
		//ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
		//context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		// add our route to the CamelContext
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				System.out.println("Configuring route...");
				//from("quartz://report?cron=0+0+6+*+*+?")
				//from("ftp://localhost/transfer/input?scheduler=quartz2&scheduler.cron=0/10+0+0+?+*+*+*&username=otto&password=otto&move=.done")
				from("ftp://localhost/transfer/input?scheduler=quartz2&scheduler.cron=*+*+*+?+*+*&username=otto&password=otto&move=.done")
				//.to("http://riders.com/orders/cmd=received&date=yesterday")
				.process(new OrderToCsvProcessor())
				.to("ftp://localhost/transfer/output?username=otto&password=otto&fileName=report-${header.Date}.csv")
				.process(new DownloadLogger())
				.process(new MessageLogger());
				System.out.println("Route configured.");
			}
		});

		// start the route and let it do its work
		context.start();
		Thread.sleep(15000);

		// stop the CamelContext
		context.stop();
	}

}

