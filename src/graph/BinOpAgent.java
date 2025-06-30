package graph;

import java.util.function.BinaryOperator;

public class BinOpAgent implements Agent
{
    public final String agentName;
    public final String topicNameOne;
    public final String topicNameTwo;
    public final String topicNameRes;
    public final BinaryOperator<Double> op;
    private Message msg1 = null, msg2 = null;

    public BinOpAgent(String agent, String firstTopic, String secTopic, String resTopic, BinaryOperator<Double> op) {
        this.agentName = agent;
        this.topicNameOne = firstTopic;
        this.topicNameTwo = secTopic;
        this.topicNameRes = resTopic;
        this.op = op;

        TopicManagerSingleton.get().getTopic(firstTopic).subscribe(this);
        TopicManagerSingleton.get().getTopic(secTopic).subscribe(this);
        TopicManagerSingleton.get().getTopic(resTopic).addPublisher(this);
    }
    @Override
    public String getName() { return agentName; }
    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(topicNameOne)) msg1 = msg;
        else if (topic.equals(topicNameTwo)) msg2 = msg;

        if (msg1 != null && msg2 != null) {
            double result = op.apply(msg1.asDouble, msg2.asDouble);
            Message resultMsg = new Message(result);
            TopicManagerSingleton.get().getTopic(topicNameRes).publish(resultMsg);

            msg1 = null;
            msg2 = null;
        }
    }
    @Override
    public void reset() { msg1 = null; msg2 = null; }
    @Override
    public void close() {}
}
