import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Filter {
    private void applyFilter(JTextField field, String regex) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches(regex)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    public void applyFilters(JTextField RNField, JTextField ACField, JTextField CCField, JTextField PField) {
        String numberRegex = "\\d*";
        String decimalNumberRegex = "\\d*\\.?\\d*";

        applyFilter(RNField, numberRegex);
        applyFilter(ACField, numberRegex);
        applyFilter(CCField, numberRegex);
        applyFilter(PField, decimalNumberRegex);
    }
}