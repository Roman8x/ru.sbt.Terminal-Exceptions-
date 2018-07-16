import java.util.Scanner;

public class CardReaderImpl implements ICardReader {
    @Override
    public long getCardNumber() {
        Scanner cardReader =  new Scanner(System.in);
            return cardReader.nextLong();

    }

    @Override
    public void blockCard() {
        try {
            Thread.sleep( 5000 );
        }
        catch ( Exception ignore ){

        }
    }
}
