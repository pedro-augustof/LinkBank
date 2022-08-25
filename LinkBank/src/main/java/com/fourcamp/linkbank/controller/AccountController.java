package com.fourcamp.linkbank.controller;

import java.util.List;

import com.fourcamp.linkbank.exceptions.InvalidInputException;
import com.fourcamp.linkbank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Extract;
import com.fourcamp.linkbank.model.TransactionPix;
import com.fourcamp.linkbank.model.TransactionTransfer;
import com.fourcamp.linkbank.service.AccountService;
import com.fourcamp.linkbank.service.ExtractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "account")
public class AccountController {
	@Autowired
	CardService cardService;

	@Autowired
	ExtractService extractService;

	@Autowired
	AccountService accountService;

	@GetMapping("/balance")
	public ResponseEntity<?> getBalance(@RequestBody Account account) {

			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("Id da conta não existe");
			}

		return new ResponseEntity<>(accountService.getBalance(account.getId()), HttpStatus.OK);

	}

	@GetMapping("/savingsbalance")
	public ResponseEntity<?> getSavingsAccountBalance(@RequestBody Account account) {

		try {
			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("Id Invalido");
			}
		} catch (Exception e) {
			throw new InvalidInputException("Id Invalido");
		}
		return new ResponseEntity<>(accountService.getSavingsAccountBalance(account.getId()), HttpStatus.OK);

	}

	@PutMapping("/deposit")
	public ResponseEntity<?> makeDeposit(@RequestBody Account account) {
		try {
			if (account.getNumber() == null || account.getBalance() == null) {
				throw new InvalidInputException("Não foi possivel realizar o deposito na conta");
			}
		} catch (Exception e) {
			throw new InvalidInputException("Não foi possivel realizar o deposito na conta");
		}
		return new ResponseEntity<>(accountService.deposit(account), HttpStatus.OK);
	}

	@PutMapping("/depositinsavingsaccount")
	public ResponseEntity<?> makeSavingsAccountDeposit(@RequestBody Account account) {
		try {
			if (account.getNumber() == null || account.getSavingsAccountBalance() == null) {
				throw new InvalidInputException("Não foi possivel realizar o deposito na conta");
			}
		} catch (Exception e) {
			throw new InvalidInputException("Não foi possivel realizar o deposito na conta");
		}
		return new ResponseEntity<>(accountService.depositInSavingsAccount(account), HttpStatus.OK);
	}

	@PutMapping("/transfer")
	public ResponseEntity<?> makeTransfer(@RequestBody TransactionTransfer transactionTransfer) {
		try {
			if (transactionTransfer.getId() == null || transactionTransfer.getValue() == null
					|| transactionTransfer.getReceiverAccount().getNumber() == null
					|| transactionTransfer.getReceiverAccount().getAgency() == null
					|| transactionTransfer.getReceiverAccount().getClient().getFullname() == null) {
				throw new InvalidInputException("Não foi possivel realizar a transferencia");
			}
		} catch (Exception e) {
			throw new InvalidInputException("Não foi possivel realizar a transferencia");
		}
		return new ResponseEntity<>(accountService.transfer(transactionTransfer), HttpStatus.OK);
	}

	@PutMapping("/paybill")
	public ResponseEntity<?> payBill(@RequestBody Account account) {
		try {
			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("ID da conta nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da conta nulo ou zero");
		}
		return new ResponseEntity<>(accountService.payBill(account), HttpStatus.OK);
	}

	@GetMapping("/extract")
	public ResponseEntity<?> getExtract(@RequestBody Account account) {
		try {
			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("Id da conta é nulo");
			}
		} catch (Exception e) {
			throw new InvalidInputException("Id da conta é nulo");
		}
		return new ResponseEntity<>(extractService.listById(account.getId()), HttpStatus.OK);
	}

	@PostMapping("/pix/email")
	public ResponseEntity<String> pixEmail(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getId() == 0) {
				throw new InvalidInputException("ID da transação Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.pixEmail(transactionPix), HttpStatus.OK);
	}

	@PutMapping("/pix/email")
	public ResponseEntity<String> transferPixEmail(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getId() == 0) {
				throw new InvalidInputException("ID da transação Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
		}
			return new ResponseEntity<>(accountService.transferPixEmail(transactionPix), HttpStatus.OK);
	}




	@DeleteMapping("/pix/email")
	public ResponseEntity<String> deletepixEmail(@RequestBody Account account) {
		try {
			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.deletePixEmail(account), HttpStatus.OK);
	}


	@PostMapping("/pix/cpf")
	public ResponseEntity<String> pixCpf(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getId() == 0) {
				throw new InvalidInputException("ID da transação Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.pixCpf(transactionPix), HttpStatus.OK);
	}


    @PutMapping("/pix/cpf")
    public ResponseEntity<String> transferPixCpf(@RequestBody TransactionPix transactionPix) {
        try {
            if (transactionPix.getId() == null || transactionPix.getId() == 0) {
                throw new InvalidInputException("ID da transação Pix nulo ou zero");
            }

        } catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
        }
		return new ResponseEntity<>(accountService.transferPixCpf(transactionPix), HttpStatus.OK);
    }

	@DeleteMapping("/pix/cpf")
	public ResponseEntity<String> deletePixCpf(@RequestBody Account account) {
		try {
			if (account.getId() == null || account.getId() == 0) {
				throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
			}

		} catch (Exception e) {
			throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.deletePixCpf(account), HttpStatus.OK);
	}


	@PostMapping("/pix/cellphone")
	public ResponseEntity<String> pixCellphone(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getId() == 0) {
				throw new InvalidInputException("ID da transação Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.pixCellphone(transactionPix), HttpStatus.OK);
	}


	@PutMapping("/pix/cellphone")
	public ResponseEntity<String> transferPixCellphone(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getId() == 0) {
				throw new InvalidInputException("ID da transação Pix nulo ou zero");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da transação Pix nulo ou zero");
		}
		return new ResponseEntity<>(accountService.transferPixCellphone(transactionPix), HttpStatus.OK);
	}


    @DeleteMapping("/pix/cellphone")
    public ResponseEntity<String> deletePixCellPhone(@RequestBody Account account) {

        try {
            if (account.getId() == null || account.getId() == 0) {
               throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
            }

        } catch (Exception e) {
			throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou zero");
        }
		return new ResponseEntity<>(accountService.deletePixCellPhone(account), HttpStatus.OK);
    }


	@PutMapping("/pix/cnpj")
	public ResponseEntity<String> transferPixCnpj(@RequestBody TransactionPix transactionPix) {
		try {
			if (transactionPix.getId() == null || transactionPix.getPixKey() == null
					|| transactionPix.getValue() == null) {
				throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou dados do Pix nulos");
			}
		} catch (Exception e) {
			throw new InvalidInputException("ID da conta responsável pela chave Pix nulo ou dados do Pix nulos");
		}
		return new ResponseEntity<>(accountService.transferPixCnpj(transactionPix), HttpStatus.OK);
	}

}
