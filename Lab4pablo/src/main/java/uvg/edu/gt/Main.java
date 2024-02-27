package uvg.edu.gt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Solicitar al usuario la implementación deseada para el stack y la lista
        Scanner scanner = new Scanner(System.in);
        System.out.println("Elija la implementación deseada para el stack (arrayList, vector, lista): ");
        String stackImplementation = scanner.nextLine();
        System.out.println("Elija la implementación deseada para la lista (simplemente encadenada, doblemente encadenada): ");
        String listImplementation = scanner.nextLine();

        // Leer la expresión infix desde el archivo de texto
        String infixExpression = readFromFile("C:\\Users\\Pbloc\\IdeaProjects\\Lab4pablo\\src\\main\\resources\\datos.txt");

        // Convertir la expresión infix a postfix
        String postfixExpression = convertToPostfix(infixExpression);

        // Evaluar la expresión postfix
        int result = evaluatePostfix(postfixExpression);

        // Mostrar el resultado
        System.out.println("El resultado de la expresión es: " + result);
    }

    private static String readFromFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static String convertToPostfix(String infixExpression) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < infixExpression.length(); i++) {
            char c = infixExpression.charAt(i);
            if (Character.isDigit(c)) {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop(); // Eliminar el paréntesis izquierdo
            } else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
    }

    private static int evaluatePostfix(String postfixExpression) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < postfixExpression.length(); i++) {
            char c = postfixExpression.charAt(i);
            if (Character.isDigit(c)) {
                stack.push(c - '0');
            } else {
                int operand2 = stack.pop();
                int operand1 = stack.pop();
                switch (c) {
                    case '+':
                        stack.push(operand1 + operand2);
                        break;
                    case '-':
                        stack.push(operand1 - operand2);
                        break;
                    case '*':
                        stack.push(operand1 * operand2);
                        break;
                    case '/':
                        stack.push(operand1 / operand2);
                        break;
                }
            }
        }
        return stack.pop();
    }

    // Implementación básica de una pila (stack) utilizando una lista enlazada
    static class Stack<T> {
        private Node<T> top;

        public void push(T data) {
            Node<T> newNode = new Node<>(data);
            newNode.next = top;
            top = newNode;
        }

        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("La pila está vacía");
            }
            T data = top.data;
            top = top.next;
            return data;
        }

        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("La pila está vacía");
            }
            return top.data;
        }

        public boolean isEmpty() {
            return top == null;
        }
    }

    // Clase para representar un nodo en una lista enlazada
    static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
