import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleRegistry {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Vehicle> vehicles;

    public VehicleRegistry() {
        vehicles = new ArrayList<>();

        frame = new JFrame("Evidence vozidel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[]{"Druh vozidla", "Značka vozidla", "Rok výroby", "SPZ"}, 0);
        table = new JTable(tableModel);

        JButton addButton = new JButton("Přidat vozidlo");
        addButton.addActionListener(new AddVehicleActionListener());

        JButton deleteButton = new JButton("Smazat vozidlo");
        deleteButton.addActionListener(new DeleteVehicleActionListener());

        JButton searchButton = new JButton("Vyhledat vozidlo");
        searchButton.addActionListener(new SearchVehicleActionListener());

        JButton exportButton = new JButton("Export do CSV");
        exportButton.addActionListener(new ExportToCSVActionListener());

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(exportButton);

        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddVehicleActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = JOptionPane.showInputDialog("Zadejte druh vozidla:");
            String brand = JOptionPane.showInputDialog("Zadejte značku vozidla:");
            int year = Integer.parseInt(JOptionPane.showInputDialog("Zadejte rok výroby:"));
            String spz = JOptionPane.showInputDialog("Zadejte SPZ:");

            Vehicle vehicle = new Vehicle(type, brand, year, spz);
            vehicles.add(vehicle);
            tableModel.addRow(new Object[]{type, brand, year, spz});
        }
    }

    private class DeleteVehicleActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row!= -1) {
                vehicles.remove(row);
                tableModel.removeRow(row);
            }
        }
    }

    private class SearchVehicleActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String spz = JOptionPane.showInputDialog("Zadejte SPZ:");
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getSpz().equals(spz)) {
                    JOptionPane.showMessageDialog(frame, "Vozidlo nalezeno!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Vozidlo nenalezeno!");
        }
    }

    private class ExportToCSVActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("vehicles.csv")) {
                writer.write("Druh vozidla,Značka vozidla,Rok výroby,SPZ\n");
                for (Vehicle vehicle : vehicles) {
                    writer.write(vehicle.getType() + "," + vehicle.getBrand() + "," + vehicle.getYear() + "," + vehicle.getSpz() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Export úspěšný!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu!");
            }
        }
    }

    private static class Vehicle {
        private String type;
        private String brand;
        private int year;
        private String spz;

        public Vehicle(String type, String brand, int year, String spz) {
            this.type = type;
            this.brand = brand;
            this.year = year;
            this.spz = spz;
        }

        public String getType() {
            return type;
        }

        public String getBrand() {
            return brand;
        }

        public int getYear() {
            return year;
        }

        public String getSpz() {
            return spz;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VehicleRegistry();
            }
        });
    }
}