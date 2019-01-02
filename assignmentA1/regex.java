import java.util.regex.Matcher;
import java.util.regex.Pattern;

class regex{
    public static void main(String[] args) {
        /*
        String input = "START";
        String regex = "^([A-Z]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        try {
            matcher.group();
        } catch (Exception e) {
            System.out.println("No Match Found");
        }
        */
        String mydata = " MOVER AREG,cd";
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*)([\\s]+)([a-zA-Z]+)([\\s]*)(.*)$");
        Matcher matcher = pattern.matcher(mydata);
        //Pattern movePattern = Pattern.compile("^([A|a|B|b|C|c|D|d][R|r][E|e][G|g])([\\s]*),([\\s]*)([a-zA-Z0-9]*)$");
        Pattern movePattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");
        Matcher operandMatcher;
        if (matcher.find()) {

            operandMatcher = movePattern.matcher((String)matcher.group(5));;
            int cnt = operandMatcher.groupCount();
            System.out.println(matcher.group(5));
            if(operandMatcher.find()) {
                for(int i = 1; i <= cnt; i++) {
                    System.out.println("Group " + i + " \t:\t" + operandMatcher.group(i));
                }
            }
            else {
                System.out.println("Invalid Operand");
            }
        }
        else {
            System.out.println("Invalid Input");
        }
    }
}