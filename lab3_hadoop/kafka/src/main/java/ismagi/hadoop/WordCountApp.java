package ismagi.hadoop;

import java.util.Properties;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class WordCountApp {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "word-count-group");
    props.put("enable.auto.commit", "true");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList("WordCount-Topic"));

    Map<String, Integer> wordCount = new HashMap<>();
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      records.forEach(record -> {
        String word = record.key();
        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        System.out.println("Mot: " + word + " - Compte: " + wordCount.get(word));
      });
    }
  }
}
