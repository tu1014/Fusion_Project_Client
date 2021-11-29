package Validator;

public class Validator {

    public static boolean isValidStudentId(String stdId) {
        if(stdId.matches("^\\d{8}")) return true;
        else return false;
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
        if(phoneNumber.matches("^\\d{3}-\\d{4}-\\d{4}$")) return true;
        else return false;
    }

    public static boolean isValidBirthDay(String birthDay) {
        if(birthDay.matches("^\\d{6}$")) return true;
        else return false;
    }

    public static boolean isZero(int i) { return i == 0; }

    public static boolean isValidGrade(String grade) {
        if (grade.matches("^[1-4]$")) return true;
        else return false;
    }



}
