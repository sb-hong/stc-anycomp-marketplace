Tested OK with the following:
Java version: 17.0.6
Spring Boot version: 3.4.5
MYSQL version: 5.7.44
Maven version: 3.9.9

mvn clean install -DskipTests

java -Xms512M -Xmx1200M -jar "C:/Users/Msi User/Documents/anycompmarketplace/target/anycompmarketplace-0.0.1-SNAPSHOT.jar"  >>anycomp_marketplace_api_logs.txt


In the postman
append this url http://localhost:8092/anycompmarketplace/v1/... for every tab request
only use "Params" tab for key and value input


listing api's input param key "sort_by" shud be [{"colname":"email","descending":true}]. copy this string to https://www.urlencoder.org/ and paste it to get encoded string and put it into value in "Params" tab