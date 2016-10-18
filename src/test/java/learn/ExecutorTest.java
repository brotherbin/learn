package learn;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ExecutorTest {

	@Test
	public void testCreateExecutor() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Callable<String> callable = new Callable<String>() {
			public String call() throws Exception {
				return "hello executor service";
			}
		};
		Future<String> future = executorService.submit(callable);
		Assert.assertEquals("hello executor service", future.get());
	}
}
