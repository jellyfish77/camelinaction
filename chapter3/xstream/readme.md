# PurchaseOrderXstream

1. Marshal a PurchaseOrderObject to XML & send to jms queue
2. Receive XML from queue and unmashall XML and convert to object

==Can't get the conversion to object working.==

Run:
		
	mvn clean compile exec:java -Dexec.mainClass=camelinaction.PurchaseOrderXstream