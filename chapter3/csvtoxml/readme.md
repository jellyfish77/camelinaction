# CSVToXML

## CSVToXMLTransformation (Java DSL)
	
Uses Java DSL routes together with a Java Processor (implements Processor interface) to transform CSV to XML.
	
	mvn clean compile exec:java -Dexec.mainClass=csvtoxml.javadsl.CSVToXMLTransformation
	
## Spring DSL Implementation

Identical functionality to the Java DSL version above, done using custom XML extensions (Spring DSL).

	mvn clean compile camel:run
	
## Test Data

Uses "IMDB 5000 Movie Datase" dataset from https://data.world

Fields:

"movie_title"   
"color"   
"num_critic_for_reviews"  
"movie_facebook_likes"   
"duration"   
"director_name"   
"director_facebook_likes"   
"actor_3_name"   
"actor_3_facebook_likes"   
"actor_2_name"   
"actor_2_facebook_likes"   
"actor_1_name"   
"actor_1_facebook_likes"   
"gross"   
"genres"   
"num_voted_users"   
"cast_total_facebook_likes"   
"facenumber_in_poster"   
"plot_keywords"   
"movie_imdb_link"   
"num_user_for_reviews"   
"language"   
"country"   
"content_rating"   
"budget"   
"title_year"   
"imdb_score"   
"aspect_ratio"  
	
Todo:

- Implement using XSLT
- Implement using Spring Bean instead of Processor
- Implement using Spring DSL
- Implement test classes