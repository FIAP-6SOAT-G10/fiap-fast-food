//package br.com.fiap.techchallenge.adapters;
//
//import br.com.fiap.techchallenge.naousar.adapters.cliente.PutClienteAdapter;
//import br.com.fiap.techchallenge.infra.persistence.entities.Cliente;
//import br.com.fiap.techchallenge.infra.mapper.cliente.ClienteMapper;
//import br.com.fiap.techchallenge.naousar.domain.valueobjects.ClienteDTO;
//import br.com.fiap.techchallenge.naousar.infra.exception.ClienteException;
//import br.com.fiap.techchallenge.infra.persistence.ClienteRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class PutClienteAdapterTest {
//
//    @Mock
//    private ClienteRepository clienteRepository;
//
//    @Mock
//    private ClienteMapper mapper;
//
//    private PutClienteAdapter putClienteAdapter;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        putClienteAdapter = new PutClienteAdapter(clienteRepository, mapper);
//    }
//
//    @Test
//    void shouldUpdateClienteWhenValidInputProvided() {
//        ClienteDTO clienteDTO = new ClienteDTO(1l,"12345678901", "Nomelindo de Jesus", "email@email.com");
//        Cliente existingCliente = new Cliente();
//        Cliente updatedCliente = new Cliente();
//
//        when(clienteRepository.findByCpf(clienteDTO.getCpf())).thenReturn(Optional.of(existingCliente));
//        when(clienteRepository.saveAndFlush(any(Cliente.class))).thenReturn(updatedCliente);
//        when(mapper.toDTO(updatedCliente)).thenReturn(clienteDTO);
//
//        ClienteDTO result = putClienteAdapter.atualizarClientes(clienteDTO);
//
//        assertEquals(clienteDTO, result);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenClienteNotFound() {
//        ClienteDTO clienteDTO = new ClienteDTO();
//
//        when(clienteRepository.findByCpf(clienteDTO.getCpf())).thenReturn(Optional.empty());
//
//        assertThrows(ClienteException.class, () -> putClienteAdapter.atualizarClientes(clienteDTO));
//    }
//}