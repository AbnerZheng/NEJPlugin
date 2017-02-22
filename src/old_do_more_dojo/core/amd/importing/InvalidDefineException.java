package old_do_more_dojo.core.amd.importing;

public class InvalidDefineException extends Exception
{
    public InvalidDefineException() {
        super("The import block was incomplete or invalid");
    }
}
