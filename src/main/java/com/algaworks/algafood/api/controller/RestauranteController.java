package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("{restauranteId}")
	public ResponseEntity<Restaurante> buscarId(@PathVariable Long restauranteId) {
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());

		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestaurante.salvar(restaurante);

			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);

		} catch (EntidadeNaoEncotradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());// devolve 400 requisão q o cliente passo não pode
																	// ser aceita

		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
		Restaurante restauranteAtual = restauranteRepository.findById(id).orElse(null);

		try {
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				restauranteRepository.save(restauranteAtual);
				return ResponseEntity.ok(restauranteAtual);
			}
			return ResponseEntity.notFound().build(); // devolce 404 - recurso soclicatdo  não existe
			
		} catch (EntidadeNaoEncotradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());// caso entidade da cozinha não extistir, devolve mensagem e 400 
		}
	}
	
	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long  restauranteId, 
			@RequestBody Map<String, Object> campos){ // String É CHAVE e O Object é o valor
		
		Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

		if(restauranteAtual == null) {
			ResponseEntity.notFound().build();
		}
			
		merge(campos, restauranteAtual.get());
		
		return atualizar(restauranteId, restauranteAtual.get());
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper(); // CONVERTE JANSON EM OBJETO JAVA, E VICE-VERSO
		Restaurante restauranteOrigem =  objectMapper.convertValue(dadosOrigem, Restaurante.class);// CRIA UMA INSTACIA DE Restaurante COM BASE AO map
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);//Field UMA CLASS UTILITARIA! A VARIAVEL field VAI REPRESENTA UM ATRIBUTO DA CLASS Restaurante! VAI FAZER DINAMICAMENTE EM TEMPO DE COMPILAÇÃO. EX: SE PRECISARMOS SO DO ATRIBUTO nome, DA CLASS Restaurante, VAI TRAZER A APENAS O NOME EM TEMPO DE COMPILAÇÃO
			field.setAccessible(true);// COMO OS ATRIBUTOS SÃO PRIVADOS, NOS PERMITIMOS SER ACESSADO
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);// BUSCANDO VALOR DO CAMPO
			
//			System.out.println(nomePropriedade + " = "  + valorPropriedade + " = "  + novoValor);
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor); // ESTOU ATRIBUINDO NO CAMPO(field), NO Object ResutanteDestino, PRA SETA novoValor
	});
}

}



