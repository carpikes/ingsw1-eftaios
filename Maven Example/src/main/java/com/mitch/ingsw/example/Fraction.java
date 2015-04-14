package com.mitch.ingsw.example;

public class Fraction {
	private int numerator;
	private int denominator;
	
	public int getNumerator() {
		return numerator;
	}
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}
	public int getDenominator() {
		return denominator;
	}
	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}
	
	public Fraction(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	public Fraction(int numerator) {
		this.denominator = 1;
		this.numerator = numerator;
	}
	
	public Fraction addFraction(Fraction f) {
		Fraction res = new Fraction( this.numerator * f.getDenominator() + this.denominator * f.getNumerator(), this.denominator * f.getDenominator() );
		return res;
	}
	
	@Override
	public String toString() {
		return this.numerator + "/" + this.denominator;
	}

}
