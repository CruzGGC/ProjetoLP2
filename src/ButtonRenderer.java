import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * ButtonRenderer class is responsible for rendering the button in the table cell.
 * It extends JButton and implements TableCellRenderer interface.
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    /**
     * Returns the component used for drawing the cell. This method is used to configure the renderer appropriately before drawing.
     *
     * @param table - the JTable that is asking the renderer to draw.
     * @param value - the value of the cell to be rendered.
     * @param isSelected - true if the cell is to be rendered with the selection highlighted; otherwise false.
     * @param hasFocus - if true, render cell appropriately.
     * @param row - the row index of the cell being drawn.
     * @param column - the column index of the cell being drawn.
     * @return - the component used for drawing the cell.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

