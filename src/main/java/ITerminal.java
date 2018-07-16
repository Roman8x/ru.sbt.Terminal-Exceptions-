
import TerminalExceptions.NoBanknoteException;

import javax.security.auth.login.AccountLockedException;
import java.io.IOException;

/**
 * 1) Проверить состояние счета
 * 2) Снять/ положить деньги
 *
 * Интерфейс терминала и список исключений остается на ваш дизайн.
 * В каждом ексепшене должно быть описание, что нужно сделать,
 * чтобы избежать его в дальнейшем.
 * */
public interface ITerminal {
    /**
     *  Проверка баланса
     * @return баланс аккаунта
     * @throws AccountLockedException // аккаунт заблокирован (карта заблокирована )
     * @throws IOException      // банк недоступен, потеря связи ( очень часто вижу)
     * @throws NoBanknoteException       // Банкнот недостаточно для выдачи
     */
    int  checkAccountBalance  () throws  IOException;

    /**
     *
     * @throws IOException
     * Положить деньги
     */
      void putMoney (int money ) throws IOException;
    /**
     *
     * @return получить деньги, ( со счета \ банкомата)
     * @throws IOException // потеря свяизи с сервером
     * @throws NoBanknoteException  // Нет банкнот в банкомате
     */
      boolean getMoney (int money  ) throws IOException , NoBanknoteException;

}
