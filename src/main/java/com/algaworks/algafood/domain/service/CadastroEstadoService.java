package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	@Autowired
	private EstadoRepository estadoRepository;

	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}

	public void excluir(Long estadoId) {
		try {
			estadoRepository.deleteById(estadoId);
		
		}catch (EmptyResultDataAccessException e) {
			 throw new  EntidadeNaoEncotradaException(
					String.format("Não existe cadasto de cozinha com codigo %d", estadoId));
		
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeNaoEncotradaException(String.format("Estado de codigo %d não pode ser removido, pois esta em uso", estadoId));
			
		}
	}
}
