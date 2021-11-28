package Validator;

public class Validator {

    public static boolean isValidStudentId(String stdId) {
        if(stdId.length() != 8) return false;
        else return true;
    }

    public static boolean isValidProfessorId(String pfId) {
        if(pfId.length() != 8) return false;
        else return true;
    }

    public static boolean isEmpty(String pfId) {
        if(pfId.length() == 0) return true;
        else return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() != 13) return false;
        else return true;
    }

    public static boolean isValidBirthDay(String phoneNumber) {
        if(phoneNumber.length() != 6) return false;
        else return true;
    }

    public static boolean isZero(int i) { return i == 0; }



}
