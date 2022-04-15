package ru.prosoft.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс CacheLru содержит методы реализации кэша определенного размера по алгоритму кэширования Least recently used (LRU)
 *
 * @param <K> тип данных ключа кэша
 * @param <V> тип данных значения кэша
 * @author Sergey Proshchaev
 * @version 1.1
 */

public class CacheLru<K, V> {

    /**
     * Поле размер кэша
     */
    private int cacheSize;

    /**
     * Поле значение приоритета от Integer.MIN_VALUE до Integer.MAX_VALUE
     */
    static Integer priority = Integer.MIN_VALUE;

    /**
     * Хеш-таблица для хранения закэшированных значений
     */
    private Map<K, V> hashTable = new HashMap<>();

    /**
     * Хеш-таблица приоритетов закэшированных значений. Последнее значение имеет наивысший приоритет
     */
    private Map<K, Integer> timeQueue = new HashMap<>();


    /**
     * Конструктор класса с параметром размера кэша
     *
     * @param cacheSize - размер кэша
     */
    public CacheLru(Integer cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * Генерация приоритета (номер обращения к переменной)
     * значение от Integer.MIN_VALUE (-2147483648) до Integer.MAX_VALUE (2147483647)
     * с циклическим переворотом для исключения переполнения
     *
     * @return
     */
    public Integer getNewPriority() {
        if (priority == Integer.MAX_VALUE) {

            priority = Integer.MIN_VALUE;

            updateValuesTimeQueue();

            return priority;

        } else {
            return ++priority;
        }
    }

    /**
     * Метод updateValuesTimeQueue() перезаписывает все значения Value в timeQueue
     * в соответствии с приоритетами, начиная с Integer.MIN_VALUE
     */

    public void updateValuesTimeQueue() {

        for (int i = 0; i < timeQueue.size(); i++) {

            K key = null;
            Integer minValue = Integer.MAX_VALUE;

            for (Map.Entry entry : timeQueue.entrySet()) {

                if (((Integer) entry.getValue() <= minValue) && ((Integer) entry.getValue() > 0)) {
                    minValue = (Integer) entry.getValue();
                    key = (K) entry.getKey();
                }
            }

            if (key != null) {
                timeQueue.put(key, priority++);
            }
        }
    }

    /**
     * Метод putToCache() записывает новое значение в кэш
     *
     * @param key   ключ
     * @param value значение
     */
    public void putToCache(K key, V value) {

        if (hashTable.size() >= cacheSize) {
            K keyForRemove = extractMinValue();
            hashTable.remove(keyForRemove);
            timeQueue.remove(keyForRemove);
        }

        hashTable.put(key, value);
        timeQueue.put(key, getNewPriority());

    }

    /**
     * Метод getValue() возвращает значение Value по передаваемуму ключу в параметре
     *
     * @param key
     * @return
     */

    public V getValue(K key) {

        V resultGetValue = hashTable.get(key);

        if (resultGetValue != null) {
            timeQueue.put(key, getNewPriority());
        }

        return resultGetValue;
    }

    /**
     * Метод extractMinValue() возвращает индекс самого наиболее давно использованного элемента кэша
     *
     * @return
     */
    public K extractMinValue() {

        K key = null;
        Integer minValue = Integer.MAX_VALUE;

        for (Map.Entry entry : timeQueue.entrySet()) {

            if ((Integer) entry.getValue() < minValue) {
                minValue = (Integer) entry.getValue();
                key = (K) entry.getKey();
            }
        }
        return key;
    }

    @Override
    public String toString() {
        return "CacheLru{" + "hashTable=" + hashTable + ", timeQueue=" + timeQueue + ", cacheSize=" + cacheSize + '}';
    }


}
