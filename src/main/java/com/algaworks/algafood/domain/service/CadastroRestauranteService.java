package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restaurantaRepository;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)// REOTRNA A COZINHA
				.orElseThrow(()->  new EntidadeNaoEncotradaException(// SE A COZINHA NÃO EXISTIR LANÇA A EXECEÇÃO
						String.format("Não existe cadastro de cozinhada com codigo %d ", cozinhaId)));
				
		restaurante.setCozinha(cozinha);
		return restaurantaRepository.save(restaurante);
	}
}
