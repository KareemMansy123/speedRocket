package com.speedrocket.progmine.speedrocket;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ibrahim on 8/13/2018.
 */

public class App extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static String getWelcomeMail(String name ){

        return "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "\t<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" +
                "  \t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0;\">\n" +
                " \t<meta name=\"format-detection\" content=\"telephone=no\"/>\n" +
                "\n" +
                "\t<!-- Responsive Mobile-First Email Template by Konstantin Savchenko, 2015.\n" +
                "\thttps://github.com/konsav/email-templates/  -->\n" +
                "\n" +
                "\t<style>\n" +
                "/* Reset styles */ \n" +
                "body { margin: 0; padding: 0; min-width: 100%; width: 100% !important; height: 100% !important;}\n" +
                "body, table, td, div, p, a { -webkit-font-smoothing: antialiased; text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; line-height: 100%; }\n" +
                "table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse !important; border-spacing: 0; }\n" +
                "img { border: 0; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; }\n" +
                "#outlook a { padding: 0; }\n" +
                ".ReadMsgBody { width: 100%; } .ExternalClass { width: 100%; }\n" +
                ".ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; }\n" +
                "/* Rounded corners for advanced mail clients only */ \n" +
                "@media all and (min-width: 560px) {\n" +
                "\t.container { border-radius: 8px; -webkit-border-radius: 8px; -moz-border-radius: 8px; -khtml-border-radius: 8px;}\n" +
                "}\n" +
                "/* Set color for auto links (addresses, dates, etc.) */ \n" +
                "a, a:hover {\n" +
                "\tcolor: #127DB3;\n" +
                "}\n" +
                ".footer a, .footer a:hover {\n" +
                "\tcolor: #999999;\n" +
                "}\n" +
                " \t</style>\n" +
                "\n" +
                "\t<!-- MESSAGE SUBJECT -->\n" +
                "\t<title>Get this responsive email template</title>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<!-- BODY -->\n" +
                "<!-- Set message background color (twice) and text color (twice) -->\n" +
                "<body topmargin=\"0\" rightmargin=\"0\" bottommargin=\"0\" leftmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" width=\"100%\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; width: 100%; height: 100%; -webkit-font-smoothing: antialiased; text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; line-height: 100%;\n" +
                "\tbackground-color: #F0F0F0;\n" +
                "\tcolor: #000000;\"\n" +
                "\tbgcolor=\"#F0F0F0\"\n" +
                "\ttext=\"#000000\">\n" +
                "\n" +
                "<!-- SECTION / BACKGROUND -->\n" +
                "<!-- Set message background color one again -->\n" +
                "<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; width: 100%;\" class=\"background\"><tr><td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0;\"\n" +
                "\tbgcolor=\"#F0F0F0\">\n" +
                "\n" +
                "<!-- WRAPPER -->\n" +
                "<!-- Set wrapper width (twice) -->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n" +
                "\twidth=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n" +
                "\tmax-width: 560px;\" class=\"wrapper\">\n" +
                "\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n" +
                "\t\t\tpadding-top: 20px;\n" +
                "\t\t\tpadding-bottom: 20px;\">\n" +
                "\n" +
                "\t\t\t<!-- PREHEADER -->\n" +
                "\t\t\t<!-- Set text color to background color -->\n" +
                "\t\t\t<div style=\"display: none; visibility: hidden; overflow: hidden; opacity: 0; font-size: 1px; line-height: 1px; height: 0; max-height: 0; max-width: 0;\n" +
                "\t\t\tcolor: #F0F0F0;\" class=\"preheader\">\n" +
                "\t\t\t\tAvailable on&nbsp;GitHub and&nbsp;CodePen. Highly compatible. Designer friendly. More than 50%&nbsp;of&nbsp;total email opens occurred on&nbsp;a&nbsp;mobile device&nbsp;— a&nbsp;mobile-friendly design is&nbsp;a&nbsp;must for&nbsp;email campaigns.</div>\n" +
                "\n" +
                "\t\t\t<!-- LOGO -->\n" +
                "\t\t\t<!-- Image text color should be opposite to background color. Set your url, image src, alt and title. Alt text should fit the image size. Real image size should be x2. URL format: http://domain.com/?utm_source={{Campaign-Source}}&utm_medium=email&utm_content=logo&utm_campaign={{Campaign-Name}} -->\n" +
                "\t\t\t<a target=\"_blank\" style=\"text-decoration: none;\"\n" +
                "\t\t\t\thref=\"https://github.com/konsav/email-templates/\"><img border=\"0\" vspace=\"0\" hspace=\"0\"\n" +
                "\t\t\t\tsrc=\"http://www.speed-rocket.com/upload/logo/1527518927.png\"\n" +
                "\t\t\t\twidth=\"80\" height=\"30\"\n" +
                "\t\t\t\talt=\"Logo\" title=\"Logo\" style=\"\n" +
                "\t\t\t\tcolor: #000000;\n" +
                "\t\t\t\tfont-size: 10px; margin: 0; padding: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; border: none; display: block;\" /></a>\n" +
                "\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "<!-- End of WRAPPER -->\n" +
                "</table>\n" +
                "\n" +
                "<!-- WRAPPER / CONTEINER -->\n" +
                "<!-- Set conteiner background color -->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n" +
                "\tbgcolor=\"#FFFFFF\"\n" +
                "\twidth=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n" +
                "\tmax-width: 560px;\" class=\"container\">\n" +
                "\n" +
                "\t<!-- HEADER -->\n" +
                "\t<!-- Set text color and font family (\"sans-serif\" or \"Georgia, serif\") -->\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 24px; font-weight: bold; line-height: 130%;\n" +
                "\t\t\tpadding-top: 25px;\n" +
                "\t\t\tcolor: #000000;\n" +
                "\t\t\tfont-family: sans-serif;\" class=\"header\">\n" +
                "\t\t\t\tHi ,"+name+" \n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t\n" +
                "\t<!-- SUBHEADER -->\n" +
                "\t<!-- Set text color and font family (\"sans-serif\" or \"Georgia, serif\") -->\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-bottom: 3px; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 18px; font-weight: 300; line-height: 150%;\n" +
                "\t\t\tpadding-top: 5px;\n" +
                "\t\t\tcolor: #000000;\n" +
                "\t\t\tfont-family: sans-serif;\" class=\"subheader\">\n" +
                "\t\t\t\tyour account has been activated  \n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<!-- HERO IMAGE -->\n" +
                "\t<!-- Image text color should be opposite to background color. Set your url, image src, alt and title. Alt text should fit the image size. Real image size should be x2 (wrapper x2). Do not set height for flexible images (including \"auto\"). URL format: http://domain.com/?utm_source={{Campaign-Source}}&utm_medium=email&utm_content={{Ìmage-Name}}&utm_campaign={{Campaign-Name}} -->\n" +
                "\n" +
                "\t<!-- PARAGRAPH -->\n" +
                "\t<!-- Set text color and font family (\"sans-serif\" or \"Georgia, serif\"). Duplicate all text styles in links, including line-height -->\n" +
                "\n" +
                "\t<!-- BUTTON -->\n" +
                "\t<!-- Set button background color at TD, link/text color at A and TD, font family (\"sans-serif\" or \"Georgia, serif\") at TD. For verification codes add \"letter-spacing: 5px;\". Link format: http://domain.com/?utm_source={{Campaign-Source}}&utm_medium=email&utm_content={{Button-Name}}&utm_campaign={{Campaign-Name}} -->\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n" +
                "\t\t\tpadding-top: 25px;\n" +
                "\t\t\tpadding-bottom: 5px;\" class=\"button\"><a\n" +
                "\t\t\thref=\"http://www.speedrocket.com/speedApp" +
                "\" target=\"_blank\" style=\"text-decoration: underline;\">\n" +
                "\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"max-width: 240px; min-width: 120px; border-collapse: collapse; border-spacing: 0; padding: 0;\"><tr><td align=\"center\" valign=\"middle\" style=\"padding: 12px 24px; margin: 0; text-decoration: underline; border-collapse: collapse; border-spacing: 0; border-radius: 4px; -webkit-border-radius: 4px; -moz-border-radius: 4px; -khtml-border-radius: 4px;\"\n" +
                "\t\t\t\t\tbgcolor=\"#E9703E\"><a target=\"_blank\" style=\"text-decoration: underline;\n" +
                "\t\t\t\t\tcolor: #FFFFFF; font-family: sans-serif; font-size: 17px; font-weight: 400; line-height: 120%;\"\n" +
                "\t\t\t\t\thref=\"http://www.speedrocket.com/speedApp\">\n" +
                "\t\t\t\t\t\tstart offering \n" +
                "\t\t\t\t\t</a>\n" +
                "\t\t\t</td></tr></table></a>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<!-- LINE -->\n" +
                "\t<!-- Set line color -->\n" +
                "\t<tr>\t\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%;\n" +
                "\t\t\tpadding-top: 25px;\" class=\"line\"><hr\n" +
                "\t\t\tcolor=\"#E0E0E0\" align=\"center\" width=\"100%\" size=\"1\" noshade style=\"margin: 0; padding: 0;\" />\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "\t<!-- LIST -->\n" +
                "\t\n" +
                "\t<!-- PARAGRAPH -->\n" +
                "\t<!-- Set text color and font family (\"sans-serif\" or \"Georgia, serif\"). Duplicate all text styles in links, including line-height -->\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 17px; font-weight: 400; line-height: 160%;\n" +
                "\t\t\tpadding-top: 20px;\n" +
                "\t\t\tpadding-bottom: 25px;\n" +
                "\t\t\tcolor: #000000;\n" +
                "\t\t\tfont-family: sans-serif;\" class=\"paragraph\">\n" +
                "\t\t\t\tHave a&nbsp;question? <a href=\"mailto:support@ourteam.com\" target=\"_blank\" style=\"color: #127DB3; font-family: sans-serif; font-size: 17px; font-weight: 400; line-height: 160%;\">support@ourteam.com</a>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\n" +
                "<!-- End of WRAPPER -->\n" +
                "</table>\n" +
                "\n" +
                "<!-- WRAPPER -->\n" +
                "<!-- Set wrapper width (twice) -->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n" +
                "\twidth=\"560\" style=\"border-collapse: collapse; border-spacing: 0; padding: 0; width: inherit;\n" +
                "\tmax-width: 560px;\" class=\"wrapper\">\n" +
                "\n" +
                "\t<!-- SOCIAL NETWORKS -->\n" +
                "\t<!-- Image text color should be opposite to background color. Set your url, image src, alt and title. Alt text should fit the image size. Real image size should be x2 -->\n" +
                "\n" +
                "\t<!-- FOOTER -->\n" +
                "\t<!-- Set text color and font family (\"sans-serif\" or \"Georgia, serif\"). Duplicate all text styles in links, including line-height -->\n" +
                "\t<tr>\n" +
                "\t\t<td align=\"center\" valign=\"top\" style=\"border-collapse: collapse; border-spacing: 0; margin: 0; padding: 0; padding-left: 6.25%; padding-right: 6.25%; width: 87.5%; font-size: 13px; font-weight: 400; line-height: 150%;\n" +
                "\t\t\tpadding-top: 20px;\n" +
                "\t\t\tpadding-bottom: 20px;\n" +
                "\t\t\tcolor: #999999;\n" +
                "\t\t\tfont-family: sans-serif;\" class=\"footer\">\n" +
                "\n" +
                "\t\t\t\tTAll Copyright Reserved © 2018 By Speedrocket \u200E <a href=\"https://github.com/konsav/email-templates/\" target=\"_blank\" style=\"text-decoration: underline; color: #999999; font-family: sans-serif; font-size: 13px; font-weight: 400; line-height: 150%;\"></a> \n" +
                "\n" +
                "\t\t\t\t<!-- ANALYTICS -->\n" +
                "\t\t\t\t<!-- http://www.google-analytics.com/collect?v=1&tid={{UA-Tracking-ID}}&cid={{Client-ID}}&t=event&ec=email&ea=open&cs={{Campaign-Source}}&cm=email&cn={{Campaign-Name}} -->\n" +
                "\t\t\t\t<img width=\"1\" height=\"1\" border=\"0\" vspace=\"0\" hspace=\"0\" style=\"margin: 0; padding: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; border: none; display: block;\"\n" +
                "\t\t\t\tsrc=\"https://raw.githubusercontent.com/konsav/email-templates/master/images/tracker.png\" />\n" +
                "            This App and website developed by <a href=\"https://progmine.com/\" target=\"_blank\" style=\"text-decoration: underline; color: #999999; font-family: sans-serif; font-size: 13px; font-weight: 400; line-height: 150%;\">Progmine</a> \n" +
                "\n" +
                "\t\t\t\t<!-- ANALYTICS -->\n" +
                "\t\t\t\t<!-- http://www.google-analytics.com/collect?v=1&tid={{UA-Tracking-ID}}&cid={{Client-ID}}&t=event&ec=email&ea=open&cs={{Campaign-Source}}&cm=email&cn={{Campaign-Name}} -->\n" +
                "\t\t\t\t<img width=\"1\" height=\"1\" border=\"0\" vspace=\"0\" hspace=\"0\" style=\"margin: 0; padding: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic; border: none; display: block;\"\n" +
                "\t\t\t\tsrc=\"https://raw.githubusercontent.com/konsav/email-templates/master/images/tracker.png\" />\n" +
                "\t\t</td>\n" +
                "\t\t\n" +
                "\t\n" +
                "\t</tr>\n" +
                "\t\n" +
                "\n" +
                "\n" +
                "<!-- End of WRAPPER -->\n" +
                "</table>\n" +
                "\n" +
                "<!-- End of SECTION / BACKGROUND -->\n" +
                "</td></tr></table>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }



    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String key = "mahmoodEsmail97$" ;
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
   /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

}
