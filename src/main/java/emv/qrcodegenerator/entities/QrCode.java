package emv.qrcodegenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class QrCode {

	private String text000201 = "000201";
	private String text0102;
	
	private String text5204;
	private String text5802;
	private String text5922;
	private String text6004;
	
	private String text2698 = "2698";
	private String subtext0032 = "00325bb66a92d69c0ea742dd4f754590fa0a";
	private String subText0103 ;  
	private String subtext0201 = "02011";
	private String subtext0501="05010";
	private String subtext0624;
	private String subtext0713;

	private String text5303;

	private String text5405;

	private String text6223 ;
	private String subtext0819;

	private String text8062 = "8062";
	private String subtextt0032 = "003237b3a355b830b3bf0974d23608a6f162";
	private String subtext0101;
	private String subtext0401="04010";
	private String subtext0506="0506010003";
	private String subtext0602="060201";
	private String Text6304;
	
	public QrCode(String text000201, String text0102, String text2698, String subtext0032, String subtext0201,
			String subtext0624, String subtext0713, String text5303, String text5405, String text6223,
			String subtext0819, String text8062, String subtextt0032, String subtext0101) {
		super();
		this.text000201 = text000201;
		this.text0102 = text0102;
		this.text2698 = text2698;
		this.subtext0032 = subtext0032;
		this.subtext0201 = subtext0201;
		this.subtext0624 = subtext0624;
		this.subtext0713 = subtext0713;
		this.text5303 = text5303;
		this.text5405 = text5405;
		this.text6223 = text6223;
		this.subtext0819 = subtext0819;
		this.text8062 = text8062;
		this.subtextt0032 = subtextt0032;
		this.subtext0101 = subtext0101;
	}

	

}
