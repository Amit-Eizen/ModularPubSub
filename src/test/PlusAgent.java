package test;

import java.util.List;

public class PlusAgent implements Agent {

    private Topic m_sub1, m_sub2 ,m_pub;
    private double m_x=0, m_y=0;
    public PlusAgent(List<Topic> subs,List<Topic> pubs)
    {
        m_sub1 = subs.get(0);
        m_sub2 = subs.get(1);
        m_pub = pubs.get(0);
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
    }
    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(m_sub1.name)) {
            m_x = msg.asDouble;
        }
        else if (topic.equals(m_sub2.name)) {
            m_y = msg.asDouble;
        }
        if (m_x != 0 && m_y != 0) {
            double sum = m_x + m_y;
            m_pub.publish(new Message(sum));
        }
    }
    @Override
    public void close() {}
}
