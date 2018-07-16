import java.io.IOException;

public class TerminalServer implements ITerminal {
    public int checkAccountBalance() throws   IOException {
        return (int )(Math.random()*10000 );
    }

    public void putMoney( int money ) throws IOException {

    }

    public boolean getMoney( int money )  {
        return true;
    }

}
