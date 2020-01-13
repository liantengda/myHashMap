package com.lian.hashmapdemo.test;


public class TestHashCode {

    public static void main(String[] args) {

        for(int i=0;i<10;i++){
            System.out.println("<-----------------------------"+i+"----------------------------------->");
            hash("hello"+i);
        }
    }


    private static int  hash(String k){
        int hashCode = k.hashCode();
        System.out.println(k+"的hashcode--->"+hashCode);
        int hello20 = hashCode>>>20;
        System.out.println(k+"的hashcode>>>20--->"+hello20);
        int hello12 = hashCode>>> 12;
        System.out.println(k+"的hashcode>>>12--->"+hello12);
        int hello2012 = (hashCode>>>20)^(hashCode>>>12);
        System.out.println("(hashCode>>>20)^(hashCode>>>12)"+hello2012);
        hashCode ^=(hashCode>>>20)^(hashCode>>>12);
        System.out.println("hashCode ^=(hashCode>>>20)^(hashCode>>>12)--->"+hashCode);
        int hashhash7 = hashCode>>>7;
        System.out.println("hashCode ^=(hashCode>>>20)^(hashCode>>>12)>>>7--->"+hashhash7);
        int hashhash4 = hashCode>>>4;
        System.out.println("hashCode ^=(hashCode>>>20)^(hashCode>>>12)>>>4--->"+hashhash4);
        int hashhash7hash = hashCode^ (hashCode>>>7);
        System.out.println("hashcode^hashCode ^=(hashCode>>>20)^(hashCode>>>12)>>>7--->"+hashhash7hash);
        int hashhash7hash4 = hashCode^ (hashCode>>>7)^(hashCode>>>4);
        System.out.println("hashCode^ (hashCode>>>7)^(hashCode>>>4)---->"+hashhash7hash4);
        return hashCode^ (hashCode>>>7)^(hashCode>>>4);
    }
}
