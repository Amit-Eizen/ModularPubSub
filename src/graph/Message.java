package graph;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    public Message(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException("msg cannot be null");
        }

        double asDoubleTemp;
        this.data = msg.getBytes(StandardCharsets.UTF_8);
        this.asText = msg;
        try
        {
            asDoubleTemp =Double.parseDouble(msg);
        }
        catch (NumberFormatException e)
        {
            asDoubleTemp = Double.NaN;
        }
        this.asDouble = asDoubleTemp;
        this.date = new Date();
    }
    public Message(byte[] msg)
    {
        this((new String(msg, StandardCharsets.UTF_8)));
    }
    public Message(double asMsg)
    {
        this(String.valueOf(asMsg));
    }
}
