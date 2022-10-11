import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Neuron implements Runnable
{
	boolean LOG_INFO = true;

	private Random random = new Random();

	CyclicBarrier cyclicBarrier;

	int LAYER_INDEX;
	int NEURON_INDEX;

	List<Double> INPUT_VALUES;
	List<Double> WEIGHTS_LIST;

	double WEIGHTED_SUM;
	private int delay_ms;

	public Neuron(int LAYER_INDEX, int NEURON_INDEX, List<Double> WEIGHTS_LIST)
	{
		this.LAYER_INDEX = LAYER_INDEX;
		this.NEURON_INDEX = NEURON_INDEX;
		this.WEIGHTS_LIST = WEIGHTS_LIST;
		this.delay_ms = 5 + random.nextInt(15);
	}


	// Neuron::Run()
	@Override public void run()
	{
		double sum = 0;
		for (int i = 0; i < INPUT_VALUES.size(); i++) {
			sum += INPUT_VALUES.get(i) * WEIGHTS_LIST.get(i);

			if (LOG_INFO) {
				System.out.println("Layer/Index: " + this.LAYER_INDEX + "/" + this.NEURON_INDEX + " \n\t" + INPUT_VALUES.get(i) + " * " + WEIGHTS_LIST.get(i) + " = " + INPUT_VALUES.get(i) * WEIGHTS_LIST.get(i));
				// System.out.println("\t" + INPUT_VALUES.get(i) + " * " + WEIGHTS_LIST.get(i) + " = " + INPUT_VALUES.get(i) * WEIGHTS_LIST.get(i));

			}

		}

		WEIGHTED_SUM = sum;
		System.out.println(Thread.currentThread().getName() + ": sum of inputs " + WEIGHTED_SUM);

		try {
			// Impose artificial delay
			// System.out.println(Thread.currentThread().getName() + " sleeping for " + this.delay_ms + " ms");
			Thread.sleep(delay_ms);

			// wait for other thread to reach barrier
			System.out.println(Thread.currentThread().getName() + ": waiting");
			cyclicBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// ...
		}
		System.out.println(Thread.currentThread().getName() + ": done");
	}
}

