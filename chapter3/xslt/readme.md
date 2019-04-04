# XSLT Project

## TransformFileXslt Class

This class will:

1. Poll for file 'message.xml' at src/data/input
2. Transform message with XSLT
3. Write transformed message to src/data/output

WireTaps are use to log msg audit trail to MySQL.

Run:
	
	mvn clean compile exec:java -Dexec.mainClass=camelinaction.TransformFileXslt
