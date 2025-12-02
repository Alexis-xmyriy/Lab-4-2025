import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Задание 8: Тестирование аналитических функций ===");
            testBasicFunctions();
            
            System.out.println("\n=== Тестирование комбинированных функций ===");
            testMetaFunctions();
            
            System.out.println("\n=== Тестирование ввода/вывода ===");
            testInputOutput();
            
            System.out.println("\n=== Задание 9: Тестирование сериализации ===");
            testSerialization();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
    
    private static void testBasicFunctions() {
        System.out.println("\n1. Тестирование Sin и Cos:");
        Sin sinFunc = new Sin();
        Cos cosFunc = new Cos();
        
        System.out.println("Sin(0) = " + sinFunc.getFunctionValue(0));
        System.out.println("Cos(0) = " + cosFunc.getFunctionValue(0));
        System.out.println("Sin(p/2) = " + sinFunc.getFunctionValue(Math.PI/2));
        System.out.println("Cos(p/2) = " + cosFunc.getFunctionValue(Math.PI/2));
        
        System.out.println("\n2. Табулирование Sin и Cos:");
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 10);
        
        System.out.println("Табулированный Sin в точке p/2: " + tabulatedSin.getFunctionValue(Math.PI/2));
        System.out.println("Табулированный Cos в точке p/2: " + tabulatedCos.getFunctionValue(Math.PI/2));
        
        System.out.println("\n3. Сумма квадратов Sin и Cos (должна быть ~1):");
        Function sinSquared = Functions.power(tabulatedSin, 2);
        Function cosSquared = Functions.power(tabulatedCos, 2);
        Function sumOfSquares = Functions.sum(sinSquared, cosSquared);
        
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("f(%.1f) = %.6f%n", x, sumOfSquares.getFunctionValue(x));
        }
    }
    
    private static void testMetaFunctions() {
        System.out.println("\n4. Тестирование Exp и Log:");
        Exp expFunc = new Exp();
        Log lnFunc = new Log(Math.E); // натуральный логарифм
        
        // Композиция ln(exp(x)) = x
        Function composition = Functions.composition(lnFunc, expFunc);
        
        System.out.println("exp(0) = " + expFunc.getFunctionValue(0));
        System.out.println("ln(1) = " + lnFunc.getFunctionValue(1));
        System.out.println("ln(exp(2)) = " + composition.getFunctionValue(2));
        
        // Сдвиг и масштабирование
        Function shiftedExp = Functions.shift(expFunc, 1, 0);
        System.out.println("exp(x-1) в точке 1 = " + shiftedExp.getFunctionValue(1));
    }
    
    private static void testInputOutput() throws IOException {
        System.out.println("\n5. Тестирование записи/чтения файлов:");
        
        // Создаем табулированную экспоненту
        Exp expFunc = new Exp();
        TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(expFunc, 0, 10, 11);
        
        // Записываем в текстовый файл
        try (FileWriter writer = new FileWriter("exp_text.txt")) {
            TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
        }
        
        // Читаем из текстового файла
        TabulatedFunction readExpText;
        try (FileReader reader = new FileReader("exp_text.txt")) {
            readExpText = TabulatedFunctions.readTabulatedFunction(reader);
        }
        
        System.out.println("Сравнение исходной и прочитанной из текстового файла:");
        for (int i = 0; i < tabulatedExp.getPointsCount(); i++) {
            double original = tabulatedExp.getPointY(i);
            double read = readExpText.getPointY(i);
            System.out.printf("Точка %d: исходная=%.6f, прочитанная=%.6f%n", i, original, read);
        }
        
        // Записываем в бинарный файл
        try (FileOutputStream fos = new FileOutputStream("exp_binary.bin")) {
            TabulatedFunctions.outputTabulatedFunction(tabulatedExp, fos);
        }
        
        // Читаем из бинарного файла
        TabulatedFunction readExpBinary;
        try (FileInputStream fis = new FileInputStream("exp_binary.bin")) {
            readExpBinary = TabulatedFunctions.inputTabulatedFunction(fis);
        }
        
        System.out.println("\nСравнение исходной и прочитанной из бинарного файла:");
        for (int i = 0; i < tabulatedExp.getPointsCount(); i++) {
            double original = tabulatedExp.getPointY(i);
            double read = readExpBinary.getPointY(i);
            System.out.printf("Точка %d: исходная=%.6f, прочитанная=%.6f%n", i, original, read);
        }
    }
    
    private static void testSerialization() throws IOException, ClassNotFoundException {
        System.out.println("\n6. Тестирование сериализации:");
        
        // Создаем функцию ln(exp(x)) = x
        Exp expFunc = new Exp();
        Log lnFunc = new Log(Math.E);
        TabulatedFunction tabulatedComposition = TabulatedFunctions.tabulate(
            Functions.composition(lnFunc, expFunc), 0, 10, 11);
        
        // Сериализация с Serializable
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("function_serializable.ser"))) {
            oos.writeObject(tabulatedComposition);
        }
        
        // Десериализация
        TabulatedFunction deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("function_serializable.ser"))) {
            deserialized = (TabulatedFunction) ois.readObject();
        }
        
        System.out.println("Сравнение после сериализации:");
        for (int i = 0; i < tabulatedComposition.getPointsCount(); i++) {
            double original = tabulatedComposition.getPointY(i);
            double after = deserialized.getPointY(i);
            System.out.printf("Точка %d: исходная=%.6f, после сериализации=%.6f%n", 
                i, original, after);
        }
    }
}