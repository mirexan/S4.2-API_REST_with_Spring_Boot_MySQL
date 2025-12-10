package cat.itacademy.s04.t02.n02.exceptions;

public class FruitNotFoundException extends RuntimeException {
	public FruitNotFoundException(String message) {
		super(message);
	}
}
