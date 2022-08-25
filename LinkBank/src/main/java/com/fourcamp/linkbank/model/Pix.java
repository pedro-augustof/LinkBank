package com.fourcamp.linkbank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
public class Pix {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pix_id", nullable = false)
	private Long pix_id;

	@CPF(message = "[Número de CPF inválido]")
	@Column
	private String cpf;

	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	@Column
	private String email;

	@Pattern(regexp = "^([11-99]{2})([9]{1})([0-9]{8})+$")
	@Column
	private String cellphone;

}
