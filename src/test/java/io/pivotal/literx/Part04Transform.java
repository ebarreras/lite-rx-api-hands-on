package io.pivotal.literx;

import org.junit.Test;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Learn how to transform values.
 *
 * @author Sebastien Deleuze
 */
public class Part04Transform {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

	@Test
	public void transformMono() {
		Mono<User> mono = repository.findFirst();
		StepVerifier.create(capitalizeOne(mono))
				.expectNext(new User("SWHITE", "SKYLER", "WHITE"))
				.verifyComplete();
	}

	// TODO Capitalize the user username, firstname and lastname
	Mono<User> capitalizeOne(Mono<User> mono) {
		return mono.map(u -> new User(u.getUsername().toUpperCase(), u.getFirstname().toUpperCase(), u.getLastname().toUpperCase()));
	}

//========================================================================================

	@Test
	public void transformFlux() {
		Flux<User> flux = repository.findAll();
		StepVerifier.create(capitalizeMany(flux))
				.expectNext(
					new User("SWHITE", "SKYLER", "WHITE"),
					new User("JPINKMAN", "JESSE", "PINKMAN"),
					new User("WWHITE", "WALTER", "WHITE"),
					new User("SGOODMAN", "SAUL", "GOODMAN"))
				.verifyComplete();
	}

	// TODO Capitalize the users username, firstName and lastName
	Flux<User> capitalizeMany(Flux<User> flux) {
		return flux.map(u -> new User(u.getUsername().toUpperCase(), u.getFirstname().toUpperCase(), u.getLastname().toUpperCase()));
	}

//========================================================================================

	@Test
	public void  asyncTransformFlux() {
		Flux<User> flux = repository.findAll();
		StepVerifier.create(asyncCapitalizeMany(flux))
				.expectNext(
					new User("SWHITE", "SKYLER", "WHITE"),
					new User("JPINKMAN", "JESSE", "PINKMAN"),
					new User("WWHITE", "WALTER", "WHITE"),
					new User("SGOODMAN", "SAUL", "GOODMAN"))
				.verifyComplete();
	}

	// TODO Capitalize the users username, firstName and lastName using asyncCapitalizeUser()
	Flux<User> asyncCapitalizeMany(Flux<User> flux) {
		return flux.flatMap(u -> asyncCapitalizeUser(u));
	}

	Mono<User> asyncCapitalizeUser(User u) {
		return Mono.just(new User(u.getUsername().toUpperCase(), u.getFirstname().toUpperCase(), u.getLastname().toUpperCase()));
	}

}
