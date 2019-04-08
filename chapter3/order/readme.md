# Chapter 3 - Data Transformation

## JAXB XML Binding using Spring DSL (order-jaxb.xml)

Camel In Action p.77

1. Bind incoming POJO to XML using JAXB
2. Log XML with MessageLogger()
3. Unbind from XML back to POJO 

Invoke Spring Test:

	mvn test -Dtest=PurchaseOrderJaxbTest 

## Apache Commons CSV Format using Spring DSL (order-csv.xml)

Camel in Action p. 79

Invoke Spring Test:

	mvn test -Dtest=PurchaseOrderCsvSpringTest
		
## Apache Commons CSV Format using Java DSL
	
	mvn test -Dtest=PurchaseOrderCsvTest

## Camel Bindy Data Format using Java DSL

Camel in Action p. 82

Marshal the order model to CSV using Bind

	mvn test -Dtest=PurchaseOrderBindyTest

## Camel Bindy Data Format using Java DSL

Camel in Action p. 80

A reverse example of how to use Bindy to transform a CSV record into a Java object.

	mvn test -Dtest=PurchaseOrderUnmarshalBindyTest
	
## Camel Jackson JSON Conversion using Java DSL

Camel in Action p. 83

Sets up a (REST) web service at /order/service listening for requests and responding with orderService bean marshaled to json with Jackson

	mvn test -Dtest=PurchaseOrderJSONTest


## Camel Custom Data Format 

Camel in Action p. 86

	mvn test -Dtest=ReverseDataFormatTest