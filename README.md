# Ecommerce Spring Boot (Lucene + Kafka)

Project demo that:
- exposes `/api/search?q=...` to search products using Apache Lucene
- on every search, publishes a `SearchEvent` to Kafka topic `product-search`

How to run:
1. Start Kafka locally (bootstrap at localhost:9092) or change application.yml and KafkaProducerConfig.
2. Build: `mvn -U -DskipTests clean package`
3. Run: `java -jar target/ecommerce-lucene-kafka-0.0.1-SNAPSHOT.jar`
4. Example: `GET http://localhost:8080/api/search?q=iphone`

Notes:
- This is a simple demo for learning purposes. In production you'd want:
  - better error handling
  - asynchronous indexing
  - resilient Kafka configuration and retries
  - secure configuration
  - better Lucene management (re-use IndexWriter, optimize, locking)
