package br.com.fiap.techchallenge.naousar.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Objeto que representa o status do pedido dentro do sistema")
public class StatusPedidoDTO {
    @Schema(description = "Campo identificador único de status do pedido", example = "1")
    private Long id;

    @Schema(description = "Campo que informa o nome do status do pedido", example = "Em Preparo")
    private String nome;
}