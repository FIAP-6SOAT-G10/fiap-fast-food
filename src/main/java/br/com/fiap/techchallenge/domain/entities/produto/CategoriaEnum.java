package br.com.fiap.techchallenge.domain.entities.produto;

import br.com.fiap.techchallenge.domain.ErrosEnum;
import br.com.fiap.techchallenge.infra.exception.CategoriaException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public enum CategoriaEnum {
    LANCHE(1L, "LANCHE"),
    BEBIDA(2L, "BEBIDA"),
    ACOMPANHAMENTO(3L, "ACOMPANHAMENTO"),
    SOBREMESA(4L, "SOBREMESA");

    private final Long idCategoria;
    private final String nome;

    @JsonCreator
    public static CategoriaEnum fromName(String name) throws CategoriaException {
        Predicate<CategoriaEnum> byName = v -> v.getNome().equalsIgnoreCase(name);
        return Arrays.stream(values()).filter(byName).findAny().orElseThrow(() -> new CategoriaException(ErrosEnum.CATEGORIA_INVALIDA, "Categoria não encontrada."));
    }

    public static CategoriaEnum fromIdCategoria(Long id) {
        Predicate<CategoriaEnum> byId = e -> e.idCategoria.equals(id.intValue());
        CategoriaEnum categoria = Arrays.stream(CategoriaEnum.values()).filter(byId).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid codigo: " + id));
        return categoria;
    }

}