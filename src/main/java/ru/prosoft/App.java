package ru.prosoft;

import ru.prosoft.service.CacheLru;

/**
 * Пример работы с классом CacheLru<K, V>
 */
public class App {
    public static void main(String[] args) {

        /**
         * Инициализация кэша с размером фрейма = 3
         */
        CacheLru<String, String> cacheLru = new CacheLru(3);


        /**
         * Запись 20 значений в кэш
         */
        String key = null;
        String value = null;
        int i = 20;

        while (i > 0) {

            key = String.valueOf(i);

            if (cacheLru.getValue(key) == null) {

                /**
                 * Расчет нового значения value, которого нет в кэше
                 */
                value = "Value_" + String.valueOf(i);

                /**
                 * Запись нового значения value в кэше
                 */
                cacheLru.putToCache(key, value);
            } else {

                /**
                 * Значение получено из кэша
                 */

            }
            i--;
        }

        /**
         * Вывод содержимого кэша
         */
        System.out.println(cacheLru.toString());

    }



}
