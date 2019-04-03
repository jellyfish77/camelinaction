# Enrich Examples

I added this project to demo some of the examples in the book.

![](http://www.enterpriseintegrationpatterns.com/img/DataEnricher.gif) 

## EnrichRouter Class ##

class: com.github.jellyfish77.EnrichRouter

This class is my implementation of an enricher from the book. It will collect text from 2 files and concatenate them with a \n between (it first processes file1 text with csv processor). Gives:

	000000444,20091208,000001212,1217,1478,2132
	some extra crap
	
Run:
		
	mvn clean compile exec:java -Dexec.mainClass=com.github.jellyfish77.EnrichRouter
