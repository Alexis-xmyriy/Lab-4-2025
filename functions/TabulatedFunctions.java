package functions;

import java.io.*;

public class TabulatedFunctions {
    // Приватный конструктор
    private TabulatedFunctions() {}
    
    // Задание 6: метод табулирования
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Tabulation interval is outside function domain");
        }
        
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    
    // Задание 7: методы ввода/вывода
    
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            dos.writeDouble(point.getX());
            dos.writeDouble(point.getY());
        }
        
        dos.flush();
    }
    
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(points);
    }
    
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.print(function.getPointsCount());
        
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            writer.print(" " + point.getX() + " " + point.getY());
        }
        
        writer.flush();
    }
    
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        
        // Читаем количество точек
        tokenizer.nextToken();
        int pointsCount = (int)tokenizer.nval;
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            
            tokenizer.nextToken();
            double y = tokenizer.nval;
            
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(points);
    }
}