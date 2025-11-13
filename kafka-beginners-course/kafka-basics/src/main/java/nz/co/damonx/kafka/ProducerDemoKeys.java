package nz.co.damonx.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ProducerDemoKeys
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());
    public static void main(String[] args)
    {
        LOGGER.info("Starting producer with keys demo");

        // create producer properties
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("partitioner.class", "org.apache.kafka.clients.producer.RoundRobinPartitioner");

        // create the Producer
        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        final String topic = "demo-java";

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                final String key = "id_" + i;
                final String value= "hello world from Damonx " + i;

                // create a Producer Record
                final ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic, key, value);

                // send data
                producer.send(producerRecord, (metadata, e) -> {
                    if (e == null) {
                        LOGGER.info("Key: {}, Value: {} | Partition: {}", key, value, metadata.partition());
//                        LOGGER.info("Topic: {}", metadata.topic());
//                        LOGGER.info("Offset: {}", metadata.offset());
//                        LOGGER.info("Timestamp: {}", metadata.timestamp());
                    } else {
                        LOGGER.error("Error while producing", e);
                    }
                });
            }
            try {
                Thread.sleep(Duration.ofMillis(500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        // tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // flush and close the producer
        producer.close();
    }
}
