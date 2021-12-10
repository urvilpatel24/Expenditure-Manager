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
	
	@Column(name = "name", nullable = false)
	private String name;
	
	/*@Column(name = "current_balance", nullable = false)
	private double currentBalance;*/
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_users_fk",nullable = false)
    private Users user;

	public Account(String name, Users user) {
		super();
		this.name = name;
//		this.currentBalance = currentBalance;
		this.user = user;
	}
	
}
