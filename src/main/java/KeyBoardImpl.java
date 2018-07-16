import java.util.Scanner;

public class KeyBoardImpl implements IKeyBoard {
    @Override
    public int get() {
        Scanner cardReader =  new Scanner(System.in);
        return cardReader.nextInt();
    }
}
