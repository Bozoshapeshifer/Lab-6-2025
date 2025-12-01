package functions.threads;
import functions.basic.*;
import java.util.Random;
import functions.*;
public class SimpleGenerator implements Runnable {
    private Task task;
    
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        int taskCount = task.getTaskCount();
        
        System.out.println("Generator started: " + taskCount + " tasks");
        
        for (int i = 0; i < taskCount; i++) {
            // Формируем параметры задачи
            double base;
            do {
                base = 1 + random.nextDouble() * 9; // [1, 10]
            } while (Math.abs(base - 1.0) < 1e-10);
            
            Function logFunction = new Log(base);
            double leftBorder = random.nextDouble() * 100; // [0, 100]
            double rightBorder = leftBorder + 10 + random.nextDouble() * 90; // [left+10, left+100]
            double step;
            do {
                step = random.nextDouble(); // [0, 1]
            } while (Math.abs(step) < 1e-10);
            
            // Синхронизированная установка всех параметров задачи
            synchronized (task) {
                task.setFunction(logFunction);
                task.setLeftBorder(leftBorder);
                task.setRightBorder(rightBorder);
                task.setStep(step);
                
                System.out.printf("Generator: Task %d - function: log(%.4f), [%.4f, %.4f], step: %.6f%n",i + 1, base, leftBorder, rightBorder, step);
            }
            
            try {
                Thread.sleep(10); // Небольшая задержка
            } catch (InterruptedException e) {
                System.out.println("Generator interrupted");
                return;
            }
        }
        
        System.out.println("Generator finished");
    }
}