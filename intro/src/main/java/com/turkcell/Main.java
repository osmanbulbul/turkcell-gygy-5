package com.turkcell;

import java.util.Arrays;

// Entrypoint
public class Main 
{
    public static void main(String[] args) 
    {
        System.out.println("Merhaba Turkcell, Java'ya hoş geldiniz!");

        // Programlama konseptleri

        // Scope Kavramı => {} kapsama alanı

        // Değişkenler (Variables)
        // Kodun akışında değer tutan isimli veriler.

        System.out.println(10);
        int X = 15; // Değişken tanımlandı. X ismine bir değer atandı.
        // Tanımlandıktan itibaren değişebilir, erişilebilir.
        System.out.println(X);
        X=20;
        System.out.println(X);

        // Değişken tipleri => int, double, boolean, char, String
        String name = "Halit";
        String age = "25";
        boolean isStudent = true;
        char grade = 'A';

        // Diziler (Arrays)

        String[] names = { "Halit", "Ayşe", "Mehmet" };
        System.out.println(names[0]); // index, 0 dan başlar.
        //System.out.println(names[5]);

        // Primitive (ilkel) tipler -> int, double,boolean,char
        int a = 0;
        int b = a;
        a = 10;
        System.out.println(a); 
        System.out.println(b); 

        // Referans tipler -> String, Array, Object
        int[] c = {0,1,2,3};
        int[] d = c;
        d[3] = 30;
        System.out.println(c[3]); 
        System.out.println(d[3]); 


        System.out.println("******");

        System.out.println(a==b);
        System.out.println(c==d);


        int[] x = {0,1,2,3};
        int[] y = {0,1,2,3};
        System.out.println(y);
        System.out.println(x==y); 
        System.out.println(Arrays.equals(x, y));


        String s1 = "Merhaba";
        String s2 = "Merhaba";
        System.out.println(s1==s2); // String Pool 
        // (Aynı metinlerin bir havuzda toplanıp performans için birebir olanları aynı referansa ata.)

        // Yine de daha güvenli bir karşılaştırma için equals() kullanılır.
        System.out.println(s1.equals(s2));

        String s3 = "Turkcell";
        String s4 = s3.intern(); 
        // intern() metodu, s3'ün değerini String Pool'a ekler ve oradaki referansı döndürür.

        System.out.println(s3==s4);

        String str3 = "Turkcell";
        String str4 = new String("Turkcell"); // instance oluşturur, farklı referans

        System.out.println(str3==str4); // false, farklı referanslar

        System.out.println("Merhaba" + " " + "Dünya!");

        System.out.println(10 * 20);
        System.out.println(10 / 3);

        System.out.println(1 == 1);
        System.out.println(1 != 1);
        System.out.println(5 > 10);

        a = a + 5;
        a+=5;
        a++; // a = a + 1;

        // Döngüler

        // X işlemini birden fazla kez çalıştırmak.
        // iteration = yineleme
        // değişken, koşul, her iterasyon sonrası işlem
        for(int i=0; i<5; i++) {
            System.out.println(i);
        }

        String[] students = {"Halit", "Ayşe", "Mehmet"};
        for(int i=0; i<students.length; i++) {
            System.out.println(students[i]);    
        }
        for(String z: students) {
            System.out.println(z);
        }
    }

    
} // Main classının kapsama alanı (sınır)
