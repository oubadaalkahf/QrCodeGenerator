package emv.qrcodegenerator.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import emv.qrcodegenerator.dto.RequestDto;
import emv.qrcodegenerator.entities.QrCode;

@Service
public class QrServices {

	private static final String CIPHER_ALGORITHM = "AES/CBC/ISO10126PADDING";
	static byte[] iv = hexStringToByteArray("48E53E0639A76C5A5E0C5BC9E3A91538");
	static String SECRET_KEY_1 = "B67C1EA886E95E689A1BB3DBAD065C16";

	public QrCode generate(RequestDto request)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, ParseException {
		QrCode qrCode = new QrCode();
		qrCode.setText5303("5303" + request.getTransaction_currency());
		qrCode.setSubtext0624("0624" + encrypt(request.getPaid_entity_reference()));
		qrCode.setSubtext0713("0713" + maskedPhone(request.getPaid_entity_reference()));
		
		System.out.println(request.getPurpose_of_transaction().length());
		if(request.getPurpose_of_transaction().length()<10)
		{
			qrCode.setSubtext0819("080"+ request.getPurpose_of_transaction().length()+ request.getPurpose_of_transaction());
		} else {
			qrCode.setSubtext0819("08"+ request.getPurpose_of_transaction().length()+ request.getPurpose_of_transaction());

		}
		
		System.out.println(qrCode.getSubtext0819().length());
		if(qrCode.getSubtext0819().length()<10)
		{
			qrCode.setText6223("620"+qrCode.getSubtext0819().length());
		}else {
			qrCode.setText6223("62"+qrCode.getSubtext0819().length());
		}
		
		qrCode.setSubtext0101("0101" + request.getOperation_type());
		qrCode.setSubText0103("01"+verifylenght(request.getFinancial_institution_code())+ request.getFinancial_institution_code());

		System.out.println(qrCode.getSubText0103());
		if (request.getPoint_of_initiation_method().equals("dynamic")) {
			qrCode.setText0102("0102" + "12");
			qrCode.setText5405("54" +verifylenght(request.getTransaction_amount())+ request.getTransaction_amount());

		} else if (request.getPoint_of_initiation_method().equals("static")) {
			qrCode.setText0102("0102" + "11");

			if (request.getTransaction_amount() != null) {
				qrCode.setText5405("54" +verifylenght(request.getTransaction_amount())+request.getTransaction_amount());
			}
		}
		 if(request.getTransaction_type().toLowerCase().equals("paiement commercant")|| request.getTransaction_type().toLowerCase().equals("paiement commerçant")) 
		 {
/*
 * private String text5204;
	private String text5802;
	private String text5922;
	private String text6004;			 
 */
			 qrCode.setText5204("52"+verifylenght(request.getMerchant_category_code())+request.getMerchant_category_code());
			 qrCode.setText5802("5802"+request.getCountry_code());
			 qrCode.setText5922("59"+verifylenght(request.getMerchant_name())+request.getMerchant_name());
			 	
			 qrCode.setText6004("60"+verifylenght(request.getMerchant_city())+request.getMerchant_city());
		 }
//		 int lenghtText2698 = qrCode.getSubtext0032().length()
//				 			+qrCode.getSubText0103().length()
//				 			+qrCode.getSubtext0501().length()
//				 			+qrCode.getSubtext0624().length()
//				 			+qrCode.getSubtext0713().length();
//		 qrCode.setText2698(lenghtText2698+"");
		return qrCode;

	}

	public static String encrypt(String message)
			throws ParseException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Key secretKey = parseSecretKey(SECRET_KEY_1);

		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

		byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		byte[] encryptedByteValue = new Base64().encode(encryptedMessage);

		String encryptedValue = new String(encryptedByteValue);
		System.out.println("------------------ENCRYPTE MESSAGE ---------------");
		System.out.println(encryptedValue);
		return encryptedValue;
	}

	public static String decrypt(String data) throws ParseException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {

		byte[] encryptedData = new Base64().decode(data);
		Cipher c = null;
		// Cipher decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
		try {
			c = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Key k = parseSecretKey(SECRET_KEY_1);
		try {
			c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] decrypted = null;
		try {
			decrypted = c.doFinal(encryptedData);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(decrypted);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] stringToByteArray(String hexaString) throws ParseException {
		// the padding shouldn't be used, so a random one was chosen
		return stringToByteArray(hexaString, hexaString.length() / 2, (byte) 0xFF);
	}
	public static String verifylenght(String str) {
		if (str.length()<10) {
			return "0" +str.length();
		}
		else return ""+str.length();
	}

	public static SecretKey parseSecretKey(String secretKey) throws ParseException {
		return new SecretKeySpec(stringToByteArray(secretKey), "AES");
	}

	public static byte[] stringToByteArray(String hexaString, int resultArraySize, byte padding) throws ParseException {
		final int HEXA_RADIX = 16;
		int length = hexaString.length();
		if (length % 2 == 0) {
			length /= 2;
			if (length <= resultArraySize) {
				byte[] numbers = new byte[resultArraySize];
				int i;
				// filling the array
				for (i = 0; i < length; i++) {
					// the following line will trigger a NumberFormatException if str contains a non
					// 0-9 A-F character
					try {
						int j = Integer.parseInt(hexaString.substring(2 * i, 2 * i + 2), HEXA_RADIX);
						numbers[i] = (byte) (j & 0xFF);
					} catch (NumberFormatException ex) {
						throw new ParseException(ex.getMessage(), i);
					}
				}
				// padding
				for (; i < resultArraySize; i++) {
					numbers[i] = padding;
				}
				return numbers;
			} else {
				throw new ParseException(
						"the resulting array size is too big compared to the min size specified in the parameters", 0);
			}
		} else {
			throw new ParseException("string length must be even", 0);
		}
	}

	public String maskedPhone(String phone) {
		StringBuffer phonee = new StringBuffer(phone);
		phonee = phonee.replace(5, 11, "######");
		return phonee.toString();
	}

	public static String CRC16CCITT(String qr) throws UnsupportedEncodingException {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		byte[] bytes = qr.getBytes("ASCII");

		for (byte b : bytes) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;
		return padLeftZeros(Integer.toHexString(crc).toUpperCase(), 4);
	}

	public static String padLeftZeros(String inputString, int length) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append('0');
		}
		sb.append(inputString);

		return sb.toString();
	}

	public String generateCRC(String qrText) throws UnsupportedEncodingException {

		return CRC16CCITT(qrText);
	}

}