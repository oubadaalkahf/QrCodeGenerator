package emv.qrcodegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
	private String transaction_type;
	private String point_of_initiation_method;
	private String paid_entity_reference;
	private String transaction_currency;
	private String transaction_amount;
	private String purpose_of_transaction;
	private String operation_type;
	private String financial_institution_code;
	
	private String merchant_category_code;
	private String country_code;
	private String merchant_name;
	private String merchant_city;
}
