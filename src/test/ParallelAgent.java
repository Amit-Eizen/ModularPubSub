package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements  Agent{
    private Agent inner;

    public ParallelAgent(Agent agent) {
        this.inner = agent;
    }
    @Override
    public String getName() { return this.inner.getName(); }

    @Override
    public void reset() { this.inner.reset(); }

    @Override
    public void callback(String topic, Message msg) {
        this.inner.callback(topic, msg);
    }

    @Override
    public void close() { this.inner.close(); }
}
