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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "expense")
@Getter @Setter @NoArgsConstructor @ToString
public class Expense {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "expense_id", nullable = false)
	private long id;
	
	@Column(name = "date", nullable = false)
	private long date;
	
	@Column(name = "amount", nullable = false)
	private double amount;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_category_fk",nullable = false)
    private Category category;

	@Column(name = "sub_category_id")
    private long subCategoryId;
	
	@Column(name = "account_id")
    private long accountId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_users_fk",nullable = false)
    private Users user;
}
