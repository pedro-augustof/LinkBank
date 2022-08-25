package com.fourcamp.linkbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_id", nullable = false)
	private Long client_id;

	@CPF(message = "[Número de CPF inválido]")
	@NotBlank(message = "[CPF não informado]")
	@Column(nullable = false, unique = true, length = 14)
	private String cpf;

	@Pattern(regexp = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{1}")
	@NotBlank(message = "[RG não informado]")
	@Column(nullable = false, unique = true)
	private String rg;

	@Column(nullable = false)
	private String fullname;

	@NotBlank(message = "Data não informada")
	@Column(nullable = false)
	private String birthday;

	@NotBlank(message = "Salário não informado")
	@Column(nullable = false)
	private Double income;

	@NotBlank(message = "Senha não informada")
	@Column(nullable = false)
	private String password;

	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	@Email(message = "E-mail inválido")
	@NotBlank(message = "E-mail não informado")
	@Column(nullable = false)
	private String email;

	@NotBlank(message = "Profissão não informada")
	@Column(nullable = false)
	private String profession;

	@OneToOne
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address address;

	@Pattern(regexp = "^([11-99]{2})([9]{1})([0-9]{8})+$")
	@NotBlank(message = "Número de celular não informado")
	@Column(nullable = false)
	private String cellphone;

}
