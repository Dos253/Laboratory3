
import javax.swing.table.AbstractTableModel;



public class GornerTableModel extends AbstractTableModel
{
    // Массив коэффициентов многочлена
    private final double[] coefficients;

    // Начало отрезка, на котором будет вычисляться многочлен
    private final double from;

    // Конец отрезка
    private final double to;

    // Шаг табулирования(расстояние между соседними х)
    private final double step;
    // Данные для таблицы

    public GornerTableModel(double from, double to, double step, double[] coefficients)
    {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }


    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }
    @Override
    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return (int) Math.ceil((to - from) / step) + 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        double x = from + step * row; // вычисляем текущее значение x для строки
        if (col == 0) {
            // Возвращаем значение x
            return x;
        } else if (col == 1) {
            // Вычисляем значение многочлена по схеме Горнера
            double result = 0.0;
            for (int i = coefficients.length - 1; i >= 0; i--) {
                result = result * x + coefficients[i];
            }
            return result; // Возвращаем значение многочлена
        } else if (col == 2) {
            // Проверка, содержит ли целая часть числа только четные цифры
            double polyValue = (double) getValueAt(row, 1);
            return hasOnlyEvenDigits(polyValue);
        }
        return null;
    }


    private boolean hasOnlyEvenDigits(double number) {
        // Извлекаем целую часть числа
        long integerPart = Math.abs((long) number);

        // Проверяем каждую цифру на четность
        while (integerPart > 0) {
            long digit = integerPart % 10;
            if (digit % 2 != 0) {
                return false; // Если цифра нечетная, возвращаем false
            }
            integerPart /= 10; // Переходим к следующей цифре
        }
        return true; // Если все цифры четные, возвращаем true
    }

    private boolean hasSequentialDigits(double number) {
        String numStr = String.valueOf(number).replace("-", "");
        for (int i = 0; i < numStr.length() - 2; i++) {
            if (Character.isDigit(numStr.charAt(i)) &&
                    Character.isDigit(numStr.charAt(i + 1)) &&
                    Character.isDigit(numStr.charAt(i + 2))) {
                int digit1 = numStr.charAt(i) - '0';
                int digit2 = numStr.charAt(i + 1) - '0';
                int digit3 = numStr.charAt(i + 2) - '0';
                if (digit2 == digit1 + 1 && digit3 == digit2 + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getColumnName(int col)
    {
        return switch (col) {
            case 0 -> "Значение х";
            case 1 -> "Значение многочлена";
            case 2 -> "Последовательный ряд?";
            default -> "";
        };
    }

    public Class<?> getColmnClass(int col)
    {
        if(col == 2)
        {
            return Boolean.class;
        }
        return Double.class;
    }
}