package com.fourcamp.linkbank.model;

import java.time.LocalDate;

public abstract class Transaction {

	private Double value;

	private LocalDate date;

	private Long id;

	public Transaction(Double value, String date, Long id) {
		this.value = value;
		this.date = LocalDate.now();
		this.id = id;
	}

	public Transaction() {
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getDate() {
		return date.toString();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
