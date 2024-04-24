import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Filter {
    public void applyFilters(JTextField RNField, JTextField ACField, JTextField CCField, JTextField PField) {

        class NumberFilter extends DocumentFilter {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
        class DecimalNumberFilter extends DocumentFilter {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*\\.?\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*\\.?\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }

        // Create new instances of NumberFilter and DecimalNumberFilter
        NumberFilter numberFilter = new NumberFilter();
        DecimalNumberFilter decimalNumberFilter = new DecimalNumberFilter();

        // Add a document filter to the text fields
        ((AbstractDocument) RNField.getDocument()).setDocumentFilter(numberFilter);
        ((AbstractDocument) ACField.getDocument()).setDocumentFilter(numberFilter);
        ((AbstractDocument) CCField.getDocument()).setDocumentFilter(numberFilter);
        ((AbstractDocument) PField.getDocument()).setDocumentFilter(decimalNumberFilter);

    }
}
