import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Neuron implements Runnable
{
	boolean LOG_INFO = false;

	private Random random = new Random();

	CyclicBarrier cyclicBarrier;

	int layerLvl;
	int ID;
	private NeuralLayer layerRef_;

	List<Double> inputValues_;
	List<Double> edgeWeights_;

	double weightedSum_;
	private int delay_ms;

	public Neuron(int layerLevel, int neuronID)
	{
		this.layerLvl = layerLevel;
		this.ID = neuronID;
		this.delay_ms = 5 + random.nextInt(15);
	}

	void setLayerRef_(NeuralLayer layerRef)
	{
		this.layerRef_ = layerRef;
	}

	// Neuron::Run()
	@Override public void run()
	{
		if (layerRef_.layerType_ == NeuralLayer.LayerType.INPUT) {
			// set the computed weighted sum in the layer which this neuron belongs to
			layerRef_.setComputedSum(this.ID, this.inputValues_.get(this.ID - 1));

			try {
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				throw new RuntimeException(e);
			}
		} else {
			double sum = 0;
			for (int i = 0; i < inputValues_.size(); i++) {
				sum += inputValues_.get(i) * edgeWeights_.get(i);

				if (LOG_INFO) {
					System.out.println("Layer/Neuron: " + this.layerLvl + "/" + this.ID + " \n\t" + inputValues_.get(i) + " * " + edgeWeights_.get(i) + " = " + inputValues_.get(i) * edgeWeights_.get(i));
				}
			}

			weightedSum_ = sum;
			// System.out.println(Thread.currentThread().getName() + ": sum of inputs " + weightedSum_);
			System.out.println(Thread.currentThread().getName() + " [Layer-" + this.layerLvl + " Neuron-" + this.ID + "]" + " Computed Sum: " + weightedSum_);

			try {
				// Impose artificial delay
				// System.out.println(Thread.currentThread().getName() + " sleeping for " + this.delay_ms + " ms");
				Thread.sleep(delay_ms);

				// set the computed weighted sum in the layer which this neuron belongs to
				layerRef_.setComputedSum(this.ID, this.weightedSum_);

				// wait for other thread to reach barrier
				// System.out.println(Thread.currentThread().getName() + ": waiting");
				System.out.println(Thread.currentThread().getName() + " [Layer-" + this.layerLvl + " Neuron-" + this.ID + "]" + " Waiting");
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// ...
			}
			System.out.println(Thread.currentThread().getName() + " [Layer-" + this.layerLvl + " Neuron-" + this.ID + "]" + " Done");
		}
	}
}

