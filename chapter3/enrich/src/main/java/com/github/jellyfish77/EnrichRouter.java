package com.github.jellyfish77;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import annotations.Author;
import camelinaction.OrderToCsvProcessor;
import loggers.DownloadLogger;
import loggers.MessageLogger;


@Author(name = "jellyfish77")
public class EnrichRouter {

	public static void main(String args[]) throws Exception {
		// create CamelContext
		CamelContext context = new DefaultCamelContext();

		// add our route to the CamelContext
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				System.out.println("Configuring route...");			
				from("file:src/data?fileName=file1&noop=true")
				.process(new OrderToCsvProcessor())				
				.pollEnrich("file:src/data?fileName=file2&noop=true",
					new AggregationStrategy() {
						public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
							if (newExchange == null) {
								return oldExchange;
							}
							String http = oldExchange.getIn().getBody(String.class);
							String ftp = newExchange.getIn().getBody(String.class);
							String body = http + "\n" + ftp;
							oldExchange.getIn().setBody(body);
							return oldExchange;
						}
					}
				)
				.to("file://src/data?fileName=report-${header.Date}.csv")
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
