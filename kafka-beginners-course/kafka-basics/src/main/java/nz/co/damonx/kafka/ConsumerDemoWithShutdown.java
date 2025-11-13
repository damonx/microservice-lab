package nz.co.damonx.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * Consumer with graceful shutdown.
 */
public class ConsumerDemoWithShutdown
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());
    public static void main(String[] args)
    {
        LOGGER.info("Starting consumer demo with grace shutdown.");

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

        // get a reference to the main thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Detected a shutdown, let us exit by calling consumer.wakeup()...");
            consumer.wakeup();

            // join the main thread to allow the execution of the code in main thread.
            try {
                mainThread.join();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            // subscribe a topic
            consumer.subscribe(List.of(topic));

            // poll for data
            while (true) {
                LOGGER.info("Polling records from topic: {}", topic);
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                if (!records.isEmpty()) {
                    LOGGER.info("Consuming records from topic: {}", topic);
                }
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("Key: {}, Value: {}", record.key(), record.value());
                    LOGGER.info("Partition: {}, Offset: {}", record.partition(), record.offset());
                }
            }
        } catch (final WakeupException we) {
            LOGGER.info("Consumer is starting to shut down.");
        } catch (final Exception e) {
            LOGGER.error("Unexpected exception in the consumer", e);
        } finally {
            consumer.close();
            LOGGER.info("The consumer is now gracefully shut down.");
        }
    }
}
