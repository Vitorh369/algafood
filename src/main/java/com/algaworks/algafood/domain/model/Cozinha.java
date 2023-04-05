package com.algaworks.algafood.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("gastronomia") SUBISTIUI O NOME DA CLASS QUANDO FOR FEITO A REQUISIÇÃO

@Data // REPRESENTA, get, setter, equal, hastCode e to String
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@JsonIgnore ESCONDE O ATRIBUTO nome QUANDO FOR FEITO A REQUISIÇÃO
	//@JsonProperty(value = "titulo")// ATRIBUTO nome SERA CHAMADO DE titulo QUANDO FOR FEITO A REQUISIÇÃO 
	
	@JoinColumn(nullable = false)
	private String nome;

}
