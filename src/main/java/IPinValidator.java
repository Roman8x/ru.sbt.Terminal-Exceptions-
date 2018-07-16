public interface IPinValidator {
    /**
     *
     * @param card_number // номер карты
     * @param pin         // пин код
     * @return            // true - авторизовались, false - нет
     */
    boolean validate (long  card_number, int  pin );
}
