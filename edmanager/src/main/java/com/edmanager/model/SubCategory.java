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
@Table(name = "sub_category", uniqueConstraints = { @UniqueConstraint(name = "sub_category_uk",columnNames = {"name", "category_fk", "user_fk"}) })
@Getter @Setter @NoArgsConstructor @ToString
public class SubCategory {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sub_category_id", nullable = false)
	private long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_fk",nullable = false)
    private Users user;
}
