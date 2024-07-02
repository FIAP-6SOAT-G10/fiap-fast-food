package br.com.fiap.techchallenge.apis.pedido;

import br.com.fiap.techchallenge.apis.PedidoController;
import br.com.fiap.techchallenge.domain.entities.Pedido;
import br.com.fiap.techchallenge.domain.entities.StatusPagamento;
import br.com.fiap.techchallenge.domain.entities.StatusPedido;
import br.com.fiap.techchallenge.domain.model.mapper.ProdutoPedidoMapper;
import br.com.fiap.techchallenge.domain.model.mapper.cliente.ClienteMapper;
import br.com.fiap.techchallenge.domain.model.mapper.pedido.PedidoMapper;
import br.com.fiap.techchallenge.infra.exception.PedidoException;
import br.com.fiap.techchallenge.infra.repositories.ClienteRepository;
import br.com.fiap.techchallenge.infra.repositories.PedidoRepository;
import br.com.fiap.techchallenge.infra.repositories.ProdutoPedidoRepository;
import br.com.fiap.techchallenge.infra.repositories.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PedidoPatchControllerTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoMapper pedidoMapper;

    @Autowired
    ClienteMapper clienteMapper;

    @Autowired
    ProdutoPedidoMapper produtoPedidoMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void shouldAtualizarStatusDoPedidoParaEmPreparacao() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatus(new StatusPedido("recebido"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("preparacao", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/status");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        ResponseEntity<Pedido> response = pedidoController.atualizarStatusDoPedido("1", jsonPatch);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldAtualizarStatusDoPedidoParaPronto() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatus(new StatusPedido("preparacao"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("pronto", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/status");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        ResponseEntity<Pedido> response = pedidoController.atualizarStatusDoPedido("1", jsonPatch);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldAtualizarStatusDoPedidoParaFinalizado() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatus(new StatusPedido("pronto"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("finalizado", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/status");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        ResponseEntity<Pedido> response = pedidoController.atualizarStatusDoPedido("1", jsonPatch);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void mustLancarPedidoExceptionQuandoAtualizarPedidoComIdentificadorInvalido() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatus(new StatusPedido("pronto"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("finalizado", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/status");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        assertThrows(PedidoException.class, () -> pedidoController.atualizarStatusDoPedido("1A", jsonPatch));
    }

    @Test
    void mustLancarPedidoExceptionQuandoAtualizarPedidoComIdentificadorInexistente() throws JsonPointerException {
        when(pedidoRepository.findById(any())).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("finalizado", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/status");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        assertThrows(PedidoException.class, () -> pedidoController.atualizarStatusDoPedido("99", jsonPatch));
    }

    @Test
    void shouldAtualizarPagamentoDoPedidoParaPago() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatusPagamento(new StatusPagamento("pendente"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("pago", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/statusPagamento");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        ResponseEntity<Pedido> response = pedidoController.atualizarStatusDePagamento("1", jsonPatch);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldAtualizarPagamentoDoPedidoParaRecusado() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatusPagamento(new StatusPagamento("pendente"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("recusado", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/statusPagamento");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        ResponseEntity<Pedido> response = pedidoController.atualizarStatusDePagamento("1", jsonPatch);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void mustLancarPedidoExceptionQuandoAtualizarPagamentoDoPedidoComStatusRecusado() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatusPagamento(new StatusPagamento("recusado"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("pago", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/statusPagamento");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        assertThrows(PedidoException.class, () -> pedidoController.atualizarStatusDePagamento("1", jsonPatch));
    }

    @Test
    void mustLancarPedidoExceptionQuandoAtualizarPagamentoDoPedidoComStatusPago() throws JsonPointerException {
        Pedido pedido = new Pedido();
        pedido.setStatusPagamento(new StatusPagamento("pago"));
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.saveAndFlush(any())).thenReturn(pedido);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode nodeDTO = objectMapper.convertValue("recusado", JsonNode.class);

        JsonPointer pointer = new JsonPointer("/statusPagamento");
        List<JsonPatchOperation> operations = List.of(new ReplaceOperation(pointer, nodeDTO));

        JsonPatch jsonPatch = new JsonPatch(operations);

        PedidoController pedidoController =  new PedidoController(pedidoRepository, produtoRepository, produtoPedidoRepository, pedidoMapper, clienteMapper, produtoPedidoMapper,clienteRepository);
        assertThrows(PedidoException.class, () -> pedidoController.atualizarStatusDePagamento("1", jsonPatch));
    }

}
