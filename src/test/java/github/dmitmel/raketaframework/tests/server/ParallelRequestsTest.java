package github.dmitmel.raketaframework.tests.server;

import github.dmitmel.raketaframework.tests.TestApplication;
import github.dmitmel.raketaframework.web.HTTPResponse;
import org.junit.Test;

import java.util.ArrayList;

public class ParallelRequestsTest extends ServerTestSuite {
    public static void runParallelThreadsWithTask(Runnable task, int threadCount) {
        try {
            ArrayList<Thread> threads = new ArrayList<>(10);
    
            for (int i = 0; i < threadCount; i++) {
                Thread thread = new Thread(task);
        
                thread.setName("parallelThread-" + i);
                thread.setDaemon(true);
        
                threads.add(thread);
            }
    
            threads.forEach(Thread::start);
            if (threadCount > 0)
                threads.get(threadCount - 1).join();
        } catch (java.lang.InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test(timeout = TestApplication.LONG_TASK_TIMEOUT + 1100)
    public void testParallelLongTask() {
        runParallelThreadsWithTask(() -> {
            HTTPResponse response = ServerTestSuite.doRequest("GET", TestApplication.URL + "/long-task");
            assertEquals("Success!", response.bodyToString());
        }, 10);
    }
}
