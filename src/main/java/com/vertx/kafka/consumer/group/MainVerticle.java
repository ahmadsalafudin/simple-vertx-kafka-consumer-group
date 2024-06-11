package com.vertx.kafka.consumer.group;

import com.vertx.kafka.consumer.group.config.KafkaConsumerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class MainVerticle {

  public static void main(String[] args) {
    VertxOptions options = new VertxOptions();
    Vertx vertx = Vertx.vertx(options);

    vertx.deployVerticle(new KafkaConsumerVerticle("Consumer1"), res -> {
      if (res.succeeded()) {
        System.out.println("Consumer1 verticle deployed, deployment id is: " + res.result());
      } else {
        System.out.println("Consumer1 verticle deployment failed!");
      }
    });

    vertx.deployVerticle(new KafkaConsumerVerticle("Consumer2"), res -> {
      if (res.succeeded()) {
        System.out.println("Consumer2 verticle deployed, deployment id is: " + res.result());
      } else {
        System.out.println("Consumer2 verticle deployment failed!");
      }
    });
  }
}
