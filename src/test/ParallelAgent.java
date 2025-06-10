package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ParallelAgent implements  Agent{
    private Agent inner;

    public ParallelAgent(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("Agent cannot be null");
        }
        this.inner = agent;
    }
    @Override
    public String getName() { return this.inner.getName(); }

    @Override
    public void reset() { this.inner.reset();}

    @Override
    public void callback(String topic, Message msg) {
        this.inner.callback(topic, msg);
    }

    @Override
    public void close() {
        this.inner.close();
    }
}
