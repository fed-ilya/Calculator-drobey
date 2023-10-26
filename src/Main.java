import java.util.Stack;
import java.util.Scanner;
class drobi {
    private int numerator = 1; //создание переменной, в которой будет числитель
    private int denominator = 1; //создание переменной, в которой будет знаменатель

    public void set(int a, int b) { //задаёт числитель и знаменатель в наши дроби
        if (b != 0){ //обработка ошибки (0 в знаменателе)
            numerator = a;
            denominator = b;
        }
        if(b < 0){ //обработка ошибки (отрицательный знаменатель), переносим минус в числитель
            numerator = -a;
            denominator = -b;
        }
    }

    public static int get_num(drobi p){
        return p.numerator;
    }

    public static int get_det(drobi p){
        return p.denominator;
    }

    public static void print(drobi d){
        System.out.println(d.numerator+"/"+d.denominator);
    }
    public static drobi sokrach(drobi summary, int maxd) {
        for (int i = maxd; i >= 2; i--) { //поиск наибольшего общего делителя и деление на него
            if (Math.abs(summary.numerator) % i == 0 && summary.denominator % i == 0) {
                summary.numerator /= i;
                summary.denominator /= i;
            }
        }
        return summary;
    }

    public static drobi slozh(drobi a, drobi b) {
        drobi summary = new drobi();
        //приводим к общему знаменателю
        summary.numerator = a.numerator * b.denominator + b.numerator * a.denominator; //Вычисление числителя
        summary.denominator = a.denominator * b.denominator; //Вычисление знаменателя
        int maxd;
        // сокращение дроби
        if(Math.abs(summary.numerator) > summary.denominator){
            maxd = summary.denominator;
        }
        else{
            maxd = Math.abs(summary.numerator);
        }
        summary = sokrach(summary, maxd);
        return summary;
    }

    public static drobi vichit(drobi a, drobi b) {
        drobi vich = new drobi();
        vich.numerator = a.numerator * b.denominator - b.numerator * a.denominator;
        vich.denominator = a.denominator * b.denominator;
        int maxd;
        if(Math.abs(vich.numerator) > vich.denominator){
            maxd = vich.denominator;
        }
        else{
            maxd = Math.abs(vich.numerator);
        }
        vich = sokrach(vich, maxd);
        return vich;
    }

    public static drobi umnozh(drobi a, drobi b) {
        drobi umn = new drobi();
        umn.numerator = a.numerator * b.numerator;
        umn.denominator = a.denominator * b.denominator;
        int maxd;
        if(Math.abs(umn.numerator) > umn.denominator){
            maxd = umn.denominator;
        }
        else{
            maxd = Math.abs(umn.numerator);
        }
        umn = sokrach(umn,maxd);
        return umn;
    }

    public static drobi delenie(drobi a, drobi b) {
        drobi del = new drobi();
        //переварачиваем вторую дробь и умножаем
        del.numerator = a.numerator * b.denominator;
        del.denominator = a.denominator * b.numerator;
        int maxd;
        if(Math.abs(del.numerator) > del.denominator){
            maxd = del.denominator;
        }
        else{
            maxd = Math.abs(del.numerator);
        }
        del = sokrach(del, maxd);
        return del;
    }
}
///////////////////////////////////////////////////////////////////////////

class Calculator {

    public static String preparingExpression(String expression){ //Для преобразования отрицательных чисел(Было -2, стало 0-2)
        String preparedExpression = new String();
        for(int i = 0; i < expression.length(); i++){
            char symbol = expression.charAt(i);
            if (symbol == '-'){
                if (i == 0)
                    preparedExpression += "0/1"; //
                else if(expression.charAt(i-1) == '(')
                    preparedExpression += "0/1";
            }

            preparedExpression += symbol;
        }
        return preparedExpression;
    }

    private static drobi perevodvdrobi(String str){ //функция из строки делает дробь
        drobi d = new drobi(); //создание новой дроби
        String chislo = new String();
        int[] mas = new int[2]; //создаём массив, в котором будет числитель и знаменатель
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '/') {
                mas[0] = (Integer.parseInt(chislo)); //записали числитель
                chislo = new String();
            }
            else chislo += str.charAt(i);
        }
        mas[1] = (Integer.parseInt(chislo)); //записали знаменатель
        d.set(mas[0],mas[1]); //записали числ. и знам. в дробь
        return d;
    }

    public static String ExpressointoRPN(String Expr){ // метод перевода выражения в обратную польскую запись
        String current = "";
        Stack<Character> stack = new Stack<>(); //объявляем стек, состоящий из символов

        int priority;
        char posl = ' '; //сюда записываем предыдущий символ, чтобы его не "потерять"
        for(int i = 0; i < Expr.length(); i++){
            priority = getPriority(Expr.charAt(i)); //записываем приоритет символа в строке

            if(priority == 0) current += Expr.charAt(i);
            if(priority == 1) stack.push(Expr.charAt(i));

            if(priority > 1){
                if(posl != '/') { // "Не даём" пробелу после / поставиться
                    current+= ' '; //Чтобы между числами были пробелы
                }
                if(posl == '/' && Expr.charAt(i) == '-'){ //чтобы программа не принимала минус после / за вычитание
                    current += Expr.charAt(i);
                }
                else {
                    while (!stack.empty()) {
                        if (getPriority(stack.peek()) >= priority) current += stack.pop(); //случай, когда приоритет предыдущего выше
                        else break;
                    }
                    stack.push(Expr.charAt(i));
                }
            }
            if(priority == -1){
                current+= ' ';
                while(getPriority(stack.peek()) != 1) current += stack.pop(); // ищем открывающуюся скобку, всё перед ней убираем в current
                stack.pop();
            }
            posl = Expr.charAt(i);
        }
        while(!stack.empty()) current += stack.pop(); //выгружаем всё из стека в current

        return current;
    }

    public static String RPNtoAnswer(String str){ // метод перевода обратной польской записи в ответ
        //вспомогательные переменные для проверки на правильность записи выражений
        char posl_c; //Прошлый символ в строке
        char nast_c; //Настоящий символ в строке
        int posl_p; //Приоритет прошлого символа в строке
        int nast_p; //Приоритет настоящего символа в строке
        int k_otkr_scob; //Кол-во открывающихся скобочек
        int k_zakr_scob; //Кол-во закрывающихся скобочек
        int flag; //определяет, что будет выполняться в программе

        //стандартные значение для переменных
        flag = 0;
        posl_p = Calculator.getPriority(str.charAt(0));
        posl_c = str.charAt(0);
        k_otkr_scob = 0;
        k_zakr_scob = 0;

        if(posl_c == '(') k_otkr_scob++; //Проверяем первый символ, скобка ли это

        for(int i = 1; i < str.length(); i++){ //Определение корректности выражения
            nast_c = str.charAt(i);
            nast_p = Calculator.getPriority(str.charAt(i));
            if(nast_p == 1000) { //Находит символы, недопустимые в выражении
                flag = 1;
                break;
            }
            if((posl_p == 2 || posl_p == 3) && (nast_p == 2 || nast_p == 3)){ //Находит ошибку - 2 подряд операции
                flag = 1;
                break;
            }
            if(posl_c == '/' && nast_c == '0'){ //Находит ошибку - деление на 0
                flag = 2;
                break;
            }
            if(nast_c == '(') k_otkr_scob++; //считаем кол-во открывающихся скобок
            if(nast_c == ')') k_zakr_scob++; //считаем кол-во закрывающихся скобок

            posl_p = Calculator.getPriority(str.charAt(i));
            posl_c = str.charAt(i);
        }

        if(k_otkr_scob != k_zakr_scob) flag = 1; //Если кол-во откр и закр скобок не равно, то поднять флаг на ошибку

        String prepared; //Правильная запись отрицательных чисел(Вместо -2 = 0-2)
        String rpn; //Обратная Польская Нотация

        if(flag == 0) { //если ошибок не найдено, начинаем решать
            prepared = Calculator.preparingExpression(str); //вызываем функцию, добавляющую нули перед минусами
            rpn = Calculator.ExpressointoRPN(prepared); //из выражения делаем польскую запись

            String operand = new String();
            Stack<drobi> stack = new Stack(); //объявляем стек, состоящий из дробей

            char posl = ' ';

            for(int i = 0; i < rpn.length(); i++){
                if(rpn.charAt(i) == ' ') continue;

                if(getPriority(rpn.charAt(i)) == 0){ //если встретили число
                    //Записываем в строку отдельно дроби(включая минус в знаменателе)
                    while(rpn.charAt(i) != ' ' && (getPriority(rpn.charAt(i)) == 0 || (posl == '/' && rpn.charAt(i) == '-'))) {
                        operand += rpn.charAt(i);
                        posl = rpn.charAt(i);
                        i++;
                        if (i == rpn.length()) break;
                    }
                    //System.out.println(operand+"//");
                    stack.push(perevodvdrobi(operand)); //Нужно сделать функцию перевода из строчки в тип drobi
                    operand = new String();
                }

                if(getPriority(rpn.charAt(i)) > 1){
                    drobi a = stack.pop(), b = stack.pop();

                    if(rpn.charAt(i) == '+') stack.push(drobi.slozh(b,a));
                    if(rpn.charAt(i) == '-') stack.push(drobi.vichit(b,a));
                    if(rpn.charAt(i) == '*') stack.push(drobi.umnozh(b,a));
                    if(rpn.charAt(i) == ':') stack.push(drobi.delenie(b,a));
                }
            }
            drobi res = stack.pop();
            int res_num = drobi.get_num(res);
            int res_det = drobi.get_det(res);
            String res_str_num = Integer.toString(res_num); //Запись числителя в виде строки
            String res_str_det = Integer.toString(res_det); //Запись знаменателя в виде строки
            return res_str_num + '/' + res_str_det;
        }
        String flag_str = Integer.toString(flag);
        return flag_str;
    }

    public static int getPriority(char token){ //Определение приоритета символа
        if(token == '*' || token == ':') return 3;
        else if(token == '+' || token == '-') return 2;
        else if(token == '(') return 1;
        else if(token == ')') return -1;
        else if(token == '1' || token == '2' || token == '3'|| token == '4'|| token == '5'|| token == '6'|| token == '7'
                || token == '8'|| token == '9'|| token == '0' || token == '/' || token == ' ') return 0;
        else return 1000;
    }
}
///////////////////////////////////////////////////////////////////////////


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String res;

        System.out.println("\nВведите выражение, чтобы получить ответ или 'quit' для выхода из программы");
        String a = sc.nextLine(); //пользователь вводит задачу

        while (!a.equalsIgnoreCase("quit")) { //выход из программы с помощью стоп слова quit (не обращая внимания на регистр)
            res = Calculator.RPNtoAnswer(a);
            if(res.equals("1")) System.out.println("Ошибка. Некорректное выражение");

            else if(res.equals("2")) System.out.println("Ошибка. Деление на 0.");

            else System.out.println(res);

            a = sc.nextLine(); //пользователь вводит задачу
        }
        System.out.println("Завершение работы");
    }
}
