package old_do_more_dojo.core.amd.naming;

public class NameException
{
    private String literal;
    private String parameter;

    public NameException(String literal, String parameter) {
        this.literal = literal;
        this.parameter = parameter;
    }

    public String getLiteral() {
        return literal;
    }

    public String getParameter() {
        return parameter;
    }
}
