package com.crash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "Nome Não pode ser nulo")
	@Column(nullable = false)
	@EqualsAndHashCode.Include
	private String username;
	@ToString.Exclude
	@NotNull(message = "Senha Não pode ser nulo")
	@Column(nullable = false)
	private String password;

	@NotNull(message = "Role Não pode ser nulo")
	@Column(nullable = false)
	@Builder.Default
	private String role = "USER";

	public ApplicationUser() {
	}

	public ApplicationUser(@NotNull ApplicationUser applicationUser) {
		this.id = applicationUser.getId();
		this.username = applicationUser.getUsername();
		this.password = applicationUser.getPassword();
		this.role = applicationUser.getRole();
	}

	public ApplicationUser(Long id, String username, String password, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
