package emv.qrcodegenerator.services;

import java.awt.Color;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import emv.qrcodegenerator.entities.EmvObject;
import emv.qrcodegenerator.entities.FrmQrCode;

@Service
public class VerifyQrCodeService {
	private static final int Length = 2;
    private static final String CIPHER_ALGORITHM = "AES/CBC/ISO10126PADDING";
    private static final String SYMMETRIC_ALGORITHM = "AES";
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    FrmQrCode frmQrCode = new FrmQrCode();
	 public FrmQrCode parseEmvcoQR(String str) throws NoSuchAlgorithmException, NoSuchPaddingException, ParseException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	        Map <Integer, EmvObject> result = new HashMap<Integer, EmvObject>();
	        
	        int i = 0;
	        
			
	        while (i < str.length()) {

	            int tagInd = i + Length;

	            int tag = Integer.parseInt(str.substring(i, tagInd));
	            int lengthValue = Integer.parseInt(str.substring(tagInd, tagInd + Length ));

	            EmvObject emvObject = new EmvObject();
	            emvObject.setLength(lengthValue);
	            
	            if ( tag==63){
	            	if (tagInd + Length + lengthValue > str.length()){
						System.out.println("Taille CRC Erronée ... !");
						  break;
	            	}
	            }
	            emvObject.setValues(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	            
	            
	           
	            if ( tag==0){
	            	frmQrCode.setTextPld(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	            }
	            if ( tag==1){
	            	frmQrCode.setTextPoI(str.substring(tagInd + Length , tagInd + Length + lengthValue)); 
	                switch(frmQrCode.getTextPoI()){    
		            case "11":    
		            	frmQrCode.setLblPoI("Statique");     
		             break;  //optional  
		            case "12":    
		            
		            	frmQrCode.setLblPoI("Dynamique");    
		             break;  //optional  
		         
		            }  
	            }
	            if ( tag==26){
	            frmQrCode.setText26(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
	            	getParsingSubValue(str.substring(tagInd + Length , tagInd + Length + lengthValue),26);
	            }
	            
	            if (tag==53){
	            frmQrCode.setText53(str.substring(tagInd + Length , tagInd + Length + lengthValue));
		           }	            
	            if (tag==54){
	            
	            	 frmQrCode.setText54(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           }
	            if (tag==52){
	           
	            	 frmQrCode.setText52(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           }	            
	            if (tag==58){
	            
	            	 frmQrCode.setText58(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           }
	            if (tag==59){
	        
	            	 frmQrCode.setText59(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           }
	            if (tag==60){
	          
	            	 frmQrCode.setText60(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           }	            
	            if (tag==62){
	            	frmQrCode.setText62(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           
		           	 getParsingSubValue(str.substring(tagInd + Length , tagInd + Length + lengthValue),62);
		           }

	            if (tag==64){
	            	 frmQrCode.setText64(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	           	 getParsingSubValue(str.substring(tagInd + Length , tagInd + Length + lengthValue),64);
	           }
	            if (tag==80){
	            	 frmQrCode.setText80(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	              	 getParsingSubValue(str.substring(tagInd + Length , tagInd + Length + lengthValue),80);
	              }
	            if (tag==63){
	            
	            	 frmQrCode.setText63(str.substring(tagInd + Length , tagInd + Length + lengthValue).toUpperCase());
	            	 frmQrCode.setCrc(str.substring(tagInd + Length , tagInd + Length + lengthValue).toUpperCase());
		           }
	       
	            i = tagInd + Length + lengthValue;

	        }

	        return frmQrCode;
	    }
	    public void parseEmvcoQR_Sub(String str,int tagP) throws NoSuchAlgorithmException, NoSuchPaddingException, ParseException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	        
	        int i = 0;
	        Cipher decryptCipher = Cipher.getInstance(CIPHER_ALGORITHM);
	        byte[] iv = hexStringToByteArray("48E53E0639A76C5A5E0C5BC9E3A91538");  //48E53E0639A76C5A5E0C5BC9E3A91538

	        while (i < str.length()) {

	            int tagInd = i + Length;

	            int Stag = Integer.parseInt(str.substring(i, tagInd));
	            int lengthValue = Integer.parseInt(str.substring(tagInd, tagInd + Length ));

	            EmvObject emvObject = new EmvObject();
	            emvObject.setLength(lengthValue);
	            emvObject.setValues(str.substring(tagInd + Length , tagInd + Length + lengthValue));

	          if (tagP == 26){
	            switch(Stag){    
	            case 0:    
	           frmQrCode.setText2600(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	             if (! frmQrCode.getText2600().equals("5bb66a92d69c0ea742dd4f754590fa0a"))
	           System.out.println("text2600 not equals");
	             else
	            	 System.out.println("text2600  equals");	 
	             break;  //optional  
	            case 2:    
	            frmQrCode.setText2602(str.substring(tagInd + Length , tagInd + Length + lengthValue));
	                switch(frmQrCode.getText2602()){    
		            case "0":    
		            	frmQrCode.setLblFc("Sans Chiffrement") ;	    
		             break;  //optional  
		            case "1":    
		            	
		            	frmQrCode.setLblFc("Avec Chiffrement") ;	
		             break;  //optional  
		         
		            }  
	             break;  //optional  
	            case 5:    
	            frmQrCode.setText2605(str.substring(tagInd + Length , tagInd + Length + lengthValue)); 
		             break;  //optional 
	            case 6:    
	            	
	            	if(frmQrCode.getText2602().equals("0")){
	            		frmQrCode.setText2602(str.substring(tagInd + Length , tagInd + Length + lengthValue))	;
	            		byte[] byteArray = Base64.decodeBase64(str.substring(tagInd + Length , tagInd + Length + lengthValue).getBytes());
	            		String decodedString = new String(byteArray);
	            		frmQrCode.setText2606D(decodedString);
		            	
	            	}else{
	            		String b64CipherText = str.substring(tagInd + Length , tagInd + Length + lengthValue);  
	                byte[] cipherText = Base64.decodeBase64(b64CipherText);
	                
	                String SECRET_KEY_1 = "B67C1EA886E95E689A1BB3DBAD065C16" ;//"B67C1EA886E95E689A1BB3DBAD065C16";
	                Key secretKey = parseSecretKey(SECRET_KEY_1);
	                
	                decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
	                
	               byte[] recoveredMessage = decryptCipher.doFinal(cipherText);

	            	frmQrCode.setText2606(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
	            	frmQrCode.setText2606D(new String(recoveredMessage, CHARSET));	 
	            		
	            	}
	            	
		             break;  //optional 
	            case 7:    
	            	frmQrCode.setText2607(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional 		             

	            }    
	           }
	            	
	          
	          if (tagP == 62){
		            switch(Stag){    
		            case 1:    
		             frmQrCode.setText6201(str.substring(tagInd + Length , tagInd + Length + lengthValue));	    
		             break;  //optional  
		            case 2:    
		            	frmQrCode.setText6202(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional  
		            case 3:    
		            	frmQrCode.setText6203(str.substring(tagInd + Length , tagInd + Length + lengthValue));	   
			             break;  //optional 
		            case 4:    
		            	frmQrCode.setText6204(str.substring(tagInd + Length , tagInd + Length + lengthValue));	 
			             break;  //optional 
		            case 5:    
		            	frmQrCode.setText6205(str.substring(tagInd + Length , tagInd + Length + lengthValue));	 
			             break;  //optional 
		            case 6:    
		            	frmQrCode.setText6206(str.substring(tagInd + Length , tagInd + Length + lengthValue));	 
			             break;  //optional 			             
		            case 7:    
		            	frmQrCode.setText6207(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
			             break;  //optional 		             
		            case 8:    
		            	frmQrCode.setText6208(str.substring(tagInd + Length , tagInd + Length + lengthValue));	 
			             break;  //optional 
		            }    
		           }	          
	            	     	

	          if (tagP == 64){
	        	  frmQrCode.setLngPref(true);  
		            switch(Stag){    
		            case 1:    
		            	frmQrCode.setText6401(str.substring(tagInd + Length , tagInd + Length + lengthValue));	    
		             break;  //optional  
		            case 2:    
		            	frmQrCode.setText6402(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional  

		            }    
		           }	

	          if (tagP == 80){
		            switch(Stag){    
		            case 0:    
		            	frmQrCode.setText8000(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		                if (! frmQrCode.getText8000().equals("37b3a355b830b3bf0974d23608a6f162"))
		                	System.out.println("Text8000 not equals");
		   	             else
		   	            	System.out.println("Text8000  equals");
		             break;  //optional  
		            case 1:    
		            	frmQrCode.setText8001(str.substring(tagInd + Length , tagInd + Length + lengthValue));
		            	
			            switch(frmQrCode.getText8001()){    
			            case "0":    
			            	
			            	frmQrCode.setLblTo("Transfert P2P face à face");
			             break;  //optional  
			            case "1":    
			            	
			            	frmQrCode.setLblTo("Paiement commerçant face à face");
			             break;  //optional  
			            case "2":    
			            
			            	frmQrCode.setLblTo("Paiement commerçant à distance (e-commerce)");
			             break;  //optional
			            case "3":    
			          
			            	frmQrCode.setLblTo("Paiement livreur face à face");
			             break;  //optional	
			             default :
			            
			            		frmQrCode.setLblTo("Réservé pour un usage futur");
			            }
		             break;  //optional  
		            case 2:    
		            	frmQrCode.setText8002(str.substring(tagInd + Length , tagInd + Length + lengthValue));	    
		             break;  //optional		
		            case 3:    
		            	frmQrCode.setText8003(str.substring(tagInd + Length , tagInd + Length + lengthValue));	    
		             break;  //optional		             
		            case 4:    
		            	frmQrCode.setText8004(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional
		            case 5:    
		            	frmQrCode.setText8005(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional
		            case 6:    
		            	frmQrCode.setText8006(str.substring(tagInd + Length , tagInd + Length + lengthValue));	
		             break;  //optional		             

		            }    
		           }
	            	//System.out.println(" =====>  SubTag : [" + String.format("%02d", Stag) + "] Length : [" + String.format("%02d", lengthValue) + "] Values : [" + str.substring(tagInd + Length , tagInd + Length + lengthValue) +"]");
	            
	       
	            i = tagInd + Length + lengthValue;

	        }

	    }
	    public static SecretKey parseSecretKey(String secretKey) throws ParseException {
	        return new SecretKeySpec(stringToByteArray(secretKey), SYMMETRIC_ALGORITHM);
	    }
	    public static byte[] hexStringToByteArray(String s) {
	        int len = s.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                                 + Character.digit(s.charAt(i+1), 16));
	        }
	        return data;
	    }
	    
	    public String getParsingSubValue(String Str,int tagP) throws NoSuchAlgorithmException, NoSuchPaddingException, ParseException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		      parseEmvcoQR_Sub(Str,tagP);
		      return "OK";
		    }    
		    
	    public static byte[] stringToByteArray(String hexaString) throws ParseException {
			// the padding shouldn't be used, so a random one was chosen
			return stringToByteArray(hexaString, hexaString.length() / 2, (byte) 0xFF);
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

		
	
}
