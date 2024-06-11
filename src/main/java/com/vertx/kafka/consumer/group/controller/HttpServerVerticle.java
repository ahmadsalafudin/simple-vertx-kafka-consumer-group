package com.vertx.kafka.consumer.group.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class HttpServerVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/").handler(ctx -> {
      ctx.response().putHeader("content-type", "text/plain").end("Hello from Vert.x HTTP Server!");
    });

    vertx.createHttpServer().requestHandler(router).listen(8082, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8082");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
