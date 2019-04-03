# Chapter 3 - Transformation ##

## Unit Tests ##
### OrderToCsvProcessor ###

A processor which translates an order in custom inhouse format to a CSV format.

Unit test for OrderToCsvProcessor using Java DSL:

	mvn test -Dtest.mainClass=camelinaction.OrderToCsvProcessorTest

Unit test for OrderToCsvProcessor using Spring DSL:
	
	mvn test -Dtest.mainClass=camelinaction.SpringOrderToCsvProcessorTest

## Main Classes ##

### OrderToCsvRouter ###

OQ - Read from FTP on a schedule (using quartz2 component) and process file using Java DSL 'OrderToCsvProcessor'.

Chron:
![](http://2.bp.blogspot.com/--d9V7XzD9aU/UgzRLNXIgSI/AAAAAAAAAcM/cIzUHV665v0/s320/cron.png) 

Execute with:

	mvn clean compile exec:java -Dexec.mainClass=camelinaction.OrderToCsvRouter

![](https://blog.christianposta.com/images/apache-camel-logo.jpg) 

