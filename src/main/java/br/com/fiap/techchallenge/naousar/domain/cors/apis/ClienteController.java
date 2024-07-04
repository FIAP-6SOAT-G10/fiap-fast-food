//package br.com.fiap.techchallenge.naousar.apis;
//
//import br.com.fiap.techchallenge.naousar.adapters.cliente.GetClienteAdapter;
//import br.com.fiap.techchallenge.naousar.adapters.cliente.PatchClienteAdapter;
//import br.com.fiap.techchallenge.naousar.adapters.cliente.PostClienteAdapter;
//import br.com.fiap.techchallenge.naousar.adapters.cliente.PutClienteAdapter;
//import br.com.fiap.techchallenge.domain.ErrorsResponse;
//import br.com.fiap.techchallenge.infra.mapper.cliente.ClienteMapper;
//import br.com.fiap.techchallenge.naousar.domain.usecases.cliente.GetClienteUseCase;
//import br.com.fiap.techchallenge.naousar.domain.usecases.cliente.PatchClienteUseCase;
//import br.com.fiap.techchallenge.naousar.domain.usecases.cliente.PostClienteUseCase;
//import br.com.fiap.techchallenge.naousar.domain.usecases.cliente.PutClienteUseCase;
//import br.com.fiap.techchallenge.naousar.domain.valueobjects.ClienteDTO;
//import br.com.fiap.techchallenge.infra.persistence.ClienteRepository;
//import br.com.fiap.techchallenge.naousar.ports.cliente.GetClienteOutboundPort;
//import br.com.fiap.techchallenge.naousar.ports.cliente.PatchClienteOutboundPort;
//import br.com.fiap.techchallenge.naousar.ports.cliente.PostClienteInboundPort;
//import br.com.fiap.techchallenge.naousar.ports.cliente.PutClienteOutboundPort;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@Slf4j
//@RestController
//@Tag(name = "Clientes", description = "Conjunto de operações que podem ser realizadas no contexto de clientes.")
//@RequestMapping("/clientes")
//@RequiredArgsConstructor
//public class ClienteController {
//
//    private final ClienteRepository clienteRepository;
//    private final ClienteMapper clienteMapper;
//
//    @Operation(summary = "Cadastrar Cliente", description = "Esta operação consiste em criar um novo cliente")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created", content =
//                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content =
//                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))}),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
//                    {@Content(mediaType = "application/json", schema =
//                    @Schema(implementation = ErrorsResponse.class))})})
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<Void> cadastrar(@Valid @RequestBody ClienteDTO clienteRequest) {
//        log.info("cadastrar um cliente");
//        PostClienteInboundPort useCase = new PostClienteUseCase(new PostClienteAdapter(clienteRepository, clienteMapper));
//        ClienteDTO clienteDTO = useCase.salvarCliente(clienteRequest);
//        if (clienteDTO == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @Operation(summary = "Listar Clientes", description = "Está operação consiste em retornar todos os clientes cadastrados paginados por página e tamanho")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Found", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
//            }),
//            @ApiResponse(responseCode = "204", description = "Not Found", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
//            })
//    })
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<List<ClienteDTO>> listarTodosClientes(@RequestParam Integer page,
//                                                                @RequestParam Integer size,
//                                                                @RequestParam(required = false) String email,
//                                                                @RequestParam(required = false) String cpf
//    ) {
//        GetClienteOutboundPort getClienteAdapter = new GetClienteAdapter(clienteRepository, clienteMapper);
//        GetClienteUseCase getClienteUseCase = new GetClienteUseCase(getClienteAdapter);
//        List<ClienteDTO> clientes = getClienteUseCase.listarClientes(page, size, email, cpf);
//        if (clientes.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(clientes);
//    }
//
//    @Operation(summary = "Atualizar Dados do Cliente", description = "Está operação consiste em atualizar os dados do cliente cadastrado")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Updated", content = {
//                    @Content(mediaType = "application/json")
//            }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
//            }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = {
//                    @Content(mediaType = "application/json", schema =
//                    @Schema(implementation = ErrorsResponse.class))
//                    }
//            ),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
//                    @Content(mediaType = "application/json", schema =
//                    @Schema(implementation = ErrorsResponse.class))
//            })
//    })
//    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<ClienteDTO> atualizarDadosCliente(@RequestBody ClienteDTO clienteDTO
//    ) {
//        log.info("Atualizando cliente.");
//        PatchClienteOutboundPort patchClienteAdapter = new PatchClienteAdapter(clienteRepository, clienteMapper);
//        PatchClienteUseCase patchClienteUseCase = new PatchClienteUseCase(patchClienteAdapter);
//        ClienteDTO cliente = patchClienteUseCase.atualizarClientes(clienteDTO);
//        if (cliente == null || cliente.getCpf().isEmpty()) {
//            log.error("Cliente não encontrado.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        log.info("Cliente atualizado com sucesso.");
//        return ResponseEntity.status(HttpStatus.OK).body(cliente);
//    }
//
//    @Operation(summary = "Atualizar Cliente", description = "Está operação consiste em atualizar o cliente cadastrado")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Updated", content = {
//                    @Content(mediaType = "application/json")
//            }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
//            }),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = {
//                    @Content(mediaType = "application/json", schema =
//                    @Schema(implementation = ErrorsResponse.class))
//                    }
//            ),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
//                    @Content(mediaType = "application/json", schema =
//                    @Schema(implementation = ErrorsResponse.class))
//            })
//    })
//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @CrossOrigin(origins = "*", maxAge = 3600)
//    public ResponseEntity<ClienteDTO> atualizarCliente(@RequestBody ClienteDTO clienteDTO
//    ) {
//        log.info("Atualizando cliente.");
//        PutClienteOutboundPort putClienteAdapter = new PutClienteAdapter(clienteRepository, clienteMapper);
//        PutClienteUseCase putClienteUseCase = new PutClienteUseCase(putClienteAdapter);
//        ClienteDTO cliente = putClienteUseCase.atualizarClientes(clienteDTO);
//        if (cliente == null || cliente.getCpf().isEmpty()) {
//            log.error("Cliente não encontrado.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        log.info("Cliente atualizado com sucesso.");
//        return ResponseEntity.status(HttpStatus.OK).body(cliente);
//    }
//
//}
