package functions;

public class FunctionPoint {
    private double x;
    private double y;

    // Конструктор с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x; //присваивает переданный x в поле x текущего объекта
        this.y = y;
    }

    // Конструктор копирования
    public FunctionPoint(FunctionPoint point) { //Создает новую точку с такими же координатами, как у переданной точки
        this.x = point.x;                   //Копирует значения x и y из существующей точки
        this.y = point.y;
    }

    // Конструктор по умолчанию (0, 0)
    public FunctionPoint() {
        this(0.0, 0.0);
    }

    // Геттер для координаты x
    public double getX() {
        return x;
    }

    // Геттер для координаты y
    public double getY() {
        return y;
    }

    // Сеттер для координаты x
    public void setX(double x) {
        this.x = x;
    }

    // Сеттер для координаты y
    public void setY(double y) {
        this.y = y;
    }
}