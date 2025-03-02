import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class ScrabbleController {

    // Declaring the GUI components as FXML
    @FXML GridPane Board;  // The 6x6 board layout
    @FXML TextField input;  // The input text field for the word
    @FXML Text output;  // The text area to display results

    // 2D array representing the 6x6 board
    String[][] board = new String[6][6];

    // Variable to store the word being checked
    private String word;

    // StringBuilder to store possible results for output
    StringBuilder possiblities;

    // Clears the board input fields when the button is clicked
    @FXML public void clear(ActionEvent event) {
        for (var node : Board.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                // Clears the text in each text field
                textField.setText("");
            }
        }
    }

    // Checks if the word exists in the board horizontally or vertically
    @FXML
    private void check(ActionEvent event) {
        int counter = 0;
        // Fill the board with the input from the text fields
        for (var node : Board.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                board[counter / 6][counter % 6] = textField.getText();
            }
            counter++;
        }

        // Get the word from the input field
        word = input.getText();
        if (!word.isEmpty()) {
            possiblities = new StringBuilder();
            output.setText("");
            // Print the board for debugging
            printBoard();
            // Check horizontally and vertically for the word
            checkHorizontal();
            checkVertical();
            output.setText(possiblities.toString());  // Display the possible results
        }
    }

    // Checks the board for the word placed horizontally
    private void checkHorizontal() {
        int letterIndex;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (word.contains(board[i][j]) && !board[i][j].isEmpty()) {
                    letterIndex = word.indexOf(board[i][j]);
                    // Check if the word fits horizontally
                    if (j - letterIndex >= 0 && j + (word.length() - letterIndex) - 1 < 6) {
                        if (hasNeighbors(word, i, j, "horizontal")) {
                            possiblities.append("Horizontally at row ").append(i + 1).append(", col ").append(j - letterIndex + 1).append("\n");
                        }
                    }
                }
            }
        }
    }

    // Checks the board for the word placed vertically
    private void checkVertical() {
        int letterIndex;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (word.contains(board[i][j]) && !board[i][j].isEmpty()) {
                    letterIndex = word.indexOf(board[i][j]);
                    // Check if the word fits vertically
                    if (i - letterIndex >= 0 && i + (word.length() - letterIndex) - 1 < 6) {
                        if (hasNeighbors(word, i, j, "vertical")) {
                            possiblities.append("Vertically at row ").append(i - letterIndex + 1).append(", col ").append(j + 1).append("\n");
                        }
                    }
                }
            }
        }
    }

    // Helper method to check if there are no neighboring letters that disrupt the word formation
    private boolean hasNeighbors(String word, int row, int col, String type) {
        if (Objects.equals(type, "horizontal")) {
            // Check for horizontal neighbors
            for (int i = -1; i < 2; i++) {
                int currentCol = col - word.indexOf(board[row][col]);
                for (int j = i == 0 ? -1 : 0; j < (i == 0 ? word.length() + 1 : word.length()); j++) {
                    int currentRow = row + i;
                    currentRow = currentRow > 0 ? (currentRow < 6 ? currentRow : 5) : 0;
                    currentCol += j;
                    currentCol = currentCol > 0 ? (currentCol < 6 ? currentCol : 5) : 0;
                    if (!board[currentRow][currentCol].isEmpty() && col != currentCol) {
                        return false; // If a neighbor is found, return false
                    }
                }
            }
            return true;
        } else if (Objects.equals(type, "vertical")) {
            // Check for vertical neighbors
            for (int j = -1; j < 2; j++) {
                int currentRow = row - word.indexOf(board[row][col]);
                for (int i = (j == 0 ? -1 : 0); i < (j == 0 ? word.length() + 1 : word.length()); i++) {
                    currentRow += i;
                    currentRow = currentRow > 0 ? (currentRow < 6 ? currentRow : 5) : 0;
                    int currentCol = col + j;
                    currentCol = currentCol > 0 ? (currentCol < 6 ? currentCol : 5) : 0;
                    if (!board[currentRow][currentCol].isEmpty() && row != currentRow) {
                        return false; // If a neighbor is found, return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    // Prints the current state of the board for debugging purposes
    private void printBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Restrict input to letters only and ensure only one character is entered per cell
    @FXML
    public void handleKeyTyped(KeyEvent event) {
        TextField sourceField = (TextField) event.getSource();
        if (!sourceField.getText().isEmpty()) {
            // Get the character typed
            char typedChar = event.getCharacter().charAt(0);
            String firstChar;

            // Restrict to only one character
            if (!sourceField.getText().isEmpty() || !Character.isLetter(typedChar)) {
                firstChar = sourceField.getText().substring(0, 1);
                if (Character.isLetter(firstChar.charAt(0))) {
                    sourceField.setText(firstChar);  // Allow only the first character
                } else {
                    sourceField.setText("");  // Clear the field if non-letter is typed
                }
            }
        }
        Board.requestFocus();  // Focus back on the board after key press
    }
}
