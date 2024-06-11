package com.vertx.kafka.consumer.group.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerVerticle extends AbstractVerticle {

  private final String verticleName;

  public KafkaConsumerVerticle(String verticleName) {
    this.verticleName = verticleName;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "my_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

    KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

    consumer.handler(record -> {
      System.out.printf("[%s] Processing key=%s, value=%s, partition=%d, offset=%d%n", verticleName, record.key(), record.value(), record.partition(), record.offset());
      consumer.commit(ar -> {
        if (ar.succeeded()) {
          System.out.printf("[%s] Offset committed%n", verticleName);
        } else {
          System.err.printf("[%s] Offset commit failed: %s%n", verticleName, ar.cause().getMessage());
        }
      });
    });

    consumer.subscribe("vertx-kafka-topic", ar -> {
      if (ar.succeeded()) {
        System.out.printf("[%s] Subscribed to topic%n", verticleName);
        startPromise.complete();
      } else {
        System.err.printf("[%s] Could not subscribe: %s%n", verticleName, ar.cause().getMessage());
        startPromise.fail(ar.cause());
      }
    });
  }
}
