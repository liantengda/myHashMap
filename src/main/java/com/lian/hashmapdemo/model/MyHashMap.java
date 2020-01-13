package com.lian.hashmapdemo.model;

import com.lian.hashmapdemo.hashmapinterface.MyMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HashMap有3个要素：hash函数+数组+单链表
 *对于hash函数而言，需要考虑些什么？
 * 要快，对于给定的Key，要能够快速计算出在数组中的index。那么什么运算够快呢？显然是位运算！
 * 要均匀分布，要较少碰撞。说白了，我们希望通过hash函数，让数据均匀分布在数组中，不希望大量数据发生碰撞，
 * 导致链表过长。那么怎么办到呢？也是利用位运算，通过对数据的二进制的位进行移动，让hash函数得到的数据散列开来，
 * 从而减低了碰撞的概率。
 *
 * 数组和链表的节点长得一样。
 * @param <K>   构造MyHashMap时，确定key的类型
 * @param <V>   构造MyHashMap时，确定value的类型
 */
public class MyHashMap<K,V> implements MyMap<K,V> {

    //数组的默认长度，1左移四位，10000   2的4次，16
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    //阈值比例
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //数组重组次数
    public static Integer resizeCount = 1;
    //哈希碰撞次数
    public static Integer conflictCount = 1;
    //默认初始化大小
    private int defaultInitSize;
    //默认装填因子
    private float defaultLoadFactor;
    //Map当中entry的数量
    private int entryUseSize;
    //数组    这个数组需要初始化，不然数组本身为空。
    private Entry<K,V>[] table = new Entry[DEFAULT_INITIAL_CAPACITY];

    /**
     * 构造函数
     *
     * 这里使用了门面模式，虽然两个构造方法指向了一个，但是向外暴露了两个门面
     */
    public MyHashMap(){
        this(DEFAULT_INITIAL_CAPACITY,DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int defaultInitialCapacity,float defaultLoadFactor){
        if(defaultInitialCapacity<0){
            throw new IllegalArgumentException("illegal initial capacity:"+defaultInitialCapacity);
        }

        if(defaultLoadFactor<=0||Float.isNaN(defaultLoadFactor)){
            throw new IllegalArgumentException("illegal load factor:"+defaultLoadFactor);
        }

        this.defaultInitSize = defaultInitialCapacity;
        this.defaultLoadFactor = defaultLoadFactor;
    }

    /**
     * 结点类   内部类
     * @param <K>
     * @param <V>
     */
    class Entry<K,V> implements MyMap.Entry<K,V> {

        private K key;
        private V value;
        private Entry<K,V> next;
        private int khashcode;
        public Entry(){

        }

        public Entry(K key, V value, Entry<K,V> next){
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * @return
         */
        @Override
        public K getKey() {
            return key;
        }

        public int getKhashcode() {
            return khashcode;
        }

        public void setKhashcode(int khashcode) {
            this.khashcode = khashcode;
        }

        /**
         * @return
         */
        @Override
        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    ", khashcode='" + khashcode + '\'' +
                    '}';
        }
    }

    /**
     *
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        V oldValue = null;
        //是否需要扩容，扩容完毕后，肯定需要重新散列。
        if (entryUseSize>=defaultInitSize*defaultLoadFactor){
            resize(2*defaultInitSize);
        }
        int index = hash(k)&(defaultInitSize-1);
        //如果数组这里为空，则填入第一个节点。
        if(table[index]==null){
            //table和链表的节点长得一样
            table[index] = new Entry<>(k,v,null);
            table[index].setKhashcode(hash(k));
            ++entryUseSize;//数组长度+1，数组容量-1
        }else {
            conflictCount++;
            Entry<K,V> entry = table[index];
            Entry<K,V> e = entry;
            while (e!=null){
                //如果key没有变，那么就把值给更换了。
                if(k == e.getKey()||k.equals(e.getKey())){
                    oldValue = e.getValue();
                    e.value = v;
                    return oldValue;
                }
                //如果key不一样，那么就应该加个节点。
                e = e.next;
            }
            table[index] = new Entry<K,V>(k,v,entry);
            ++ entryUseSize;
        }
        return oldValue;
    }

    @Override
    public V get(K k) {
        int index = hash(k)&(defaultInitSize-1);

        if(table[index]==null){
            return null;
        }else{
            Entry<K,V> entry = table[index];
            do {
                if(k == entry.getKey()||k.equals(entry.getKey())){
                    return entry.value;
                }
                entry = entry.next;
            }while (entry!=null);
        }
        return null;
    }

    /**
     * 散列方式
     * @param k
     * @return
     */
    private int hash(K k){
        int hashCode = k.hashCode();
        hashCode ^=(hashCode>>>20)^(hashCode>>>12);
        return hashCode^ (hashCode>>>7)^(hashCode>>>4);
    }

    /**
     * 重组数组
     * @param newSize
     */
    private void resize(int newSize){
        System.out.println(this);
        System.out.println("<-------------数组发生第"+resizeCount+++"次重组-------------->");
        Entry[] newTable = new Entry[newSize];
        defaultInitSize = newSize;
        //这句话是干啥用的，使用大小置空，因为需要重新散列
        entryUseSize =0;
        System.out.println("原数组大小-->"+table.length);
        //重新散列
        rehash(newTable);
        System.out.println("先数组大小-->"+table.length);
    }

    /**
     * 重新散列
     * @param newTable
     */
    private void rehash(Entry<K,V>[] newTable){
        List<Entry<K,V>> entryList = new ArrayList<>();
        //遍历老table，将所有结点装到结点集合中
        for(Entry<K,V> entry: table){
            if(entry!=null){
                do{
                    //遍历每个数组索引下的单链表，第一个结点为数组和链表共有。
                    entryList.add(entry);
                    entry = entry.next;
                }while (entry!=null);
            }
        }
        //如果新的table长度大于0，那么就把本hashmap的表指向新表。
        if(newTable.length>0){
            table = newTable;
        }
        //重新塞入一遍旧的值
        for(Entry<K,V> entry: entryList){
            put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * 获得map的底层数组
     * @return
     */
    @Override
    public Entry<K,V>[] getTable(){
        return table;
    }

    @Override
    public String toString() {
        return "MyHashMap{" +
                "defaultInitSize=" + defaultInitSize +
                ", defaultLoadFactor=" + defaultLoadFactor +
                ", entryUseSize=" + entryUseSize +
                ", table=" + Arrays.toString(table) +
                '}';
    }

}
