package functions.threads;

import functions.basic.*;
import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
        this.setName("Generator-Thread");
    }

 @Override
public void run() {
    Random random = new Random();
    int taskCount = task.getTaskCount();

    System.out.println(getName() + " started: " + taskCount + " tasks");

    try {
        for (int i = 0; i < taskCount && !isInterrupted(); i++) {
            try {
                // Генерируем параметры
                double base;
                do {
                    base = 1 + random.nextDouble() * 9;
                } while (Math.abs(base - 1.0) < 1e-10);
                
                Log logFunc = new Log(base);

                double leftBorder = random.nextDouble() * 100;
                double rightBorder;
                do {
                    rightBorder = leftBorder + 1 + random.nextDouble() * 100;
                } while (rightBorder <= leftBorder);

                double step;
                do {
                    step = random.nextDouble();
                } while (Math.abs(step) < 1e-10);

                if (leftBorder <= 0) {
                    leftBorder = 0.1;
                    rightBorder = Math.max(rightBorder, leftBorder + 1);
                }

                // Используем семафор для записи
                semaphore.beginWrite();
                
                try {
                    task.setFunction(logFunc);
                    task.setLeftBorder(leftBorder);
                    task.setRightBorder(rightBorder);
                    task.setStep(step);

                    System.out.printf("%s %d: Source %.6f %.6f %.6f (base=%.4f)%n", 
                                     getName(), i + 1,
                                     leftBorder, rightBorder, step, base);
                } finally {
                    semaphore.endWrite();
                }

                // Пауза
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(getName() + " interrupted during sleep");
                    break; // Выходим из цикла
                }

            } catch (InterruptedException e) {
                System.out.println(getName() + " interrupted in semaphore operation");
                break; // Выходим из цикла
            } catch (Exception e) {
                System.out.println(getName() + " error: " + e.getMessage());
                i--; // Повторяем итерацию
            }
        }
    } finally {
        System.out.println(getName() + " finished");
    }
}
}