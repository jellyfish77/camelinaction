package camelinaction.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import otto.utils.DownloadLogger;
import otto.utils.MessageLogger;

@Component
public class FtpToJMSRoute extends SpringRouteBuilder {
	public void configure() {
		from("ftp://localhost/transfer/input?username=otto&password=otto&move=.done").
		process(new DownloadLogger()).
		process(new MessageLogger()).
        to("log:like-to-see-all?level=INFO&showAll=true&multiline=true").
		to("jms:incomingOrders");
	}
}