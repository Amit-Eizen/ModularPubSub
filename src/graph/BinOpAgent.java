package graph;

import java.util.function.BinaryOperator;

public class BinOpAgent
{
    public final String agentName;
    public final String topicNameOne;
    public final String topicNameTwo;
    public final String topicNameRes;
    public final BinaryOperator<Double> op;

    public BinOpAgent(String agent, String firstTopic, String secTopic, String resTopic, BinaryOperator<Double> op) {
        this.agentName = agent;
        this.topicNameOne = firstTopic;
        this.topicNameTwo = secTopic;
        this.topicNameRes = resTopic;
        this.op = op;
    }
}
