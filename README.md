# Binary to Decimal Converter (Java Mini Project)

This is a complete, beginner-friendly Java GUI application built with Swing. It features a modern, clean UI and allows you to convert numbers between Binary and Decimal formats.

## 📁 Project Structure

- `src/com/converter/ConverterApp.java` - The main file that builds the user interface (GUI) and handles button clicks.
- `src/com/converter/ConverterLogic.java` - A separate file that contains the core logic (the math) for converting numbers.

## 📖 Step-by-Step Explanation of the Code

### 1. `ConverterLogic.java` (The Logic)
This class has two main static methods:
*   `binaryToDecimal(String binaryStr)`: It first checks if the user entered only `0`s and `1`s using a Regular Expression (`"[01]+"`). If not, it throws a `NumberFormatException`. Otherwise, it uses Java's built-in `Long.parseLong(string, base)` method (where base is 2) to convert it to a decimal number.
*   `decimalToBinary(String decimalStr)`: It checks if the string contains only digits (`"\\d+"`). It parses it to a base-10 `Long`, and then uses `Long.toBinaryString()` to get the binary equivalent.

### 2. `ConverterApp.java` (The User Interface)
*   **JFrame & JPanel:** We extend `JFrame` to create the main window. A `JPanel` acts as a container for all our components, set to a soft gray-blue color (`new Color(245, 247, 250)`).
*   **Layout:** We use `BoxLayout` to stack elements vertically, and `FlowLayout` for panels that hold buttons horizontally.
*   **Components:** 
    *   `JRadioButton` for selecting "Binary to Decimal" or "Decimal to Binary" (grouped by `ButtonGroup` so only one is active).
    *   `JTextField` for the user to type their input.
    *   `JButton`s for "Convert" and "Clear" with custom colors to make it look modern.
    *   `JLabel` to display the Title and the Final Result.
*   **Action Listeners:** 
    *   When the "Clear" button is clicked, it empties the text field and resets the result label.
    *   When the "Convert" button is clicked, the `performConversion()` method is called. It checks the radio buttons to decide which logic to run.
*   **Error Handling (Try-Catch):** We catch the `NumberFormatException` thrown by the Logic class. If an error is caught, it turns the result label red and shows a popup alert using `JOptionPane`.

## 🚀 How to Run the Project in VS Code

1. **Install Prerequisites:** Ensure you have the **Java Development Kit (JDK)** installed on your computer, and install the **Extension Pack for Java** in VS Code.
2. **Open the Folder:** Open the main `binarytodecimal` folder in VS Code (`File > Open Folder`).
3. **Open the Main File:** In the Explorer sidebar, navigate to `src/com/converter/ConverterApp.java` and open it.
4. **Run:** Once VS Code recognizes it as a Java project (you'll see a "Run | Debug" button appear above the `public static void main` method), click **Run**.
5. Alternatively, you can run it from the terminal inside the root folder:
   ```bash
   javac src/com/converter/*.java
   java -cp src com.converter.ConverterApp
   ```

## 🧪 Sample Test Cases

Here are some test cases you can use to verify that the project works correctly:

| Test Case | Input | Selected Mode | Expected Output / Behavior |
| :--- | :--- | :--- | :--- |
| **Valid Binary** | `1010` | Binary to Decimal | `Decimal: 10` |
| **Valid Binary** | `11111111` | Binary to Decimal | `Decimal: 255` |
| **Invalid Binary** | `1020` | Binary to Decimal | Popup Error: "Invalid Binary Input! Only 0s and 1s are allowed." |
| **Valid Decimal** | `42` | Decimal to Binary | `Binary: 101010` |
| **Valid Decimal** | `255` | Decimal to Binary | `Binary: 11111111` |
| **Invalid Decimal** | `12A` | Decimal to Binary | Popup Error: "Invalid Decimal Input! Only numbers are allowed." |
| **Empty Input** | *(leave blank)* | Either | Popup Error: "Please enter a number to convert." |
