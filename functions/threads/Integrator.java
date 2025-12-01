package functions.threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread 
{
    private Task task;
    private Semaphore semaphore;
    
    public Integrator(Task task, Semaphore semaphore) 
    {
        this.task = task;
        this.semaphore = semaphore;
        this.setName("Integrator-Thread");
    }
    
@Override
public void run() {
    int tasksProcessed = 0;
    int taskCount = task.getTaskCount();
    
    System.out.println(getName() + " started");
    
    try {
        while (tasksProcessed < taskCount && !isInterrupted()) {
            Function function = null;
            double leftBorder = 0, rightBorder = 0, step = 0;
            
            try {
                // Используем семафор для получения доступа на ЧТЕНИЕ
                semaphore.beginRead();
                
                try {
                    // Читаем все параметры задачи
                    function = task.getFunction();
                    if (function != null) {
                        leftBorder = task.getLeftBorder();
                        rightBorder = task.getRightBorder();
                        step = task.getStep();
                    }
                } finally {
                    // Всегда освобождаем семафор
                    semaphore.endRead();
                }
                
                // Если функция не установлена, ждем
                if (function == null) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        System.out.println(getName() + " interrupted while waiting for data");
                        break; // Выходим из цикла
                    }
                    continue;
                }
                
                try {
                    // Вычисляем интеграл
                    double integral = Functions.integrate(function, leftBorder, rightBorder, step);
                    
                    System.out.printf("%s: Task %d - [%.4f, %.4f], step: %.6f, result: %.6f%n",
                        getName(), tasksProcessed + 1, leftBorder, rightBorder, step, integral);
                    
                    tasksProcessed++;
                    
                } catch (IllegalArgumentException e) {
                    System.out.printf("%s: Invalid task %d - %s%n", 
                        getName(), tasksProcessed + 1, e.getMessage());
                    tasksProcessed++;
                }
                
                // Пауза между задачами
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(getName() + " interrupted between tasks");
                    break; // Выходим из цикла
                }
                
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrupted in semaphore operation");
                break; // Выходим из цикла
            }
        }
    } finally {
        System.out.println(getName() + " finished: " + tasksProcessed + " tasks processed");
    }
}
}