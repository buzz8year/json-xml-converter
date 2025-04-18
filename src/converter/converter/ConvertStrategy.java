package converter.converter;

public interface ConvertStrategy
{
    void convert();
    void setPayload(String payload);
    String getResult();
}
