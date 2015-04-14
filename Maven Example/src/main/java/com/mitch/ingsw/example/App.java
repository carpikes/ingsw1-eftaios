package com.mitch.ingsw.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Fraction f1 = new Fraction(2,5);
        Fraction f2 = new Fraction(3,5);
        
        System.out.println(f1.addFraction(f2));
    }
}
