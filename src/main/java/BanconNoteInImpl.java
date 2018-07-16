import java.util.Random;
import java.util.Scanner;

public class BanconNoteInImpl implements IBanconNoteIn {
    @Override
    public int getMoney() {
            // Эмуляция вставки  банкнот
        return new Scanner( System.in ).nextInt();

    }
}
