package learn;

public class Tester {

	public static void main(String[] args) {
		String os = Tester.class.getClassLoader().getResource("").getPath();
		System.out.println(os);
	}
}
