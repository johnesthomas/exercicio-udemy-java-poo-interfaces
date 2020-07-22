package services;

import java.util.Calendar;
import java.util.Date;

import entities.Contract;
import entities.Installment;

public class ContractService {

	private OnlinePaymentService onlinePaymentService;

	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}

	public void processContract(Contract contract, int months) {
		// basicQuota = valor total divido pelo numero de meses
		double basicQuota = contract.getTotalValue() / months;
		for (int i = 1; i <= months; i++) {
			// updateQuota = valor atualizado com os juros de acordo com a quant. de meses
			double updateQuota = basicQuota + onlinePaymentService.interest(basicQuota, i);
			// fullQuota = valor final com os juros de pagamento
			double fullQuota = updateQuota + onlinePaymentService.paymentFee(updateQuota);
			// dueDate = adicionar N(i) mês(es) ao mês atual 
			Date dueDate = addMonth(contract.getDate(), i);
			// 
			contract.getInstallments().add(new Installment(dueDate, fullQuota));
		}
	}

	// metodo para acrescentar um mês a parcela
	private Date addMonth(Date date, int N) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, N);
		return calendar.getTime();
	}
}