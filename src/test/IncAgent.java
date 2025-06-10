package test;

import java.util.List;

public class IncAgent implements Agent {
    private String name;
    private String[] m_sub1;
    private String[] m_pub;
    private Topic inputTopic;
    private Topic outputTopic;
    public IncAgent(String[] subs, String[] pubs) {
        this.name = "IncAgent";
        m_sub1 = subs;
        m_pub = pubs;

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        if (subs.length >= 1) {
            this.inputTopic = tm.getTopic(m_sub1[0]);
            inputTopic.subscribe(this);
        }

        if (pubs.length >= 1) {
            this.outputTopic = tm.getTopic(m_pub[0]);
            outputTopic.addPublisher(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void reset() { }
    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            double result = msg.asDouble + 1.0;
            Message resultMessage = new Message(result);
            if (outputTopic != null) {
                outputTopic.publish(resultMessage);
            }
        }
    }
    @Override
    public void close() {
        if (inputTopic != null) {
            inputTopic.unsubscribe(this);
        }
        if (outputTopic != null) {
            outputTopic.removePublisher(this);
        }
    }
}
