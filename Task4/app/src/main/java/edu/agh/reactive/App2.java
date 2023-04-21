package edu.agh.reactive;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.nio.file.Paths;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class App2 {
    public static Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class)
                .map(s -> ByteString.fromString(s.toString() + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        final CompletionStage<Done> done = source.runForeach(i -> System.out.println(i), system);




        final Source<BigInteger, NotUsed> factorials =
                source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

        factorials
                .zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.ofSeconds(1))
                .runForeach(s -> System.out.println(s), system);
    }
}
