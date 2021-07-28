import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

public class calc {
    public static class ActionService {

        public static String calculate(Number first, Number second, String action) throws Exception {

            int result = switch (action) {
                    case "+" -> first.getValue() + second.getValue();
                    case "-" -> first.getValue() - second.getValue();
                    case "*" -> first.getValue() * second.getValue();
                    case "/" -> first.getValue() / second.getValue();
                    default -> throw new Exception("Я же сказал - только '+, -, *, /'");
            };

                if (first.getType() == NumberType.ROMAN) {
                return NumberService.toRomanNumber(result);
            } else return String.valueOf(result);
        }
    }

        record Number(int value, NumberType type) {

                int getValue() {
                        return value;
                }

                NumberType getType() {
                        return type;
                }
        }

    static class NumberService {

        private final static TreeMap<Integer, String> romanString = new TreeMap<>();

        static {
            romanString.put(1, "I");
            romanString.put(4, "IV");
            romanString.put(5, "V");
            romanString.put(9, "IX");
            romanString.put(10, "X");
            romanString.put(40, "XL");
            romanString.put(50, "L");
            romanString.put(90, "XC");
            romanString.put(100, "C");
        }

        static Number parseAndValidate(String symbol) throws Exception {

            int value;
            NumberType type;

            try {
                value = Integer.parseInt(symbol);
                type = NumberType.ARABIC;
            } catch (NumberFormatException e) {
                value = toArabicNumber(symbol);
                type = NumberType.ROMAN;
            }

            if (value < 1 || value > 10) {
                throw new Exception("Неподходящее значение числа(ел), используйте числа от 1 до 10 включительно");
            }

            return new Number(value, type);
        }

        static Number parseAndValidate(String symbol, NumberType type) throws Exception {

            Number number = parseAndValidate(symbol);
            if (number.getType() != type) {
                throw new Exception("Числа разных типов, используйте один тип вводных значений");
            }

            return number;
        }

        private static int letterToNumber(char letter) {

            int result = -1;

            for (Map.Entry<Integer, String> entry : romanString.entrySet()) {
                if (entry.getValue().equals(String.valueOf(letter))) result = entry.getKey();
            }
            return result;
        }

        static String toRomanNumber(int number) {

            int i = romanString.floorKey(number);

            if (number == i) {
                return romanString.get(number);
            }
            return romanString.get(i) + toRomanNumber(number - i);
        }

        static int toArabicNumber(String roman) throws Exception {
            int result = 0;

            int i = 0;
            while (i < roman.length()) {
                char letter = roman.charAt(i);
                int num = letterToNumber(letter);

                if (num < 0) throw new Exception("Неверный римский символ");

                i++;
                if (i == roman.length()) {
                    result += num;
                } else {
                    int nextNum = letterToNumber(roman.charAt(i));
                    if (nextNum > num) {
                        result += (nextNum - num);
                        i++;
                    } else result += num;
                }
            }
            return result;
        }
    }

    public enum NumberType {
        ARABIC,
        ROMAN
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        startCalc();

        while (true) {

            System.out.println("Введите выражение: ");
            String line = scanner.nextLine();

            if (line.equals("exit")) {
                exitCalc();
                break;
            }

            try {
                String[] symbols = line.split(" ");
                if (symbols.length != 3) throw new Exception("Я сдох, попробуй по-другому");

                Number firstNumber = NumberService.parseAndValidate(symbols[0]);
                Number secondNumber = NumberService.parseAndValidate(symbols[2], firstNumber.getType());
                String result = ActionService.calculate(firstNumber, secondNumber, symbols[1]);
                System.out.println("Ответ: \n" + result);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                exitCalc();
                break;
            }
        }

        scanner.close();
    }

    private static void startCalc() {
        System.out.println("калькулятор работает только с арабскими и римскими цифрами от 1 до 10");
        System.out.println("Он может предложить только следующие операции:");
        System.out.println("Сложение(+), Вычитание(-), Умножение(*), Деление(/)");
        System.out.println("Введите 'exit', чтобы выключить программу");
    }

    private static void exitCalc() {

        System.out.println("Гудбай!");

    }
}
