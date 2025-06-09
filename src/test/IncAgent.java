package test;

import java.util.List;

public class IncAgent implements Agent {
    private Topic m_sub1, m_pub;
    private double increment;
    public IncAgent(List<Topic> subs, List<Topic> pubs) {
        m_sub1 = subs.get(0);
        m_pub = pubs.get(0);
    }
    public void setIncrement(double x) {increment = x;}
    @Override
    public String getName() {
        return "IncAgent";
    }
    @Override
    public void reset() {}
    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(m_sub1.name)) {
            if (!Double.isNaN(msg.asDouble))
            {
                this.setIncrement(msg.asDouble+1);
                m_pub.publish(new Message(increment));
            }
        }
    }
    @Override
    public void close() {}
}
