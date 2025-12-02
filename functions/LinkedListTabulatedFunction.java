package functions;
import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    
    private class Node {
        FunctionPoint point;
        Node prev, next;
        Node(FunctionPoint p) { point = p; }
    }
    
    private Node head;
    private int size;
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) 
            throw new IllegalArgumentException("Invalid domain or points count");
        
        init();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) 
            addNodeToTail(new FunctionPoint(leftX + i * step, 0));
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2) 
            throw new IllegalArgumentException("Invalid domain or points count");
        
        init();
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) 
            addNodeToTail(new FunctionPoint(leftX + i * step, values[i]));
    }

    // НОВЫЙ конструктор с массивом точек (Задание 1)
    public LinkedListTabulatedFunction(FunctionPoint[] pointsArray) {
        if (pointsArray.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }
        
        // Проверка упорядоченности
        for (int i = 0; i < pointsArray.length - 1; i++) {
            if (pointsArray[i].getX() >= pointsArray[i + 1].getX()) {
                throw new IllegalArgumentException("Points must be ordered by X");
            }
        }
        
        init();
        for (FunctionPoint point : pointsArray) {
            addNodeToTail(new FunctionPoint(point));
        }
    }
    
    private void init() {
        head = new Node(null);
        head.prev = head;
        head.next = head;
        size = 0;
    }
    
    private void addNodeToTail(FunctionPoint point) {
        Node newNode = new Node(point);
        Node last = head.prev;
        
        newNode.prev = last;
        newNode.next = head;
        last.next = newNode;
        head.prev = newNode;
        size++;
    }
    
    private Node getNode(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Index: " + index);
        
        Node current = head.next;
        for (int i = 0; i < index; i++) 
            current = current.next;
        
        return current;
    }
    
    private Node addNodeAtIndex(int index, FunctionPoint point) {
        Node newNode = new Node(point);
        Node target = (index == size) ? head : getNode(index);
        
        newNode.prev = target.prev;
        newNode.next = target;
        target.prev.next = newNode;
        target.prev = newNode;
        size++;
        
        return newNode;
    }
    
    private void removeNode(int index) {
        Node toRemove = getNode(index);
        toRemove.prev.next = toRemove.next;
        toRemove.next.prev = toRemove.prev;
        size--;
    }

    @Override
    public double getLeftDomainBorder() { 
        return head.next.point.getX(); 
    }
    
    @Override
    public double getRightDomainBorder() { 
        return head.prev.point.getX(); 
    }
    
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) 
            return Double.NaN;
        
        Node current = head.next;
        for (int i = 0; i < size; i++) {
            if (Math.abs(current.point.getX() - x) < 1e-9) 
                return current.point.getY();
            current = current.next;
        }
        
        current = head.next;
        for (int i = 0; i < size - 1; i++) {
            if (x >= current.point.getX() && x <= current.next.point.getX()) {
                double x1 = current.point.getX(), y1 = current.point.getY();
                double x2 = current.next.point.getX(), y2 = current.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }
        return Double.NaN;
    }
    
    @Override
    public int getPointsCount() { return size; }
    
    @Override
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNode(index).point);
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        Node node = getNode(index);
        
        if ((index > 0 && point.getX() <= node.prev.point.getX()) || 
            (index < size - 1 && point.getX() >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException("Invalid X order");
        }
        
        node.point = new FunctionPoint(point);
    }
    
    @Override
    public double getPointX(int index) { 
        return getNode(index).point.getX(); 
    }
    
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        Node node = getNode(index);
        
        if ((index > 0 && x <= node.prev.point.getX()) || 
            (index < size - 1 && x >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException("Invalid X order");
        }
        
        node.point.setX(x);
    }
    
    @Override
    public double getPointY(int index) { 
        return getNode(index).point.getY(); 
    }
    
    @Override
    public void setPointY(int index, double y) {
        getNode(index).point.setY(y);
    }
    
    @Override
    public void deletePoint(int index) {
        if (size < 3) 
            throw new IllegalStateException("Minimum 2 points required");
        removeNode(index);
    }
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        Node current = head.next;
        int i = 0;
        
        while (i < size && current.point.getX() < point.getX()) {
            current = current.next;
            i++;
        }
        
        if (i < size && Math.abs(current.point.getX() - point.getX()) < 1e-9)
            throw new InappropriateFunctionPointException("Duplicate X coordinate");
        
        addNodeAtIndex(i, new FunctionPoint(point));
    }
}