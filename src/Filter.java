import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

//THis class is used to apply filters to the JTextFields inputs
public class Filter {

    /**
     * Applies the regex filter to a JTextField.
     * @param field The JTextField to apply the filter to.
     * @param regex The regex pattern to use for the filter.
     */
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

    /**
     * Applies filters to all the JTextFields in the UI.
     * @param RNField The room number JTextField.
     * @param ACField The adult count JTextField.
     * @param CCField The child count JTextField.
     * @param PField The price JTextField.
     * @param AField The adult count JTextField.
     * @param CField The child count JTextField.
     * @param FNField The first name JTextField.
     * @param LNField The last name JTextField.
     */
    public void applyFilters(JTextField RNField, JTextField ACField, JTextField CCField, JTextField PField, JTextField AField, JTextField CField, JTextField FNField, JTextField LNField) {
        String numberRegex = "\\d*";
        String decimalNumberRegex = "\\d*\\.?\\d*";
        String letterRegex = "[a-zA-Z]*";

        applyFilter(RNField, numberRegex);
        applyFilter(ACField, numberRegex);
        applyFilter(CCField, numberRegex);
        applyFilter(PField, decimalNumberRegex);
        applyFilter(AField, numberRegex);
        applyFilter(CField, numberRegex);
        applyFilter(FNField, letterRegex);
        applyFilter(LNField, letterRegex);
    }
}