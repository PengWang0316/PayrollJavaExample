package com.serverless.models;

public class Payroll {
	
	private int		numberOfWorkingHours = 0;
	private	double 	payRatePerHour 		 = 0.0;
	
	public Payroll (int numberOfWorkingHours, double payRatePerHour) {
		this.numberOfWorkingHours = numberOfWorkingHours;
		this.payRatePerHour = payRatePerHour;
	}

	public double calculateGrossPay() {
		double grossPay = this.numberOfWorkingHours * this.payRatePerHour;
		return grossPay;
	}
	
}
