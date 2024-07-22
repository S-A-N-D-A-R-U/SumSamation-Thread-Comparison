public class Main {

    public static void main(String[] args) throws InterruptedException {
        int start = 1;
        int end = 10_000_000;
        int numThreads = 4;

        long startTime = System.currentTimeMillis();
        long resultWithoutThreads = summationWithoutThreads(start, end);
        long endTime = System.currentTimeMillis();
        System.out.println("Summation without threads: " + resultWithoutThreads);
        System.out.println("Execution time without threads: " + (endTime - startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        long resultWithThreads = summationWithThreads(start, end, numThreads);
        endTime = System.currentTimeMillis();
        System.out.println("Summation with threads: " + resultWithThreads);
        System.out.println("Execution time with threads: " + (endTime - startTime) + " milliseconds");
    }


    public static long summationWithoutThreads(int start, int end) {
        long total = 0;
        for (int i = start; i <= end; i++) {
            total += i;
        }
        return total;
    }

    public static class SummationWorker extends Thread {
        private int start;
        private int end;
        private long result;

        public SummationWorker(int start, int end) {
            this.start = start;
            this.end = end;
            this.result = 0;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                result += i;
            }
        }

        public long getResult() {
            return result;
        }
    }

    public static long summationWithThreads(int start, int end, int numThreads) throws InterruptedException {
        SummationWorker[] workers = new SummationWorker[numThreads];
        int chunkSize = (end - start + 1) / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int threadStart = start + i * chunkSize;
            int threadEnd = (i == numThreads - 1) ? end : (threadStart + chunkSize - 1);
            workers[i] = new SummationWorker(threadStart, threadEnd);
            workers[i].start();
        }

        long total = 0;
        for (SummationWorker worker : workers) {
            worker.join();
            total += worker.getResult();
        }

        return total;
    }
}