package com.mayflowertech.chilla.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mayflowertech.chilla.config.custom.CustomException;

public class CommonUtils {
  private static final Logger logger = LoggerFactory
          .getLogger(CommonUtils.class);
  
  public static void printLog(String msg) {
      Date today = new Date();
      SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
              "dd-MM-yyyy HH:mm:SS");
      String date = DATE_FORMAT.format(today);
      System.out.println(date + " >> " + msg);

  }
  
   public static String[] ToStringArray(List<String> arr) 
      { 
    
          // declaration and initialise String Array 
          String str[] = new String[arr.size()]; 
    
          // ArrayList to Array Conversion 
          for (int j = 0; j < arr.size(); j++) { 
    
              // Assign each value to String array 
              str[j] = arr.get(j); 
          } 
    
          return str; 
      } 

  public static Date toDate(String datestring, String format) {
      Date dt = null;
      DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
      DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
      DateFormat df4 = new SimpleDateFormat("MM dd, yyyy");
      DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");
      DateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
      DateFormat df7 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      DateFormat df8 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      DateFormat df9 = new SimpleDateFormat("dd-MM-yyyy");
      try {
          if (format.trim().equalsIgnoreCase("dd/MM/yyyy")) {
              dt = df1.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("dd-MM-yyyy HH:mm:ss")) {
              dt = df2.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("dd-MMM-yyyy")) {
              dt = df3.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("MM dd, yyyy")) {
              dt = df4.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("E, MMM dd yyyy")) {
              dt = df5.parse(datestring);
          } else if (format.trim()
                  .equalsIgnoreCase("E, MMM dd yyyy HH:mm:ss")) {
              dt = df6.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("dd-MM-yyyy HH:mm")) {
              dt = df7.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("dd/MM/yyyy HH:mm")) {
              dt = df8.parse(datestring);
          } else if (format.trim().equalsIgnoreCase("dd-MM-yyyy")) {
              dt = df9.parse(datestring);
          }

      } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }

      return dt;
  }

  public static String getClientIp(HttpServletRequest request) {

      String remoteAddr = "";
      if (request != null) {
          remoteAddr = request.getHeader("X-FORWARDED-FOR");
          if (remoteAddr == null || "".equals(remoteAddr)) {
              remoteAddr = request.getRemoteAddr();
          }
      }

      return remoteAddr;
  }

  public static String encryptData(String data, String key) {
      String ret = "";
      Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher;
      try {
          cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.ENCRYPT_MODE, aesKey);
          byte[] encrypted = cipher.doFinal(data.getBytes());
          ret = new String(encrypted);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      // encrypt the text
      catch (InvalidKeyException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (BadPaddingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }

      return ret;
  }

  public static String deccryptData(String data, String key) {
      String ret = "";
      Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher;
      try {
          cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.DECRYPT_MODE, aesKey);
          byte[] decrypted = cipher.doFinal(data.getBytes());
          ret = new String(decrypted);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      // encrypt the text
      catch (InvalidKeyException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (BadPaddingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }

      return ret;
  }

  public static String encrypt(String data, String key, String initVector) {
      try {
          IvParameterSpec iv = new IvParameterSpec(
                  initVector.getBytes("UTF-8"));
          SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),
                  "AES");

          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
          cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

          byte[] encrypted = cipher.doFinal(data.getBytes());
          System.out.println("encrypted string: "
                  + Base64.encodeBase64String(encrypted));

          return Base64.encodeBase64String(encrypted);
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      return null;
  }

  public static String decrypt(String data, String key, String initVector) {
      try {
          IvParameterSpec iv = new IvParameterSpec(
                  initVector.getBytes("UTF-8"));
          SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"),
                  "AES");

          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
          cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

          byte[] original = cipher.doFinal(Base64.decodeBase64(data));

          return new String(original);
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      return null;
  }


  public static String getOSType() {
      String ret = "LINUX";
      String os = System.getProperty("os.name");
      if (os.toLowerCase().startsWith("windows")) {
          ret = "WINDOWS";
      }
      return ret;
  }
  
  
  public static  int findCurrentFinancialYear(String year) {
      int financialyear = 2019;
      String[] ddmmyyyytokens = year.split("-");
      if (ddmmyyyytokens.length != 3) {
          logger.error("Error in findCurrentFinancialYear. Invalid date format dd-mm-yyyy  "+year);
      }
      try {
          financialyear = Integer.parseInt(ddmmyyyytokens[2]);
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      if (financialyear == 0) {
          logger.error("Error in findCurrentFinancialYear. Invalid year value ");
      }
      return financialyear;
  }
  
  public static boolean isValidEmail(String email) {
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    return email.matches(emailRegex);
	}
  

  public static  LocalDate parseDate(String dateStr) throws CustomException {
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        return LocalDate.parse(dateStr, formatter);
	    } catch (DateTimeParseException e) {
	        throw new CustomException("Invalid date format. Expected format: dd-MM-yyyy");
	    }
	}
}