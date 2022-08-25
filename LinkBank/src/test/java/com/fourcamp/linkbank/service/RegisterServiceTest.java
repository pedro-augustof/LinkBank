package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.exceptions.AlreadyExistsException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Address;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.repository.AccountRepository;
import com.fourcamp.linkbank.repository.AddressRepository;
import com.fourcamp.linkbank.repository.ClientRepository;
import com.fourcamp.linkbank.repository.PixRepository;
import com.fourcamp.linkbank.utils.Validations;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class RegisterServiceTest {

	@InjectMocks
	private RegisterService service;

	@Mock
	ClientRepository clientRepository;
	@Mock
	AddressRepository addressRepository;
    @Mock
    CardService cardService;
    @Mock
    PixRepository pixRepository;
    @Mock
    AccountRepository accountRepository;

    @Mock
    RegisterService serviceMock;

    private Client client;
    private Address address;
    private Card card;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		createAddress();
		createClient();
	}

	private void createClient() {
		this.client = new Client(1L, "05833506077", "383792629", "Pedro Henrique", "06/10/1999", 1800.0, "12345678",
				"pedro@foursys.com", "programador", address, "42998028575");
	}

	private void createAddress() {
		this.address = new Address(1L, "Rua X", "809", "8450000", "Bairro X", "Cidade X", "Estado X", "405");

    }
    private void createCard() {
        this.card = new Card(1L, "5555 5555 5555 5555", "MASTERCARD", 123456, true, 30.00, 6000.00, "PREMIUM", 20000.00, 155);
    }

    @Test
    void DeveJogarExceçãoQuandoClienteComMesmoCpfJaExistir() {
        when(clientRepository.existsByCpf(client.getCpf())).thenReturn(true);
        AlreadyExistsException alreadyExistsException = Assert.assertThrows(AlreadyExistsException.class, () -> service.register(client));
        Assertions.assertEquals("Cliente já cadastrado!", alreadyExistsException.getMessage());
    }

    @Test
    void SenhaMenorQueOito() {
        when(clientRepository.existsByCpf(client.getCpf())).thenReturn(false);
        client.setPassword("1234");
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class , () -> service.register(client));
        Assertions.assertEquals("Senha inválida!", invalidInputException.getMessage());
    }

    @Test
    void registerPassAll() {
        when(clientRepository.existsByCpf(client.getCpf())).thenReturn(false);
        Assertions.assertEquals("Conta criada com sucesso!", service.register(client));
    }

    @Test
    void SenhaMenorQueOitoUpdate() {
        when(clientRepository.existsByCpf(client.getCpf())).thenReturn(true);
        client.setPassword("1234");
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class , () -> service.update(client));
        Assertions.assertEquals("Senha inválida!", invalidInputException.getMessage());
    }

    @Test
    void exceçãoCpfDiferente() {
        when(clientRepository.existsByCpf("05833506077")).thenReturn(false);
        InvalidInputException invalidInputException = Assert.assertThrows(InvalidInputException.class, () -> service.update(client));
        Assertions.assertEquals("Cpf inválido!", invalidInputException.getMessage());

    }

    @Test
    void UpdateCliente() {
        when(clientRepository.existsByCpf("05833506077")).thenReturn(true);
        Assertions.assertEquals("Dados atualizados com sucesso!", service.update(client));
    }

    @Test
    void RetornarQuantidadeDigitosSolicitada(){
        int quantity = 10;
        Assertions.assertEquals(10 , service.createDigit(quantity).length());
    }
}
