package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
		if (cozinha.isPresent()) {// est presente
			return ResponseEntity.ok(cozinha.get()); // reotrna 200
		}
		return ResponseEntity.notFound().build(); // retorna 404

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) // retorna 201
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinha.salvar(cozinha);

	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
			Cozinha cozinhaAtual = cozinhaRepository.findById(cozinhaId).orElse(null);

		if (cozinhaAtual != null) {
			// cozinhaAtual.setNome(cozinha.getNome()); PODEMOS SETAR TODOS OS ATRIBUTOS COM
			// COGIGO ABAIXO, USANDO BeanUtils.copyProperties.
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id"); // IGNORA O ID, pois o ID NÃO ATUALIZA

			Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual);

			return ResponseEntity.ok(cozinhaSalva);
		}
		return ResponseEntity.notFound().build();// retorna 404
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {
		try {
			cadastroCozinha.excluir(cozinhaId);
			return ResponseEntity.noContent().build(); // devolve 204

		} catch (EntidadeNaoEncotradaException e) {
			return ResponseEntity.notFound().build(); // retorna 404 não encotrado

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build(); // retorna 409, nãp ode deleta o recurso
		}
	}
}
