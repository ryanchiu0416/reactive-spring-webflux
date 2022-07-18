package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    // publisher
    public Flux<String> namesFlux() {
        // IRL, db or a remote service call
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Mono<String> namesMono() {
        return Mono.just("alex");
    }

    public Flux<String> namesFluxMap(int stringLength) {
        // filter the string with length > 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(name -> name.length() + "-" + name)
                .log();
    }

    public Flux<String> namesFluxImmutable() {
        // IRL, db or a remote service call
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> namesFluxFlatMap(int stringLength) {
        // filter the string with length > 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFluxFlatMapAsync(int stringLength) {
        // filter the string with length > 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    public Flux<String> namesFluxConcatMap(int stringLength) {
        // filter the string with length > 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    // ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name) {
        String[] charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitStringWithDelay(String name) {
        int delay = new Random().nextInt(1000);
        String[] charArray = name.split("");
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }


    public Mono<List<String>> namesMonoFlatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] charArray = s.split("");
        return Mono.just(List.of(charArray));
    }


    public Flux<String> namesMonoFlatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }



    public Flux<String> namesFluxTransform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFluxTransform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString);

        Flux<String> defaultFlux = Flux.just("default").transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }


    // concat & concatWith
    public Flux<String> exploreConcat() {
        Flux<String> abcFlux = Flux.just("A","B","C");
        Flux<String> defFlux = Flux.just("D","E","F");

        return Flux.concat(abcFlux, defFlux)
                .log();
    }


    public Flux<String> exploreConcatWithFlux() {
        Flux<String> abcFlux = Flux.just("A","B","C");
        Flux<String> defFlux = Flux.just("D","E","F");

        return abcFlux.concatWith(defFlux)
                .log();
    }

    public Flux<String> exploreConcatWithMono() {
        Mono<String> aMono = Mono.just("A");
        Mono<String> bMono = Mono.just("B");

        return aMono.concatWith(bMono)
                .log();
    }

    // merge & mergeWith
    public Flux<String> exploreMerge() {
        Flux<String> abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux)
                .log();
    }


    public Flux<String> exploreMergeWithFlux() {
        Flux<String> abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux)
                .log();
    }

    public Flux<String> exploreMergeWithMono() {
        Mono<String> aMono = Mono.just("A");
        Mono<String> bMono = Mono.just("B");

        return aMono.mergeWith(bMono)
                .log();
    }


    public Flux<String> exploreMergeSequential() {
        Flux<String> abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux)
                .log();
    }

    public Flux<String> exploreZip() {
        Flux<String> abcFlux = Flux.just("A","B","C");
        Flux<String> defFlux = Flux.just("D","E","F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second)
                .log();
    }

    public Flux<String> exploreZip2() {
        Flux<String> abcFlux = Flux.just("A","B","C");
        Flux<String> defFlux = Flux.just("D","E","F");
        Flux<String> _123Flux = Flux.just("1","2","3");
        Flux<String> _456Flux = Flux.just("4","5","6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t -> t.getT1() + t.getT2() + t.getT3() + t.getT4())
                .log();
    }


    public Flux<String> exploreZipWith() {
        Flux<String> abcFlux = Flux.just("A","B","C");
        Flux<String> defFlux = Flux.just("D","E","F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
    }



    public Mono<String> exploreZipWithMono() {
        Mono<String> aMono = Mono.just("A");
        Mono<String> bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t -> t.getT1() + t.getT2())
                .log();
    }

    // subscriber
    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is : " + name);
                });

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> {
                    System.out.println("Mono Name is : " + name);
                });
    }
}