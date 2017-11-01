package utils.team3;

import java.util.Random;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import utils.db.MySQLAccess;
import java.util.*;

public class Challenge{

  public static byte[] generateChallenge(){
    byte[] challenge = new byte[32];
    new Random().nextBytes(challenge);
    return challenge;
  }

  public static boolean validateResponse(byte[] response, byte[] challenge, byte[] passwordHash){
    try{
      System.out.println("Response: "+Base64.getEncoder().encodeToString(response));
      // h(h(h(pwd)) + c) + h(pwd) = response
      //XOR hash of password hash with challenge
      byte[] expectedResult = computeXOR(generateHash(passwordHash), challenge); // h(h(pwd)) + c
      System.out.println(" h(h(pwd)) + c: "+Base64.getEncoder().encodeToString(expectedResult));
      //hash the result
      expectedResult = generateHash(expectedResult); // h(h(h(pwd)) + c)
      System.out.println(" h(h(h(pwd)) + c): "+Base64.getEncoder().encodeToString(expectedResult));
      System.out.println(" h(h(h(pwd)) + c) + h(pwd)"+Base64.getEncoder().encodeToString(computeXOR(expectedResult, passwordHash)));
      //XOR result with challenge response
      expectedResult = computeXOR(expectedResult, response); // h(h(h(pwd)) + c) + h(h(h(pwd)) + c) + h(pwd) = h(pwd)
      System.out.println("h(pwd): "+Base64.getEncoder().encodeToString(expectedResult));
      //hash the result
      expectedResult = generateHash(expectedResult); // h(h(pwd))
      System.out.println("h(h(pwd)): "+Base64.getEncoder().encodeToString(expectedResult));
      System.out.println("h(h(pwd)) actual: "+Base64.getEncoder().encodeToString(generateHash(passwordHash)));
      return Arrays.equals(expectedResult, generateHash(passwordHash));
    } catch(Exception e){
      e.printStackTrace();
    }
    return false;
  }

  public static boolean validateNFCResponse(byte[] response, byte[] challenge, byte[] nfcHash){
    try{
      System.out.println("NFC Response: "+Base64.getEncoder().encodeToString(response));
      System.out.println("NFC challenge: "+Base64.getEncoder().encodeToString(challenge));

      // h(h(s)) + c) + h(s) = response
      //XOR hash of password hash with challenge
      byte[] expectedResult = computeXOR(nfcHash, challenge); // h(s) + c
      System.out.println("h(s) + c: "+Base64.getEncoder().encodeToString(expectedResult));
      //hash the result
      expectedResult = generateHash(expectedResult); // h(h(s) + c)
      System.out.println("h(h(s) + c): "+Base64.getEncoder().encodeToString(expectedResult));
      //XOR result with challenge response
      expectedResult = computeXOR(expectedResult, response); // h(h(s) + c) + h(h(s) + c) + h(s)
      System.out.println("s: "+Base64.getEncoder().encodeToString(expectedResult));
      expectedResult = generateHash(expectedResult);
      System.out.println("nfcHASH: "+Base64.getEncoder().encodeToString(nfcHash));
      System.out.println("H(h(h(s)XOR c) XOR resp): "+Base64.getEncoder().encodeToString(expectedResult));
      return Arrays.equals(expectedResult, nfcHash);
    } catch(Exception e){
      e.printStackTrace();
    }
    return false;
  }


  private static byte[] computeXOR(byte[] b1, byte[] b2) {
    byte[] result = new byte[32];
    for (int i = 0; i < 32; i++) {
        result[i] = (byte)(b1[i] ^ b2[i]);
    }
    return result;
  }

  public static byte[] generateHash(byte[] input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    return digest.digest(input);
  }

  public static int storeChallenge(byte[] challenge, String username, String type){
    String sql = "SELECT uid FROM CS3205.user WHERE username = ?";
    int result = 0;
      try{
          // Maybe need to get from server 4
          PreparedStatement ps =  MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
            int userID = rs.getInt("uid");
            sql = "INSERT INTO CS3205.user_challenge (challengeString, uid, type) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE challengeString=VALUES(challengeString), type=VALUES(type)";
            ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            System.out.println("String: "+Base64.getEncoder().encodeToString(challenge) + "   "+Arrays.toString(challenge));
            ps.setString(1, Base64.getEncoder().encodeToString(challenge));
            ps.setInt(2, userID);
            ps.setString(3, type);
            result = ps.executeUpdate();
            break;
          }
      }catch(Exception e){
          e.printStackTrace();
      }
      return result;
  }

  public static byte[] getUserChallenge(String username, String type){
    String sql = "SELECT * FROM CS3205.user WHERE username = ?";
      try{
          PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
          ps.setString(1, username);
          ResultSet rs = ps.executeQuery();
          while(rs.next()){
            int userID = rs.getInt("uid");
            sql = "SELECT * FROM CS3205.user_challenge WHERE uid = ? AND type = ?";
            ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setString(2, type);
            rs = ps.executeQuery();
            while(rs.next()){
              return Base64.getDecoder().decode(rs.getString("challengeString").getBytes());
            }
          }
      }catch(Exception e){
          e.printStackTrace();
      }
      return new byte[32];
  }

  public static int deleteUserChallenge(String username, String type){
      String sql = "SELECT * FROM CS3205.user WHERE username = ?";
        try{
            PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
              int userID = rs.getInt("uid");
              sql = "DELETE FROM CS3205.user_challenge WHERE uid = ? AND type = ?";
              ps = MySQLAccess.connectDatabase().prepareStatement(sql);
              ps.setInt(1, userID);
              ps.setString(2, type);
              return ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
  }
}
