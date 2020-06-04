import javax.script.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.print("Input a arithmetic expression: ");
        String consoleString = in.nextLine();
        //String consoleString = "4/2"; //uncomment if you are debugging
        String[] checkString = CheckMethod(consoleString);
        int result = DoOperation(checkString);
        if (!checkString[0].matches("^[0-9]"))
            System.out.println(ConvertToRoman(result));
        else
            System.out.println(result);
    }
    private static String[] CheckMethod(String input) throws Exception{
        if(input.length()<2){
            throw new Exception("The input string is not in the correct format. Please use the format 1+1 or I*I");
        }
        String[] result = input.split("(?<=[-+*/])|(?=[-+*/])");
        if (result.length != 3){
            throw new Exception("The input string is not in the correct format. Please use the format 1+1 or I*I");
        }
        String regexRoman = "[XVI]+", regexDigit = "^[0-9]", regexOperations = "[+-/*]+";
        if (!((result[0].matches(regexRoman)&&result[2].matches(regexRoman) ||
                (result[0].matches(regexDigit)&&result[2].matches(regexDigit))) && result[1].matches(regexOperations))){
            throw new Exception("The input string is not in the correct format. Please use the format 1+1 or I*I");
        }
        return result;
    }
    private static int DoOperation(String[] input) throws Exception{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        var a = ConvertToArabic(input[0]);
        var b = ConvertToArabic(input[2]);
        Object result = engine.eval(ConvertToArabic(input[0]) + input[1]+ ConvertToArabic(input[2]));
        if (result.toString().matches("^[0-9]{1,2}"))
            return (int)result;
        else
            return (int)Math.floor((double)result);
    }
    private static int ConvertToArabic(String input){
        if(input.matches("^[0-9]"))
            return Integer.parseInt(input);
        int arabicValue = 0;
        while(input.length()!=0){
            String finalInput = input;
            var isEqual = (Arrays.stream((ArabicRoman.values())).filter(x->
                    x.name().length()>1 && finalInput.equals(x.name())).findAny());
            if ((isEqual).isPresent()){
                return ((ArabicRoman)(Arrays.stream((ArabicRoman.values())).filter(x->
                        x.name().length()>1 && finalInput.equals(x.name())).toArray()[0])).value;
            }
            ArabicRoman temp = ((ArabicRoman)Arrays.stream((ArabicRoman.values())).filter(x->
                    finalInput.contains(x.name())).toArray()[0]);
            input = input.replaceFirst(temp.name(),"");
            arabicValue+=temp.value;
        }
        return arabicValue;
    }
    private static String ConvertToRoman (int num){
        StringBuilder roman = new StringBuilder();
        int N = num;
        for (int i = 0; i < ArabicRoman.values().length; i++) {
            while (N >= ArabicRoman.values()[i].value) {
                roman.append(ArabicRoman.values()[i].name());
                N -= ArabicRoman.values()[i].value;
            }
        }
        return roman.toString();
    }
    public enum ArabicRoman {
        XC(90),L(50),XL(40),X(10),IX(9),V(5),IV(4), I(1);
        private final int value;
        private ArabicRoman(int value) {
            this.value = value;
        }
    }
}
