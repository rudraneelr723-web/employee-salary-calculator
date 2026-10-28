package com.example.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Salary(BigDecimal basic, BigDecimal allowances, BigDecimal hra, BigDecimal da,
                     BigDecimal gross, BigDecimal pf, BigDecimal professionalTax,
                     BigDecimal deductions, BigDecimal net) {
    private static BigDecimal money(BigDecimal n) {
        return n.setScale(2, RoundingMode.HALF_UP);
    }

    public static Salary calculate(BigDecimal basic, BigDecimal allowances) {
        basic = money(basic);
        allowances = money(allowances);
        BigDecimal hra = money(basic.multiply(new BigDecimal("0.20")));
        BigDecimal da = money(basic.multiply(new BigDecimal("0.10")));
        BigDecimal gross = money(basic.add(hra).add(da).add(allowances));
        BigDecimal pf = money(basic.multiply(new BigDecimal("0.12")));
        BigDecimal tax = gross.compareTo(new BigDecimal("15000.00")) > 0
                ? new BigDecimal("200.00") : new BigDecimal("0.00");
        BigDecimal deductions = money(pf.add(tax));
        BigDecimal net = money(gross.subtract(deductions));
        return new Salary(basic, allowances, hra, da, gross, pf, tax, deductions, net);
    }
}
