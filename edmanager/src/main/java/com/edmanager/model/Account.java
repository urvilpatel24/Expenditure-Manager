package com.edmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "account", uniqueConstraints = { @UniqueConstraint(name = "account_uk",columnNames = {"name", "account_users_fk"}) })
@Getter @Setter @NoArgsConstructor @ToString
public class Account {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false)
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "current_balance", nullable = false)
	private double currentBalance;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_users_fk",nullable = false)
    private Users user;

	public Account(String name, double currentBalance, Users user) {
		super();
		this.name = name;
		this.currentBalance = currentBalance;
		this.user = user;
	}
	
}
