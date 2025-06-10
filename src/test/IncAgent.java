package test;

import java.util.List;

public class IncAgent implements Agent {
    private final Topic m_sub1;
    private final Topic m_pub;
    private double increment;
    public IncAgent(List<Topic> subs, List<Topic> pubs) {
        if (subs == null || subs.isEmpty() || pubs == null || pubs.isEmpty()) {
            throw new IllegalArgumentException("Lists cannot be null or empty");
        }

        m_sub1 = subs.get(0);
        m_pub = pubs.get(0);
        m_sub1.subscribe(this);
    }
    public void setIncrement(double x) {increment = x;}
    @Override
    public String getName() {
        return "IncAgent";
    }
    @Override
    public void reset() {  increment = 0; }
    @Override
    public void callback(String topic, Message msg) {
        if (topic != null && topic.equals(m_sub1.name)) {
            if (!Double.isNaN(msg.asDouble)) {
                this.setIncrement(msg.asDouble + 1);
                m_pub.publish(new Message(increment));
            }
        }
    }
    @Override
    public void close() {
        m_sub1.unsubscribe(this);
        m_pub.removePublisher(this);
    }
}
