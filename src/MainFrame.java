import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class MainFrame extends JFrame
{
    // Поля для ввода значений
    private final JTextField fromField = new JTextField("0.0", 10);
    private final JTextField toField = new JTextField("1.0", 10);
    private final JTextField stepField = new JTextField("0.1", 10);

    // Таблица для отображения результатов
    private JTable table;

    public MainFrame(double[] coefficients)
    {
        // Настройка основного окна
        setTitle("Табулирование многочлена");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создание меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);

        // Элементы меню для сохранения данных
        JMenuItem saveTextItem = new JMenuItem("Сохранить в текстовый файл");
        fileMenu.add(saveTextItem);
        JMenuItem saveBinaryItem = new JMenuItem("Сохранить данные для построения графика");
        fileMenu.add(saveBinaryItem);

        // Меню "Справка"
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(helpMenu);
        JMenuItem aboutMenuItem = new JMenuItem("О программе");
        helpMenu.add(aboutMenuItem);

        // Действие при нажатии на "О программе"
        aboutMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Автор: Смольский Илья\nГруппа: 7\nПрограмма для табулирования многочлена по схеме Горнера.",
                    "О программе",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Панель ввода параметров
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 3, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Параметры табуляции"));
        inputPanel.add(new JLabel("От:")); // Метка "От:"
        inputPanel.add(new JLabel("До:")); // Метка "До:"
        inputPanel.add(new JLabel("Шаг:")); // Метка "Шаг:"
        inputPanel.add(fromField); // Поле ввода для "От"
        inputPanel.add(toField); // Поле ввода для "До"
        inputPanel.add(stepField); // Поле ввода для "Шаг"

        // Кнопки "Вычислить" и "Очистить"
        JButton calculateButton = new JButton("Вычислить");
        JButton clearButton = new JButton("Очистить");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        // Добавление панелей в окно
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Действие при нажатии кнопки "Вычислить"
        calculateButton.addActionListener(e -> {
            try {
                // Чтение значений из текстовых полей
                double from = Double.parseDouble(fromField.getText());
                double to = Double.parseDouble(toField.getText());
                double step = Double.parseDouble(stepField.getText());

                // Проверка на корректность введённых значений
                if(from >= to)
                {
                    throw new IllegalArgumentException("Начальное значение должно быть меньше конечного");
                }

                if (step <= 0)
                {
                    throw new IllegalArgumentException("Шаг табуляции должен быть больше 0.");
                }
                // Удаление предыдущей таблицы, если она существует
                if(table != null)
                {
                    getContentPane().remove(table.getParent().getParent());
                }

                // Создание модели таблицы с вычислениями
                GornerTableModel model = new GornerTableModel(from, to, step, coefficients);
                table = new JTable(model); // Создание таблицы
                table.setDefaultRenderer(Double.class, new GornerTableCellRenderer());
                table.setDefaultRenderer(Boolean.class, new GornerTableCellRenderer());
                //table.getColumnModel().getColumn(2).setCellRenderer(new GornerTableCellRenderer());

                // Установка ширины столбцов
                table.getColumnModel().getColumn(0).setPreferredWidth(150); // x
                table.getColumnModel().getColumn(1).setPreferredWidth(200); // P(x)
                table.getColumnModel().getColumn(2).setPreferredWidth(100); // Последовательный ряд

                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                // Добавление таблицы на форму
                getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
                getContentPane().validate(); // Обновление интерфейса
            } catch (NumberFormatException ex) {
                // Обработка исключения при неверном формате числа
                JOptionPane.showMessageDialog(this,
                        "Ошибка ввода числового значения.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                // Обработка других некорректных значений
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Действие при нажатии кнопки "Очистить"
        clearButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите очистить данные?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                fromField.setText("0.0");
                toField.setText("1.0");
                stepField.setText("0.1");

                if (table != null) {
                    getContentPane().remove(table.getParent().getParent());
                    table = null;
                }

                getContentPane().revalidate();
                getContentPane().repaint();
            }
        });
    }

    public static void main(String[] args) {
        // Открываем диалог для ввода коэффициентов
        double[] coefficients = openCoefficientInputDialog();

        // Запускаем главное окно
        SwingUtilities.invokeLater(() -> new MainFrame(coefficients).setVisible(true));
    }

    private static double[] openCoefficientInputDialog() {
        // Диалоговое окно для ввода коэффициентов
        ArrayList<Double> coefficients = new ArrayList<>();
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Введите коэффициент многочлена (или оставьте пустым для завершения):",
                    "Ввод коэффициентов",
                    JOptionPane.PLAIN_MESSAGE);
            if (input == null || input.isEmpty()) {
                break; // Завершаем ввод
            }
            try {
                coefficients.add(Double.parseDouble(input));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка: Коэффициент должен быть числом.",
                        "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Если пользователь ничего не ввёл, используем коэффициенты по умолчанию
        if (coefficients.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Коэффициенты не заданы. Используются значения по умолчанию: {1.0, -2.0, 1.0}",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
            return new double[]{1.0, -2.0, 1.0};
        }

        // Преобразуем список в массив и возвращаем
        return coefficients.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
