package com.lian.hashmapdemo.hashmapinterface;

/**
 *
 * @param <K>  键
 * @param <V>  值
 */
public interface MyMap<K,V> {
    /**
     * 取值
     * @param k
     * @param v
     * @return
     */
    public V put(K k,V v);

    /**
     * 存值
     * @param k
     * @return
     */
    public V get(K k);

    /**
     * 获取内部数组
     * @return
     */
    Entry<K,V>[] getTable();


    /**
     *
     * @param <K>
     * @param <V>
     */
    interface Entry<K,V>{
        public K getKey();
        public V getValue();
    }
}
