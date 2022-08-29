package emv.qrcodegenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class QrResponse {

	private String transaction_type;
	private String paiement_type;
	private String country_code;
	private String merchand_name;
	private String merchand_city;
	private String merchand_phone_number;
	private String transaction_currency;
	private String transaction_amount;
	private String qrCodeVersion;
	
}
