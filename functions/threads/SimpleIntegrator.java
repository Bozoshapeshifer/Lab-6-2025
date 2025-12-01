package functions.threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        int tasksProcessed = 0;
        int taskCount = task.getTaskCount();
        
        System.out.println("Integrator started");
        
        while (tasksProcessed < taskCount) {
            Function function = null;
            double leftBorder, rightBorder, step;
            
            // Синхронизированное чтение всех параметров задачи
            synchronized (task) {
                function = task.getFunction();
                leftBorder = task.getLeftBorder();
                rightBorder = task.getRightBorder();
                step = task.getStep();
            }
            
            // Проверка на null для избежания NullPointerException
            if (function == null) {
                try {
                    Thread.sleep(10); // Короткая пауза если данные еще не готовы
                } catch (InterruptedException e) {
                    System.out.println("Integrator interrupted");
                    return;
                }
                continue;
            }
            
            try {
                // Используем метод integrate из класса Functions
                double integral = Functions.integrate(function, leftBorder, rightBorder, step);
                
                System.out.printf("Integrator: Task %d - [%.4f, %.4f], step: %.6f, result: %.6f%n",
                    tasksProcessed + 1, leftBorder, rightBorder, step, integral);
                
                tasksProcessed++;
                
            } catch (IllegalArgumentException e) {
                System.out.printf("Integrator: Invalid task %d - %s%n", 
                    tasksProcessed + 1, e.getMessage());
                tasksProcessed++; // Все равно считаем задачу обработанной
            }
            
            try {
                Thread.sleep(10); // Небольшая задержка
            } catch (InterruptedException e) {
                System.out.println("Integrator interrupted");
                return;
            }
        }
        
        System.out.println("Integrator finished: " + tasksProcessed + " tasks processed");
    }
}