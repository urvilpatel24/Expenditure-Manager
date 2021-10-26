package com.edmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "users_uk",columnNames = {"email"}) })
@Getter @Setter @NoArgsConstructor @ToString
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "users_id",nullable = false)
	private long id;
	
	@Column(name = "name",nullable = false)
	private String name;
	
	@Column(name = "email",nullable = false)
	private String email;

}
