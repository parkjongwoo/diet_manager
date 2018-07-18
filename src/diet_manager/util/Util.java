package diet_manager.util;

import java.security.MessageDigest;

public class Util {
	/**
	 * 비밀번호 암호화 유틸. 
	 * 암호 DB저장시 이 메소드로 처리하여 저장.
	 * 암호 비교시에도 이 메소드로 처리한 값과 DB값을 비교.
	 * 개인정보보호법에 의거 개발자는 이용자의 비번을 알면 안된다.
	 * @param planText 암호화할 문자열
	 * @return sha-256 방식으로 암호화된 문자열
	 */
	public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }          

            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
