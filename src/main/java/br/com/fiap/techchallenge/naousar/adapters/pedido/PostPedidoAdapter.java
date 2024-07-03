//package br.com.fiap.techchallenge.naousar.adapters.pedido;
//
//import br.com.fiap.techchallenge.domain.ErrosEnum;
//import br.com.fiap.techchallenge.domain.entities.pagamento.StatusPagamentoEnum;
//import br.com.fiap.techchallenge.domain.entities.pedido.StatusPedidoEnum;
//import br.com.fiap.techchallenge.infra.mapper.ProdutoPedidoMapper;
//import br.com.fiap.techchallenge.infra.mapper.cliente.ClienteMapper;
//import br.com.fiap.techchallenge.infra.mapper.pedido.PedidoMapper;
//import br.com.fiap.techchallenge.infra.persistence.ClienteRepository;
//import br.com.fiap.techchallenge.infra.persistence.PedidoRepository;
//import br.com.fiap.techchallenge.infra.persistence.ProdutoEntityRepository;
//import br.com.fiap.techchallenge.infra.persistence.ProdutoPedidoRepository;
//import br.com.fiap.techchallenge.infra.persistence.entities.*;
//import br.com.fiap.techchallenge.naousar.domain.valueobjects.*;
//import br.com.fiap.techchallenge.naousar.infra.exception.BaseException;
//import br.com.fiap.techchallenge.naousar.infra.exception.ClienteException;
//import br.com.fiap.techchallenge.naousar.infra.exception.PedidoException;
//import br.com.fiap.techchallenge.naousar.infra.exception.ProdutoException;
//import br.com.fiap.techchallenge.naousar.ports.cliente.PostPedidoOutboundPort;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Slf4j
//public class PostPedidoAdapter implements PostPedidoOutboundPort {
//
//    private final PedidoRepository pedidoRepository;
//    private final PedidoMapper pedidoMapper;
//    private final ClienteMapper clienteMapper;
//    private final ProdutoPedidoMapper produtoPedidoMapper;
//    private final ProdutoEntityRepository produtoEntityRepository;
//    private final ProdutoPedidoRepository produtoPedidoRepository;
//    private final ClienteRepository clienteRepository;
//
//    public PostPedidoAdapter(PedidoRepository pedidoRepository, PedidoMapper mapper, ClienteMapper clienteMapper, ProdutoPedidoMapper produtoPedidoMapper, ProdutoEntityRepository produtoEntityRepository, ProdutoPedidoRepository produtoPedidoRepository, ClienteRepository clienteRepository) {
//        this.pedidoRepository = pedidoRepository;
//        this.pedidoMapper = mapper;
//        this.clienteMapper = clienteMapper;
//        this.produtoPedidoMapper = produtoPedidoMapper;
//        this.produtoEntityRepository = produtoEntityRepository;
//        this.produtoPedidoRepository = produtoPedidoRepository;
//        this.clienteRepository = clienteRepository;
//    }
//
//    @Override
//    public PedidoDTO realizarCheckout(Long id) throws BaseException, InterruptedException {
//        log.info("realizarCheckout");
//
//        TimeUnit.SECONDS.sleep(5);
//
//        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
//        if (pedidoOptional.isEmpty()) {
//            throw new PedidoException(ErrosEnum.PEDIDO_CODIGO_IDENTIFICADOR_INVALIDO);
//        }
//
//        Pedido pedido = pedidoOptional.get();
//        pedido.setStatus(new StatusPedido(StatusPedidoEnum.RECEBIDO.getStatus()));
//        pedido.setStatusPagamento(new StatusPagamento(StatusPagamentoEnum.PAGO.getStatus()));
//
//        return this.pedidoMapper.toDTO(pedidoRepository.saveAndFlush(pedido));
//    }
//
//    private List<ProdutoPedidoEntity> totalizaItensDoPedido(ItemDTO item) {
//        Map<Long, List<ItemPedidoDTO>> items = Stream.of(
//                                                        item.getLanches(),
//                                                        item.getAcompanhamento(),
//                                                        item.getBebida(),
//                                                        item.getSobremesa())
//                                                .reduce(this::unificarItens)
//                                                .get()
//                                                .stream()
//                                                .collect(Collectors.groupingBy(ItemPedidoDTO::getId));
//
//        return items.entrySet().stream().map(this::criarListaDeProdutosPedido)
//                                                .reduce(this::unificarProdutos)
//                                                .stream()
//                                                .findAny()
//                                                .orElse(Collections.emptyList());
//    }
//
//    private List<ItemPedidoDTO> unificarItens(List<ItemPedidoDTO> primeira, List<ItemPedidoDTO> segunda) {
//        primeira.addAll(segunda);
//        return primeira;
//    }
//
//    private List<ProdutoPedidoEntity> unificarProdutos(List<ProdutoPedidoEntity> primeira, List<ProdutoPedidoEntity> segunda) {
//        List<ProdutoPedidoEntity> produtos = new ArrayList<>();
//        produtos.addAll(primeira);
//        produtos.addAll(segunda);
//
//        return produtos;
//    }
//
//    private List<ProdutoPedidoEntity> criarListaDeProdutosPedido(Map.Entry<Long, List<ItemPedidoDTO>> entry) {
//        Long id = entry.getKey();
//        List<ItemPedidoDTO> itens = entry.getValue();
//
//        long quantidade = itens.stream().map(ItemPedidoDTO::getQuantidade).reduce(0L, Long::sum);
//        ProdutoEntity produtoEntity = produtoEntityRepository.findById(id).orElseThrow(()-> new ProdutoException(ErrosEnum.PRODUTO_NAO_ENCONTRADO));
//
//        return List.of(ProdutoPedidoEntity
//                .builder()
//                .produtoEntity(produtoEntity)
//                .quantidade(BigInteger.valueOf(quantidade))
//                .valorTotal(produtoEntity.getPreco().multiply(BigDecimal.valueOf(quantidade)))
//                .build()
//        );
//    }
//
//    @Override
//    public PedidoDTO criarPedido(PedidoRequestDTO request) {
//        Cliente cliente = buscaCliente(request);
//
//        List<ProdutoPedidoEntity> itens = totalizaItensDoPedido(request.getItems());
//        BigDecimal subtotal = itens.stream().map(ProdutoPedidoEntity::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        Pedido pedido = pedidoRepository.saveAndFlush(pedidoMapper.toEntity(PedidoDTO
//                .builder()
//                .valor(subtotal)
//                .cliente(cliente == null ? null : clienteMapper.toDTO(cliente))
//                .statusPagamento(StatusPagamentoDTO
//                        .builder()
//                        .id(StatusPagamentoEnum.PENDENTE.getId())
//                        .nome(StatusPagamentoEnum.PENDENTE.getStatus())
//                        .build())
//                .status(StatusPedidoDTO
//                        .builder()
//                        .id(StatusPedidoEnum.RECEBIDO.getId())
//                        .nome(StatusPedidoEnum.RECEBIDO.getStatus())
//                        .build())
//                .build()));
//
//        itens.parallelStream().forEach(item -> item.setPedido(pedido));
//        produtoPedidoRepository.saveAll(itens);
//
//        return this.pedidoMapper.toDTO(pedido);
//    }
//
//    private Cliente buscaCliente(PedidoRequestDTO request) {
//        if(request.getCliente() == null || request.getCliente().isBlank() || request.getCliente().isEmpty()) {
//            return null;
//        }
//        Optional<Cliente> cliente = clienteRepository.findByCpf(request.getCliente());
//        if (cliente.isEmpty()){
//            throw new ClienteException(ErrosEnum.CLIENTE_CPF_NAO_EXISTE);
//        }
//        return cliente.get();
//    }
//}
