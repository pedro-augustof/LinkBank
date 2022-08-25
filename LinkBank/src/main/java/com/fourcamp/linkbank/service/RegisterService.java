package com.fourcamp.linkbank.service;
import com.fourcamp.linkbank.exceptions.AlreadyExistsException;
import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.model.Pix;
import com.fourcamp.linkbank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
@Service
public class RegisterService {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	CardService cardService;
	@Autowired
	PixRepository pixRepository;
	public String register(Client client) {
		LocalDate localDate = LocalDate.now();
		Account account = new Account();
		String number = createDigit(5) + "-" + createDigit(1);
		Boolean verify = clientRepository.existsByCpf(client.getCpf());
		if (Boolean.TRUE.equals(verify)) {
			throw new AlreadyExistsException("Cliente já cadastrado!");
		} else {
			if (Boolean.FALSE.equals(validaSenha(client))) {
				throw new InvalidInputException("Senha inválida!");
			}

			account.setClient(client);
			account.setNumber(number);
			account.setAgency("0001");
			account.setBalance(0.0);
			account.setData(localDate);
			addressRepository.save(client.getAddress());
			clientRepository.save(client);
			Card card = cardService.createCard(account);
			account.setCard(card);
			account.setPix(new Pix());
			account.setData(localDate);
			account.setSavingsAccountRate(0.00035);
			account.setBalance(0.00);
			account.setSavingsAccountBalance(0.00);


			pixRepository.save(account.getPix());
			accountRepository.save(account);
			return "Conta criada com sucesso!";
		}
	}

	public String update(Client client) {
		Boolean verify = clientRepository.existsByCpf(client.getCpf());
		if (Boolean.FALSE.equals(verify)) {
			throw new InvalidInputException("Cpf inválido!");
		}else {
			if (Boolean.FALSE.equals(validaSenha(client))) {
				throw new InvalidInputException("Senha inválida!");
			}

			addressRepository.save(client.getAddress());
			clientRepository.save(client);

			return "Dados atualizados com sucesso!";
		}
	}

	public String createDigit(int quantity) {
		String digits = "";
		Random random = new Random();
		for (int x = 0; x < quantity; x++) {
			Integer number = random.nextInt(0, 10);
			digits += number.toString();
		}
		return digits;
	}
	public Boolean validaSenha(Client client){
		if(client.getPassword().length()<8){
			return false;
		}
		return true;
	}

}