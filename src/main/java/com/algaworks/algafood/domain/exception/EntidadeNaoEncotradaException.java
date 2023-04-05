package com.algaworks.algafood.domain.exception;

public class EntidadeNaoEncotradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncotradaException(String mensagem) {
		super(mensagem);
	}

}
