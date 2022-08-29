package emv.qrcodegenerator.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.PostRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import emv.qrcodegenerator.dto.QrText;
import emv.qrcodegenerator.dto.RequestDto;
import emv.qrcodegenerator.entities.FrmQrCode;
import emv.qrcodegenerator.entities.QrCode;
import emv.qrcodegenerator.entities.QrResponse;
import emv.qrcodegenerator.services.QrServices;
import emv.qrcodegenerator.services.VerifyQrCodeService;

@RestController
@RequestMapping("/")
public class QrCodeController {

	
	@Autowired
	private QrServices qrServices;
	
	private static String MADCurrencyCodeIso = "504";
	private static String EUROCurrencyCodeIso = "978";
	private static String USDCurrencyCodeIso = "840";
	
	@Autowired
	private VerifyQrCodeService qrCodeService;
	

	@PostMapping(path="/transferp2p")
	public String transferp2p(@RequestBody RequestDto request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, ParseException, WriterException, IOException {
		String qrText="" ; 		
		QrCode qrCode = qrServices.generate(request);
		if(request.getTransaction_type().toLowerCase().equals("transfer p2p"))
		{
			
			qrText = qrCode.getText000201()+qrCode.getText0102()+qrCode.getText2698()
					+qrCode.getSubtext0032()+qrCode.getSubText0103()
					+qrCode.getSubtext0201()+qrCode.getSubtext0501()+qrCode.getSubtext0624()
					+qrCode.getSubtext0713()+qrCode.getText5303()+qrCode.getText5405()
					+qrCode.getText6223()+qrCode.getSubtext0819()+qrCode.getText8062()
					+qrCode.getSubtextt0032()+qrCode.getSubtext0101()
					+qrCode.getSubtext0401()+qrCode.getSubtext0506()+qrCode.getSubtext0602();
			
		}
		else if(request.getTransaction_type().toLowerCase().equals("paiement commercant")|| request.getTransaction_type().toLowerCase().equals("paiement commerçant")) 
		{
			
			qrText =  qrCode.getText000201()+qrCode.getText0102()+qrCode.getText2698()
					+qrCode.getSubtext0032()+qrCode.getSubText0103()
					+qrCode.getSubtext0201()+qrCode.getSubtext0501()+qrCode.getSubtext0624()
					+qrCode.getSubtext0713()+qrCode.getText5204()
					
					+qrCode.getText5303()+qrCode.getText5405()+
					qrCode.getText5802()+qrCode.getText5922()+qrCode.getText6004()
					
					+qrCode.getText8062()
					+qrCode.getSubtextt0032()+qrCode.getSubtext0101()
					+qrCode.getSubtext0401()+qrCode.getSubtext0506()+qrCode.getSubtext0602();
		}
			
			if(qrText.contains("null"))
			{
				qrText  = qrText.replaceAll("null", "");
			}
			
		qrCode.setText6304(qrServices.generateCRC(qrText+"6304"));
		qrText = qrText +"6304"+qrCode.getText6304();
			return qrText;
	}  
	
	
	
	
    @PostMapping(path = "/getqrcode", produces = MediaType.IMAGE_PNG_VALUE)
	public BufferedImage generateQRCodeImage(@RequestBody QrText qrText) throws Exception {
        //QRcode generator logic
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrText.getQrText(), BarcodeFormat.QR_CODE, 250, 250);
			
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

    @PostMapping("/verify")
	public QrResponse verifyQrCode(@RequestBody QrText qrText)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, ParseException {
		QrResponse qrResponse = new QrResponse();
		FrmQrCode frmQrCode = qrCodeService.parseEmvcoQR(qrText.getQrText());

		qrResponse.setCountry_code(frmQrCode.getText58());

		qrResponse.setMerchand_city(frmQrCode.getText60());
		qrResponse.setMerchand_phone_number(frmQrCode.getText2606D());
		qrResponse.setMerchand_name(frmQrCode.getText59());

		qrResponse.setTransaction_type(frmQrCode.getLblPoI());

		if (frmQrCode.getText53().equals(MADCurrencyCodeIso)) {
			
			qrResponse.setTransaction_currency("MAD");
			
		} else if (frmQrCode.getText53().equals(EUROCurrencyCodeIso)) {
			
			qrResponse.setTransaction_currency("EURO");
			
		} else if (frmQrCode.getText53().equals(USDCurrencyCodeIso)) {
			
			qrResponse.setTransaction_currency("USD");
			
		}

		qrResponse.setTransaction_amount(frmQrCode.getText54());

		qrResponse.setPaiement_type(frmQrCode.getLblTo());
		qrResponse.setQrCodeVersion(frmQrCode.getText8005());

		return qrResponse;

	}
	


}
