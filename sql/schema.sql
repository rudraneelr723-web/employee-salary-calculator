CREATE DATABASE IF NOT EXISTS employee_salary_db;
USE employee_salary_db;

CREATE TABLE IF NOT EXISTS employees (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    basic_salary DECIMAL(12,2) NOT NULL,
    other_allowances DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    hra DECIMAL(12,2) NOT NULL,
    da DECIMAL(12,2) NOT NULL,
    gross_salary DECIMAL(12,2) NOT NULL,
    pf_deduction DECIMAL(12,2) NOT NULL,
    professional_tax DECIMAL(12,2) NOT NULL,
    total_deductions DECIMAL(12,2) NOT NULL,
    net_salary DECIMAL(12,2) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_basic_salary CHECK (basic_salary >= 0),
    CONSTRAINT chk_allowances CHECK (other_allowances >= 0)
);
