import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class CollectionPerformanceExperiment {

    private static final int N = 10_000;
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURE_RUNS = 7;

    private static final String[] NAMES = {
            "Добавление в конец",
            "Добавление в начало",
            "Вставка в середину",
            "Доступ по индексу",
            "Удаление из начала",
            "Удаление из конца"
    };

    private static long checksum = 0;


    public static void main(String[] args) {
        System.out.println("Операция               | Элементов | ArrayList, мс | LinkedList, мс");
        System.out.println("-----------------------|-----------|---------------|---------------");

        for (int scenario = 0; scenario < NAMES.length; scenario++) {
            double arrayListTime = measure(scenario, true);
            double linkedListTime = measure(scenario, false);
            System.out.printf("%-22s | %9d | %13.3f | %14.3f%n",
                    NAMES[scenario], N, arrayListTime, linkedListTime);
        }
        System.out.println("Контрольная сумма: " + checksum);
    }


    private static double measure(int scenario, boolean useArrayList) {

        for (int i = 0; i < WARMUP_RUNS; i++) {
            List<Integer> list = prepareList(scenario, useArrayList);
            checksum += runScenario(scenario, list);
        }

        long[] times = new long[MEASURE_RUNS];
        for (int i = 0; i < MEASURE_RUNS; i++) {
            List<Integer> list = prepareList(scenario, useArrayList);

            long start = System.nanoTime();
            long result = runScenario(scenario, list);
            long end = System.nanoTime();

            checksum += result;
            times[i] = end - start;
        }

        Arrays.sort(times);
        long median = times[MEASURE_RUNS / 2];
        return median / 1_000_000.0;
    }


    private static List<Integer> prepareList(int scenario, boolean useArrayList) {
        List<Integer> list;
        if (useArrayList) {
            list = new ArrayList<>();
        } else {
            list = new LinkedList<>();
        }

        if (scenario >= 3) {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
        }
        return list;
    }


    private static long runScenario(int scenario, List<Integer> list) {
        long sum = 0;

        if (scenario == 0) {
            for (int i = 0; i < N; i++) {
                list.add(i);
            }
            sum = list.size();
        } else if (scenario == 1) {
            for (int i = 0; i < N; i++) {
                list.add(0, i);
            }
            sum = list.get(0);
        } else if (scenario == 2) {
            for (int i = 0; i < N; i++) {
                list.add(list.size() / 2, i);
            }
            sum = list.get(list.size() / 2);
        } else if (scenario == 3) {
            for (int i = 0; i < N; i++) {
                sum += list.get(i);
            }
        } else if (scenario == 4) {
            while (!list.isEmpty()) {
                sum += list.remove(0);
            }
        } else if (scenario == 5) {
            while (!list.isEmpty()) {
                sum += list.remove(list.size() - 1);
            }
        }
        return sum;
    }
}
