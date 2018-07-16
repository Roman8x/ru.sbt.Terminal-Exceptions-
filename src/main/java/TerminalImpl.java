import TerminalExceptions.NoBanknoteException;

import javax.security.auth.login.AccountLockedException;
import java.io.IOException;
import java.io.PrintStream;


public class TerminalImpl implements ITerminal  {

    private ITerminal terminalServer; // Интерфейс терминала для отправки \ получения сообещений с сервера
    private PrintStream userInterface ; // UI  Пользователеский интерфейс ( консоль в нашем случае)
    private ICardReader cardReader ;    // Считыватель карт
    private IKeyBoard keyBoard;         // Цифровая клавиатура
    private IBanconNoteIn banconNoteIn; // Приемник банкнот
    private IPinValidator pinValidator  ;// проверка пинкода карты

    final int MAX_attempt =  3 ;





    /**
     *
     * @param args
     *  В упрощенном виде считаем что терминал стартует сразу
     */
    public static void main( String[] args ) {
        TerminalImpl terminal= TerminalImpl.build();
        terminal.run();

    }
    /** Бесконечный цикл работы терминала
     *  Упрощенно: номер карты  + пинкод
     *    считать карту и пинкод, отправить на проверку,
     *             если проходит проверку, работаем с пользователем, типы операции:
     *             0. забрать карту
     *             1. показать баланс , Exceptions : IOError
     *             2. Положить деньги , Exceptions : IOError
     *             3. Снять деньги    , Exceptions : IOError , NoBancnote
     *
     *               Операция завершена успешно, заберите карту
     **/
    public void run (){
        while ( true ) {
            try {
                sendHello (); // отправляем  приглашение
                long cardNumber = cardReader.getCardNumber()  ; // ожидаем пока карта не будет вставлена
                workWithClient (cardNumber);
            }
            catch ( Exception ex ){

            }

        }
    }

    /**
     * Работаем с клиентом: авторизируемся , показываем пользовательское меню
     * @param cardNumber
     */
    private void workWithClient (long cardNumber ) {
        try{
            if (!(userValidate  (cardNumber) )){
                return;
            }

            int userСhoice;
            do {
                showUserMenu () ;
                userСhoice = keyBoard.get();
                switch (  userСhoice ) {
                    case 0: {
                        showGooBy () ;
                        break;}
                    case 1: {
                        checkAccountBalance () ;
                        break;}
                    case 2: {
                        userInterface.println( "Положите деньги в приемное устройство: " );
                        int money = banconNoteIn.getMoney();
                        putMoney ( money ) ;
                        break;}
                    case 3: {
                        getMoney ( 0 );
                        break;}
                    default: { break;}
                }
            } while (userСhoice !=0);
        }

        catch (  IOException ex ) {
            userInterface.println( "Потеря связи с сервером " );
        }
        /** Надуманное исключение, его можно избежать, проврив количество банкнот */
        catch (  NoBanknoteException  ex ) {
            userInterface.println( "" );
            userInterface.println( "Недостаточно денег в банкомате " );
            userInterface.println( "" );
        }
        catch ( Exception ex ){
            // непредвиденное исключение, отправим в лог на сервер

        }


    }

    /**
     * Экран в конце работы
     */
    private void showGooBy () {
        userInterface.println( "Спасибо, всего доброго !" );
        userInterface.println( "Заберите свою карту" );
        userInterface.println(  );
    }

    /**
     * Проверка пользователя
     * @param cardNumber // номер карты
     * @return
     */
    public boolean userValidate (long cardNumber ){
        for ( int i = 0; i < MAX_attempt ; i++ ) {
            userInterface.println( "ведите пин код" );
            int pincode = keyBoard.get();
            if (pinValidator.validate( cardNumber, pincode )){
                return true;
            };
        }
        userInterface.println( "Ваша карта будет заблокировна на 5 секунд" );
        cardReader.blockCard();
        return false;
    }


    /**
     *  Пользовательское меню
     */
    public void showUserMenu (){
        userInterface.println( "Сделайте выбор:" );
        userInterface.println( "0. забрать карту" );
        userInterface.println( "1. показать баланс" );
        userInterface.println( "2. Положить деньги" );
        userInterface.println( "3. Снять деньги" );
    }


    /**
     * Экран приветствия
     */
    private void sendHello () {
        userInterface.println("Вставьте карту в терминал");
        userInterface.println("for example:  ");
        userInterface.println(" 1234567890 ");
    }

    /**
     * Собираем терминал
     * @return
     */
    public static TerminalImpl build() {
        TerminalImpl result  = new TerminalImpl();
        result.userInterface =   new PrintStream (System.out) ;
        result.terminalServer=   new TerminalServer()  ;
        result.cardReader =  new CardReaderImpl();
        result.keyBoard =  new KeyBoardImpl();
        result.pinValidator = new PinValidatorImpl();
        result.banconNoteIn = new BanconNoteInImpl();
        return result;
    }

    /**
     * Проверка баланса
     * @return
     * @throws IOException
     */
    public int checkAccountBalance() throws IOException {
        int balance = terminalServer.checkAccountBalance();
        userInterface.println( "Ваш баланс составляет: "  + balance + " рублей");
        userInterface.println( );
        return balance;
    }

    /**
     * Положить деньги в банкомат
     * @param money
     * @throws IOException
     */
    public void putMoney( int money ) throws IOException {
        terminalServer.putMoney( money );
    }

    /**
     *
     * @param money
     * @return
     * @throws IOException
     * @throws NoBanknoteException
     */
    public boolean getMoney(  int money ) throws IOException, NoBanknoteException  {
        userInterface.println( "Сколько денег вы хотите снять ?" );
        userInterface.println( "Введите количество денег кратное 100" );
        int howManyMoneyNeed = keyBoard.get();
        if  (howManyMoneyNeed %100 !=0) {
            userInterface.println( "Сумма должна быть кратной 100 рублей" );
            return false;
        }
        boolean result = terminalServer.getMoney( money );
        if (result) {
            // можно напечатать чек
            // генерируем исключение, его можно избежать если написать проверу,
            throw new NoBanknoteException();
        }
        else {
            userInterface.println( "На вашем счете недостаточно денег" );
        }

        return result;
    }

}
