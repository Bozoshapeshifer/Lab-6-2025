package functions.threads;

import functions.Function;
import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread 
{
    private Task task;
    private Semaphore dataReady;
    private Semaphore dataProcessed;
    
    public Integrator(Task task,Semaphore dataReady,Semaphore dataProcessed) 
    {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed=dataProcessed;
        this.setName("Integrator-Thread");
    }
    
@Override
public void run() {
    int tasksProcessed = 0;
    int taskCount = task.getTaskCount();
    
    System.out.println(getName() + " started");
    
    try {
        while (tasksProcessed < taskCount && !isInterrupted()) 
            {
            try {
                // Используем семафор для чтения     
                 dataReady.acquire();
                 if (isInterrupted()) {
                        // Освобождаем семафор для завершения другого потока
                        dataProcessed.release();
                        break;
                    }
                    Function function = task.getFunction();
                    double leftBorder = task.getLeftBorder();
                    double rightBorder = task.getRightBorder();
                    double step = task.getStep();

                
                try 
                {
                    double integral= Functions.integrate(function, leftBorder, rightBorder, step);
                    
                    
                    System.out.printf("%s: Task %d - [%.4f, %.4f], step: %.6f, result: %.6f%n",getName(), tasksProcessed + 1, leftBorder, rightBorder, step, integral);
                    
                    tasksProcessed++;
                    
                } catch (IllegalArgumentException e) {
                    System.out.printf("%s: Invalid task %d - %s%n", 
                        getName(), tasksProcessed + 1, e.getMessage());
                    tasksProcessed++;
                }

                dataProcessed.release();
                // Пауза между задачами
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(getName() + " interrupted between tasks");
                    Thread.currentThread().interrupt();
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