package graph;

public class PlusAgent implements Agent {

    private String name;
    private Topic m_sub1, m_sub2 ,m_pub;
    private double m_x=0, m_y=0;
    private boolean receivedX = false, receivedY = false;
    private String[] subs;
    private String[] pubs;

    public PlusAgent(String[] subs, String[] pubs)
    {
        this.name = "PlusAgent";
        this.subs = subs;
        this.pubs = pubs;

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        if (subs.length >= 2) {
            this.m_sub1 = tm.getTopic(subs[0]);
            this.m_sub2 = tm.getTopic(subs[1]);
            m_sub1.subscribe(this);
            m_sub2.subscribe(this);
        }

        if (pubs.length >= 1) {
            this.m_pub = tm.getTopic(pubs[0]);
            m_pub.addPublisher(this);
        }
    }
    public double getX() {return this.m_x;}
    public double getY() {return this.m_y;}
    public void setX(double x) {this.m_x = x;}
    public void setY(double y) {this.m_y = y;}

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void reset() {
        setX(0);
        setY(0);
        receivedX = receivedY = false;
    }
    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            if (m_sub1.name.equals(topic)) {
                m_x = msg.asDouble;
                receivedX = true;
            } else if (m_sub2.name.equals(topic)) {
                m_y = msg.asDouble;
                receivedY = true;
            }
            if (receivedX && receivedY) {
                double sum = m_x + m_y;
                m_pub.publish(new Message(sum));
                receivedX = receivedY = false;
            }
        }
    }
    @Override
    public void close() {
        if (m_sub1 != null) m_sub1.unsubscribe(this);
        if (m_sub2 != null) m_sub2.unsubscribe(this);
        if (m_pub != null) m_pub.removePublisher(this);
    }
}
