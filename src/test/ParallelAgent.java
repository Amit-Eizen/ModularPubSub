package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ParallelAgent implements  Agent{
    private final Agent inner;
    private final ExecutorService executor;

    public ParallelAgent(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("Agent cannot be null");
        }
        this.inner = agent;
        this.executor = Executors.newSingleThreadExecutor();
    }
    @Override
    public String getName() { return this.inner.getName(); }

    @Override
    public void reset() { executor.submit(this.inner::reset);}

    @Override
    public void callback(String topic, Message msg) {
        executor.submit(() -> this.inner.callback(topic, msg));
    }

    @Override
    public void close() {
        executor.shutdown();
        this.inner.close();
    }
}
