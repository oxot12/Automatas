package proyectoautomatas1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProyectoAutomatas1 extends JFrame {
    private JTextField codeTextField;
    private JButton analyzeButton;
    private JTable tokenTable;

    public ProyectoAutomatas1() {
        setTitle("Proyecto Automatas - Identificador tokex");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        codeTextField = new JTextField();
        analyzeButton = new JButton("correr");
        tokenTable = new JTable();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(codeTextField, BorderLayout.NORTH);
        panel.add(analyzeButton, BorderLayout.SOUTH);

        JScrollPane tableScrollPane = new JScrollPane(tokenTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeCode();
            }
        });

        add(panel);

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Lexema", "Patrón", "Descripción", "Accesibilidad", "Nombre de Atributo", "Tipo de Dato"}, 0);
        tokenTable.setModel(tableModel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProyectoAutomatas1();
            }
        });
    }

    private void analyzeCode() {
        String input = codeTextField.getText();
        ArrayList<Token> tokens = tokenize(input);

        DefaultTableModel tableModel = (DefaultTableModel) tokenTable.getModel();
        tableModel.setRowCount(0);

        for (Token token : tokens) {
            // No agregar espacios en blanco a la tabla
            if (!token.getLexeme().equals(" ")) {
                tableModel.addRow(new Object[]{
                    token.getLexeme(),
                    token.getPattern(),
                    token.getDescription(),
                    token.getAccessibility(),
                    token.getAttributeName(),
                    token.getDataType()
                });
            }
        }
    }

    public static ArrayList<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        String regex = "\\d+|\\w+|\\+|\\-|\\*|\\/|=|\\{|\\}|;|_";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String token = matcher.group();
            TokenType type = getTokenType(token);
            tokens.add(new Token(token, type));
        }

        return tokens;
    }

    public static TokenType getTokenType(String token) {
        if (token.matches("\\d+")) {
            return TokenType.INTEGER;
        } else if (token.matches("\\w+")) {
            return isReservedWord(token) ? TokenType.RESERVED_WORD : TokenType.IDENTIFIER;
        } else {
            switch (token) {
                case "+":
                    return TokenType.PLUS;
                case "-":
                    return TokenType.MINUS;
                case "*":
                    return TokenType.MULTIPLY;
                case "/":
                    return TokenType.DIVIDE;
                case "=":
                    return TokenType.ASSIGN;
                case "{":
                    return TokenType.OPEN_BRACE;
                case "}":
                    return TokenType.CLOSE_BRACE;
                case ";":
                    return TokenType.SEMICOLON; // Indicar que es un punto y coma
                case "_":
                    return TokenType.UNDERSCORE; // Indicar que es un guion bajo
                default:
                    return TokenType.SYMBOL; // Indicar que es un símbolo
            }
        }
    }

    public static boolean isReservedWord(String token) {
        String[] reservedWords = {
            "public", "private", "static", "class", "int", "String", "string", "get", "set", "boolean", "system", "out", "println", "void"
        };
        for (String word : reservedWords) {
            if (word.equals(token)) {
                return true;
            }
        }
        return false;
    }
}

enum TokenType {
    INTEGER, IDENTIFIER, PLUS, MINUS, MULTIPLY, DIVIDE, ASSIGN, RESERVED_WORD, UNKNOWN, OPEN_BRACE, CLOSE_BRACE, SYMBOL, SEMICOLON, UNDERSCORE
}

class Token {
    private String lexeme;
    private TokenType type;

    public Token(String lexeme, TokenType type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getPattern() {
        return getPatternForTokenType(type);
    }

    public String getDescription() {
        return getDescriptionForTokenType(type);
    }

    public String getAccessibility() {
        return getAccessibilityForTokenType(type);
    }

    public String getAttributeName() {
        return getAttributeNameForTokenType(type);
    }

    public String getDataType() {
        return getDataTypeForTokenType(type);
    }

    private static String getPatternForTokenType(TokenType type) {
        switch (type) {
            case INTEGER:
                return "\\d+";
            case IDENTIFIER:
            case RESERVED_WORD:
            case SYMBOL:
            case SEMICOLON:
            case UNDERSCORE:
                return "Palabra Reservada";
            case PLUS:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case ASSIGN:
            case OPEN_BRACE:
            case CLOSE_BRACE:
                return "Símbolo";
            default:
                return "No se encontró un patrón para el token.";
        }
    }

    private static String getDescriptionForTokenType(TokenType type) {
        switch (type) {
            case INTEGER:
                return "Número entero";
            case IDENTIFIER:
                return "Identificador";
            case RESERVED_WORD:
                return "Palabra reservada";
            case SYMBOL:
            case SEMICOLON:
            case UNDERSCORE:
                return "Símbolo";
            case PLUS:
                return "Operador de suma";
            case MINUS:
                return "Operador de resta";
            case MULTIPLY:
                return "Operador de multiplicación";
            case DIVIDE:
                return "Operador de división";
            case ASSIGN:
                return "Operador de asignación";
            case OPEN_BRACE:
                return "Símbolo de apertura de llave";
            case CLOSE_BRACE:
                return "Símbolo de cierre de llave";
            default:
                return "Descripción no disponible.";
        }
    }

    private static String getAccessibilityForTokenType(TokenType type) {
        switch (type) {
            case INTEGER:
            case IDENTIFIER:
            case RESERVED_WORD:
            case SYMBOL:
            case SEMICOLON:
            case UNDERSCORE:
            case OPEN_BRACE:
            case CLOSE_BRACE:
                return "Pública";
            default:
                return "No se aplica";
        }
    }

    private static String getAttributeNameForTokenType(TokenType type) {
        switch (type) {
            case IDENTIFIER:
            case RESERVED_WORD:
            case SYMBOL:
            case SEMICOLON:
            case UNDERSCORE:
            case OPEN_BRACE:
            case CLOSE_BRACE:
                return "nombreclase";
            default:
                return "No se aplica";
        }
    }

    private static String getDataTypeForTokenType(TokenType type) {
        switch (type) {
            case SYMBOL:
            case SEMICOLON:
            case UNDERSCORE:
                return "char";
            case IDENTIFIER:
            case RESERVED_WORD:
            case OPEN_BRACE:
            case CLOSE_BRACE:
                return "String";
            default:
                return "No se aplica";
        }
    }
}


