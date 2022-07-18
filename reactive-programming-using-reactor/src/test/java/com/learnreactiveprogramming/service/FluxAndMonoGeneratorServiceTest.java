package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
     FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben")
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        //given
        int stringLength = 3;

        //when
        var namesFluxMap = fluxAndMonoGeneratorService.namesFluxMap(stringLength);

        //then
        StepVerifier.create(namesFluxMap)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutable() {
        //given


        //when
        var namesFluxMap = fluxAndMonoGeneratorService.namesFluxImmutable();

        //then
        StepVerifier.create(namesFluxMap)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        //given
        int stringLength = 3;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxFlatMap(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        //given
        int stringLength = 3;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();

    }

    @Test
    void namesFluxConcatMap() {
        //given
        int stringLength = 3;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxConcatMap(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        int stringLength = 3;

        var namesMonoFlatMap = fluxAndMonoGeneratorService.namesMonoFlatMap(stringLength);

        StepVerifier.create(namesMonoFlatMap)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();

    }

    @Test
    void namesMonoFlatMapMany() {
        int stringLength = 3;

        var namesMonoFlatMap = fluxAndMonoGeneratorService.namesMonoFlatMapMany(stringLength);

        StepVerifier.create(namesMonoFlatMap)
                .expectNext("A","L","E","X")
                .verifyComplete();

    }

    @Test
    void namesFluxTransform_1() {
        //given
        int stringLength = 3;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxTransform(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform_2() {
        //given
        int stringLength = 6;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxTransform(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform_switchIfEmpty() {
        //given
        int stringLength = 6;

        //when
        var namesFluxFlatMap = fluxAndMonoGeneratorService.namesFluxTransform_switchIfEmpty(stringLength);

        //then
        StepVerifier.create(namesFluxFlatMap)
                .expectNext("D", "E", "F", "A", "U", "L", "T")

                .verifyComplete();
    }

    @Test
    void exploreConcat() {

        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreConcat();

        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWithFlux() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreConcatWithFlux();

        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWithMono() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreConcatWithMono();

        StepVerifier.create(concatFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreMerge();

        StepVerifier.create(concatFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWithFlux() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreMerge();

        StepVerifier.create(concatFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void exploreMergeWithMono() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreConcatWithMono();

        StepVerifier.create(concatFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void exploreMergeSequential() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreMergeSequential();

        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreZip();

        StepVerifier.create(concatFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZip2() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreZip2();

        StepVerifier.create(concatFlux)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void exploreZipWith() {
        Flux<String> concatFlux = fluxAndMonoGeneratorService.exploreZipWith();

        StepVerifier.create(concatFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void exploreZipWithMono() {
        Mono<String> concatFlux = fluxAndMonoGeneratorService.exploreZipWithMono();

        StepVerifier.create(concatFlux)
                .expectNext("AB")
                .verifyComplete();
    }
}