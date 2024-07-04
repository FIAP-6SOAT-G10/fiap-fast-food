package br.com.fiap.techchallenge.naousar.domain.usecases.cliente;

import br.com.fiap.techchallenge.naousar.domain.valueobjects.ClienteDTO;
import br.com.fiap.techchallenge.naousar.ports.cliente.GetClienteInboundPort;
import br.com.fiap.techchallenge.naousar.ports.cliente.GetClienteOutboundPort;

import java.util.List;

public class GetClienteUseCase implements GetClienteInboundPort {

    private final GetClienteOutboundPort port;

    public GetClienteUseCase(GetClienteOutboundPort port) {
        this.port = port;
    }

    @Override
    public List<ClienteDTO> listarClientes(Integer page, Integer size, String email, String cpf) {
        return this.port.listarClientes(page, size, email, cpf);
    }
}
