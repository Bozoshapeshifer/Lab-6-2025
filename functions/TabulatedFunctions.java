package functions;

import java.io.*;

public final class TabulatedFunctions {

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Проверяем, что интервал внутри области определения
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Заданный интервал выходит за границы области определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        
        return new ArrayTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        int pointCount = function.getPointsCount();
        dataOut.writeInt(pointCount);

        for (int i = 0; i < pointCount; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] data = new FunctionPoint[pointCount];
        
        for (int i = 0; i < pointCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            data[i] = new FunctionPoint(x, y);
        }
        
        return new ArrayTabulatedFunction(data);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        int pointCount = function.getPointsCount();
        writer.println(pointCount);
        
        for (int i = 0; i < pointCount; i++) {
            writer.println(function.getPointX(i));
            writer.println(function.getPointY(i));
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int itemsCount = (int) st.nval;
        FunctionPoint[] data = new FunctionPoint[itemsCount];
        
        for (int i = 0; i < itemsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            data[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(data);
    }
}