import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Neuron implements Runnable
{
	boolean LOG_INFO = true;

	private Random random = new Random();

	CyclicBarrier cyclicBarrier;

	int layerLvl;
	int ID;

	List<Double> inputValues_;
	List<Double> edgeWeights_;

	double WEIGHTED_SUM;
	private int delay_ms;

	public Neuron(int layerLevel, int neuronID){
		this.layerLvl = layerLevel;
		this.ID = neuronID;
		this.delay_ms = 5 + random.nextInt(15);
	}

	// Neuron::Run()
	@Override public void run()
	{
		double sum = 0;
		for (int i = 0; i < inputValues_.size(); i++) {
			sum += inputValues_.get(i) * edgeWeights_.get(i);

			if (LOG_INFO) {
				System.out.println("Layer/Neuron: " + this.layerLvl + "/" + this.ID + " \n\t" + inputValues_.get(i) + " * " + edgeWeights_.get(i) + " = " + inputValues_.get(i) * edgeWeights_.get(i));
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

