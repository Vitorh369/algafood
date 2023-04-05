package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data // REPRESENTA, get, setter, equal, hastCode e to String
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include // INCLUINDO APENAS O id PARA EqualsAndHashCode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false) //NÃO ACEITA nome NULO
	private String nome;

	
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@JoinColumn(name = "cozinha_id", nullable = false)// NOME DA COLUNA COM RELACIONAMENTO! PODEMOS POR NOME Q QUISER, MAS POR PADRÃO JA VAI SER cozinha_id
	@ManyToOne
	private Cozinha cozinha;

}
