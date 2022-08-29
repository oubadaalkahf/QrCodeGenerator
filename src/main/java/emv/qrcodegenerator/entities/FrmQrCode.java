package emv.qrcodegenerator.entities;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FrmQrCode {


    String emvcoQrValue;
    int tag, subTag;
    boolean hasSubTag;
    private String textPoI;
    private String textPld;
    private String text26;
    private String text2600;
    private String text2602;
    private String text2605;
    private String text2606;
    private String text2607;
    private String text2606D;
    private String text53;
    private String text54;
    private String text62;
    private String text6201;
    private String text6202;
    private String text6203;
    private String text6204;
    private String text6205;
    private String text6206;
    private String text6207;
    private String text6208;
    private String text64;
    private String text6401;
    private String text6402;
    private String text80;
    private String text8000;
    private String text8001;
    private String text8004;
    private String text63;
    private String lblPoI;
    private String lblFc;
    private String lblTo;
    private String text58;
    private String text59;
    private String text60;
    private String text8002;
    private String text8003;
    private String text52;
    private String crc;
    private String text8005;
    private String text8006;
    private Boolean LngPref = false;
    private String Key;
    private String Iv;
}


