package com.fourcamp.linkbank.controller;

import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Address;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.service.RegisterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private RegisterService registerService;
    private Client client;

    @BeforeEach
    void Setup() {
        MockitoAnnotations.openMocks(this);
        Address address = new Address();
        address.setCep("82920020");
        this.client = new Client(1015182022L, "03969774993", "93363574", "Joao Soares Barcelos", "12/05/1982", 15000.00, "05510889", "joao@barcelos.com.br", "engenheiro", address, "(51)98765-4321");
    }

    @Test
    void DeveRetornarInvalidInputExceptionSeAlgumDadoForNullNoRegister() {
        client.setPassword(null);
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> registerController.register(client));
        Assertions.assertEquals("Dados invalidos", exception.getMessage());
    }

    @Test
    void DeveRetornarCriadoNoRegister() {
        when(registerService.register(client)).thenReturn("Criado");
        String retorno = registerController.register(client);
        Assertions.assertEquals("Criado", retorno);
    }

    @Test
    void DeveRetornarDadosIncompletosNoUpdate() {
        client.setPassword(null);
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> registerController.update(client));
        Assertions.assertEquals("Dados incompletos", exception.getMessage());
    }

    @Test
    void DeveRetornarAceitoNoUpdate() {
        when(registerService.update(client)).thenReturn("Aceito");
        String retorno = registerController.update(client);
        Assertions.assertEquals("Aceito", retorno);

    }


}
