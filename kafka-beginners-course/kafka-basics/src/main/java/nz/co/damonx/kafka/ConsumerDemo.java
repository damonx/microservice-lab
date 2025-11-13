package nz.co.damonx.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerDemo
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());
    public static void main(String[] args)
    {
        LOGGER.info("Starting consumer demo");

        final String groupId = "my-java-application";
        final String topic = "demo-java";

        // create producer properties
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // set producer properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        // create a consumer
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // subscribe a topic
        consumer.subscribe(List.of(topic));

        // poll for data
        while (true) {
            //LOGGER.info("Consuming records from topic: " + topic);
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            if (!records.isEmpty()) {
                LOGGER.info("Consuming records from topic: {}", topic);
            }
            for (ConsumerRecord<String, String> record : records) {
                LOGGER.info("Key: {}, Value: {}", record.key(), record.value());
                LOGGER.info("Partition: {}, Offset: {}", record.partition(), record.offset());
            }
        }

    }
}
