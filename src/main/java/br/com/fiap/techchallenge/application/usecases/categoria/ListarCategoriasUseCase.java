package br.com.fiap.techchallenge.application.usecases.categoria;

import br.com.fiap.techchallenge.application.gateways.ICategoriaRepository;
import br.com.fiap.techchallenge.infra.persistence.entities.CategoriaEntity;

import java.util.List;

public class ListarCategoriasUseCase {

    private final ICategoriaRepository categoriaRepository;

    public ListarCategoriasUseCase(ICategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaEntity> listarCategorias() {
        return this.categoriaRepository.listarCategorias();
    }


}
