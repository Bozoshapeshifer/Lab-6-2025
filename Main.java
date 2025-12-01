import functions.*;
import functions.basic.*;
import functions.threads.Generator;
import java.util.concurrent.Semaphore;

import functions.threads.Integrator;
import functions.threads.SimpleGenerator;
import functions.threads.SimpleIntegrator;
import functions.threads.Task;

import java.beans.Expression;
import java.io.*;
import java.util.Random;

public class Main 
{
    public static void main(String[] args) 
    {
        Function expFunction = new Exp();
        System.out.println("Тест 1");
        double accuracy = 1e-7;
        double left = 0;
        double right = 1;
        double equivalentValue = Math.E - 1;
        double step = 0.0001;
       
        double integral = Functions.integrate(expFunction, left, right, step);
        
        System.out.printf("Вычисленный интеграл: %.10f%n", integral);
        System.out.printf("Теоретическое значение: %.10f%n", equivalentValue);
        System.out.printf("Разница: %.2e%n", Math.abs(integral - equivalentValue));
        
        if(Math.abs(integral - equivalentValue) <= accuracy)
        {
            System.out.println("Необходимая точность достигнута, интеграл найден верно");
        }
        else
        {
            System.out.println("Необходимая точность не достигнута");
        }

        System.out.println("Тест 2");
        //nonThread();

        System.out.println("Тест 3");
        //simpleThreads();

        System.out.println("Тест 4");
        complicatedThreads();
       
    }
    public static void complicatedThreads() 
    {
    
    Task task = new Task(100);
    Semaphore dataReady = new Semaphore(0);
    Semaphore dataProcessed = new Semaphore(1);
    
    
    Generator generator = new Generator(task, dataReady,dataProcessed);
    Integrator integrator = new Integrator(task,dataReady,dataProcessed);
    
    System.out.println("complicatedThreads");
    System.out.println("Всего задач: " + task.getTaskCount());
    
    
    generator.setPriority(Thread.MIN_PRIORITY);
    integrator.setPriority(Thread.MAX_PRIORITY);
    
    
    generator.start();
    integrator.start();
    
    try {
        //ждем 50 мс 
        Thread.sleep(50);
        
        // Прерываем потоки
        System.out.println("\nMain thread: Interrupting threads after 50ms...");
        generator.interrupt();
        integrator.interrupt();
        
        //завершение потоков
        generator.join();
        integrator.join();
        
    } catch (InterruptedException e) {
        System.out.println("Main thread was interrupted");
    }
    
    System.out.println("complicatedThreads()");
}


 public static void simpleThreads() {
        Task task = new Task(100);
        // Создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        // Эксперименты с приоритетами
        //одинаковые приоритеты
        generatorThread.setPriority(Thread.NORM_PRIORITY);
        integratorThread.setPriority(Thread.MAX_PRIORITY);
        //разные приоритеты_1
        // generatorThread.setPriority(Thread.MAX_PRIORITY);
        // integratorThread.setPriority(Thread.MIN_PRIORITY);
        //разные приоритеты_2
        // generatorThread.setPriority(Thread.MIN_PRIORITY);
        // integratorThread.setPriority(Thread.MAX_PRIORITY);
        
        System.out.println("Starting threads with priorities - " +"Generator: " + generatorThread.getPriority() +", Integrator: " + integratorThread.getPriority());
        
        // Запускаем потоки
        generatorThread.start();
        integratorThread.start();
        
        // Ожидаем завершения потоков
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("All threads completed");
    }
    

     public static void nonThread()
      {
        Random random = new Random();
        Task task = new Task();
        task.setTaskCount(100); 
        
        System.out.println("ПОСЛЕДОВАТЕЛЬНАЯ ОБРАБОТКА ЗАДАНИЙ");
        
        
        for (int i = 0; i < task.getTaskCount(); i++) {
            try {
                double base;
                do {
                    base = 1 + random.nextDouble() * 9; // [1, 10]
                } while (Math.abs(base - 1.0) < 1e-7); // избегаем основание = 1
                Function logFunction = new Log(base);
                task.setFunction(logFunction);
                
                
                double leftBorder = random.nextDouble() * 100; // [0, 100]
                task.setLeftBorder(leftBorder);
                
                
                double rightBorder = 100 + random.nextDouble() * 100; // [100, 200]
                task.setRightBorder(rightBorder);
                
                //Шаг дискретизации от 0 до 1
                double step;
                do {
                    step = random.nextDouble(); // [0, 1]
                } while (step == 0.0); // избегаем нулевой шаг
                task.setStep(step);
                
                //Вывод информации о задании
                System.out.printf("Source %d: %.6f %.6f %.6f%n", i + 1, task.getLeftBorder(), task.getRightBorder(), task.getStep());
                double integral = Functions.integrate(task.getFunction(), task.getLeftBorder(), task.getRightBorder(), task.getStep());
                System.out.printf("Result %d: %.6f %.6f %.6f %.6f%n",i + 1, task.getLeftBorder(), task.getRightBorder(), task.getStep(), integral);
                System.out.println("_________");
                
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка в задании " + (i + 1) + ": " + e.getMessage());
                System.out.println("Повторяем задание...");
                i--; // Повторяем это задание
            }
        }
        
        System.out.println("Все задания выполнены!");
    }
}
