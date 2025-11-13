package nz.co.damonx.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());
    public static void main(String[] args)
    {
        LOGGER.info("Starting producer demo");

        // create producer properties
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the Producer
        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // create a Producer Record
        final ProducerRecord<String, String> producerRecord =
            new ProducerRecord<>("demo_java", "hello world, from damonx");

        // send data
        producer.send(producerRecord);

        // tell the producer to send all data and block until done -- synchronous
        producer.flush();

        // flush and close the producer
        producer.close();
    }
}
