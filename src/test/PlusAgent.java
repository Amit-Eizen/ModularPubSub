package test;

import java.util.List;
import java.util.Objects;

public class PlusAgent implements Agent {

    private Topic m_sub1, m_sub2 ,m_pub;
    private double m_x=0, m_y=0;
    private boolean receivedX = false, receivedY = false;

    public PlusAgent(List<Topic> subs,List<Topic> pubs)
    {
        Objects.requireNonNull(subs, "subs list cannot be null");
        Objects.requireNonNull(pubs, "pubs list cannot be null");

        if (subs.size() < 2 || pubs.isEmpty()) {
            throw new IllegalArgumentException("Need at least 2 subscribers and 1 publisher");
        }

        m_sub1 = Objects.requireNonNull(subs.get(0), "sub1 cannot be null");
        m_sub2 = Objects.requireNonNull(subs.get(1), "sub2 cannot be null");
        m_pub = Objects.requireNonNull(pubs.get(0), "pub cannot be null");

        m_sub1.subscribe(this);
        m_sub2.subscribe(this);
        m_pub.addPublisher(this);
    }
    public double getX() {return this.m_x;}
    public double getY() {return this.m_y;}
    public void setX(double x) {this.m_x = x;}
    public void setY(double y) {this.m_y = y;}

    @Override
    public String getName() {
        return "PlusAgent";
    }
    @Override
    public void reset() {
        setX(0);
        setY(0);
        receivedX = receivedY = false;
    }
    @Override
    public void callback(String topic, Message msg) {
        if (m_sub1.name.equals(topic)) {
            m_x = msg.asDouble;
            receivedX = true;
        }
        else if (m_sub2.name.equals(topic)) {
            m_y = msg.asDouble;
            receivedY = true;
        }
        if (receivedX && receivedY) {
            double sum = m_x + m_y;
            m_pub.publish(new Message(sum));
            receivedX = receivedY = false;
        }
    }
    @Override
    public void close() {
        if (m_sub1 != null) m_sub1.unsubscribe(this);
        if (m_sub2 != null) m_sub2.unsubscribe(this);
        if (m_pub != null) m_pub.removePublisher(this);
    }
}
