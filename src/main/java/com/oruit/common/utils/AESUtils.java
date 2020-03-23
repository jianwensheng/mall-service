package com.oruit.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

/**
 * ase加密utils wangyao 2019/4/12.
 */
public class AESUtils {


    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES加密字符串
     *
     * @param content 需要被加密的字符串
     * @param key     加密需要的密码
     * @return 密文和密钥
     */
    public static String encrypt(String content, String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.ENCRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] byte_content = content.getBytes("utf-8"); //获取加密内容的字节数组(设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] encode_content = cipher.doFinal(byte_content); //密码器加密数据
            System.out.println("加密后的内容：" + encode_content);
            return Base64.encodeBase64String(encode_content); //将加密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解密AES加密过的字符串
     *
     * @param content AES加密过过的内容
     * @param key     加密时的密码
     * @return 明文
     */
    public static String decrypt(String content, String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (key.length() != 16) {
            return null;
        }
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHM);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.DECRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte[] encode_content = Base64.decodeBase64(content); //把密文字符串转回密文字节数组
            byte[] byte_content = cipher.doFinal(encode_content); //密码器解密数据
            return new String(byte_content, "utf-8"); //将解密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        /*String content = "这是一次测试AES加密";
        String password = "qbj9mkrqza2svcw0";//getRandomString(16);//"821455a3-58ce-4965-ae08-3e6b0d090fae";//UUID.randomUUID().toString();
        System.out.println("加密的密钥：" + password);
        System.out.println("加密之前的内容：" + content);

        // 加密
        String contant = AESUtils.encrypt(content, password);
        System.out.println("转码后的内容：" + contant);*/
        //System.out.println("加密的key：" + retMap.get("key").toString());

        String contant = "mDYZPruUm+fLckkN+3hRL9fF06GqtX1QZWBXzg7" +
                "sBIl0dpTL0KtozdWXduuSYodKTCqnmXrGlzinw4K3u0c1rYngDdiB84s" +
                "fPsmPyXiVza74xGvsc4We9Mn5EwdhyNUgx/syHJrLqlQEd+IUQntPaRyPfWOi" +
                "xsoWGazDUCIJkVMBSBb2AU6I/PcJGGjlXsNubBUKGQ/AvU1sRDcWUvb92q4vunWAl" +
                "m8zWTxPeZJuV8RsYjDld79eDdlGSs67G5xi+d1neSvgXSf17v4AWekW/SUAn2uS+wyss1s" +
                "/Kdy7GUv7XDFUdPPQz13ukwQc8Q7125VvhXoUdxc/FHg+MVrJi6Jud72lL14OfPHVKH4Uifs" +
                "saPk0/Tytw1MRzuCGkYZ/CqszKzip84xYhlA7638Q0sVBhUfM6BWD7ZA/weNmTQn+KSaj3t+hdBPz" +
                "/7t9QhczS2w9PxljrVZkkAfb3IhXH5EwEG13xQgoeo57u1yElr5e/1Obd35LIhFygD/FoOYvcX1OEJx0EP" +
                "2snZF21VSxBPEXwGOSZc4ThiCt95rtm3o4r9zQJIij3H09dPVdVKCvbGpcX5+2a7jkNCjsau7Nbgf0qCmOu0" +
                "2C0jNQzqrc8/48Engq+GMUFUSEwa037nZrG51dOMDhPaOwfuXgAWL81iqktHjENifJJPMZsWa/szJdCaap8Dpb4KrRP" +
                "eAVeRkUGqqkj0S9Ul3oArpQKwqtUxphWQRzGYEXvbetBASeF2aURID6oNIAAGr0Jm1kzja182iPoz3gdHfr1YWzJsCrdFpQ" +
                "qrX4OyCiTlC4qwP18Qef4TZEZFZxbaI958Ot5zbtg+0QdBDIQky2l7tvE0cCaDU+ETNGejWO7HZk1uu1+sUsiLrnibaGfPpG" +
                "yeyjraEVm30D88lkGYQWi6XedJVK/aSjpQX3Xb3xcP0cIEbRL1SXCXmJ9djx5oZpSC1p+AeAxHRlxBhDA0qbdLLPcnbfiL6OwLmUsMr8" +
                "Wcoa6imfBP8wm0aCvoG4xDiVBoozZb/gExcne6Ruun6PmdeVSyf492/ivkitQzgFCTuUuH8IITxntoazuHELyz8CHQvsjm8DRK2s90ieDZ/EO" +
                "d2NJrzgMVICmqp4iyhuqrCt7eG8+y2gkL8IfBmXN8bZJqmfN8rRZQEgsjkS/oQKuXKSg5aEsHqZXYmldRub7A+0T17JbMG7evu9C361s+gpf1aKEiEa" +
                "mDQFfqcG9xrvqIvyFJ0T7SoDEgnzPHCzNCdfxnSClU67evu9C361s+gpf1aKEiEaCRt4y8dauP51DiKfaUX296eiafNgr1ldfD4XFFjHri9aHfLJfSfJ3ao" +
                "lAjL4w13IgrREuUVyaXoKk0bxomAFGPaHr2JQOMB4DD4R1w/GufQ7wiHCTkPw31V35GnPdCTzfNniqlktQVD0DXHSF/Em6f3ID7XssWrto9le2U2VIMWdday" +
                "7E0yxNUJ+6mAHJmdvsvX8PJL2qDHth+gCSS+52EostvgySf5UTuHV29L3vYbqUpx+u/S19GRD1ctx3S5G68JGNRzIPFRRAPFsbWCLC8/Unzx8IBWm2qRWSsr" +
                "fV8pai18PqbdwZAzZOOPB4H9aGQMbgd+Cdb8Wr3dxMfwMzTU+ETNGejWO7HZk1uu1+sVzqEBP7vecZauTpktv4a2lp9IgQO4QzjrbypZhwUCCziAaTPnQ2M" +
                "5wVg/YNFM5m8uk9aD+hehGwohsOO6Wk8zL+LDRcZneX9eYOEf2A4AAIuWalAhySa+2EcSrIktNiPLlihqQU5DVG8aSchyBsWryrU6frXauHcXe9Yf37CHaH" +
                "mxOo8E2MNnEk/K/EWreksS9JH4RbKUV/W/Y3UguejJvpKOlBfddvfFw/RwgRtEvVAX+vXijYbij12WJM2WaPp3uoynz8zOVsMrGm3WKoTKZe9+R3hjg" +
                "m2wv5gHsrmMLyIdvs5ncYlzTZjHBFhEUGxJddzqc5hkcRLTvlVOBtgxOTqlF18NjEMtaMNsreR4WgayPJ5g1iARZKDhGuukrQNSwPd8LSG63O4ao2N9" +
                "rIWqQzozfAOH6foahjRg2JcjxV2r2htuRQtLV9rmJLie0bYhdItlQx2ePA2fS2e83pJXIkI8EQbisG1KNgL4BpFkrabH5iy9M6bHup7p" +
                "++XTLVgf5auCdfgQkjpf8IcQC5Ds55HXLsFFB/v0/NiY3kDi/vVUCGnQBJlxZJuaQc3AA5Qs7uYcp2m5MSPYK1aRgdr6e8eVmdbUzDy" +
                "dJJTvFWd7frxJgUfW++FGiNG50ItZLjFUpAQdV7bj+kk9uc8M7H2VMl3tkwsIY9045gThtZV/mgKzXEglV6+/PtXWUBUXNVqKiPLDwRJ" +
                "RFHyU7l0nPlgOgEb8I7qKqQJKnX6B3t9BqH1Nr2FSEoUnJ6mBrmhCvW6AsTOs+usse6GBO9Hp6epkuU/+DT+Fxu9rVvXlg3FJBrVwWTRA" +
                "yjL3Zox5/sFBwEBdUWh8zOnJRCUSuR4BX8kZpeeKmLDmVyYuo6S/i48VoCGYK3JOO6LDGRnLXLOh+ih5sZIQNLAk7Ev8RVMshkzMXkMbR5QL" +
                "TKQJeX/za9xRiJciZGaZke52Re3dx4N4ugpJxlI5TBWjBCN75AFRaa9kNplpmwvHJcR8L3UiVxvH2Bs1Uj83LfPoDOUJwXWE36GIyBzIW3Na" +
                "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";
        // 解密
        String password = "gs5wo8feygrab9lg";
        String decrypt = AESUtils.decrypt(contant, password);
        System.out.println("解密后的内容：" + decrypt);
    }

    public static String getRandomString(int length) {//length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
