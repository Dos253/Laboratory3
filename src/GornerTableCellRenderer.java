import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer
{
    private final JPanel panel = new JPanel(); // Панель для отображения ячейки

    private final JLabel label = new JLabel(); // Метка для отображения числовых значений

    private final JCheckBox checkBox = new JCheckBox(); // Чекбокс для булевых значений

    // Формат для отображения чисел
    private final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer()
    {
        // Максимальное количество дробных знаков
        formatter.setMaximumFractionDigits(10);

        // Отключение группировки
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

        // Установка компоновки панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Добавление метки на панель
        panel.add(label);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (col == 2) {
            // Третий столбец (булевое значение): используем JCheckBox
            checkBox.setSelected((Boolean) value); // Устанавливаем значение из таблицы

            return checkBox;
        } else {
            // Первые два столбца (числовые значения): используем JLabel
            String formattedDouble = formatter.format(value);
            label.setText(formattedDouble);

            // Устанавливаем цвет фона в зависимости от условий
            double x = (double) table.getValueAt(row, 0);
            double polyValue = (double) table.getValueAt(row, 1);
            if ((x >= 0 && polyValue < 0) || (x < 0 && polyValue >= 0)) {
                panel.setBackground(Color.YELLOW);
            } else {
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            }

            label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            panel.setOpaque(true);
            return panel;
        }
    }
}

