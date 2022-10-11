import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class NeuralLayer implements Runnable
{
	boolean LOG_INFO = false;

	private Random random = new Random();
	private int LAYER_ID;
	private int NUM_NEURONS;
	List<Neuron> NEURONS;
	List<Double> INPUT_VALUES;

	CyclicBarrier cyclicBarrier;// = new CyclicBarrier(NUM_NEURONS);


	public NeuralLayer(int layerID, List<Neuron> neurons)
	{
		this.LAYER_ID = layerID;
		this.NUM_NEURONS = neurons.size();
		this.NEURONS = neurons;

		cyclicBarrier = new CyclicBarrier(this.NUM_NEURONS);

		if (LOG_INFO) {
			System.out.println("LAYER-" + this.LAYER_ID + " created");
			System.out.println("Number of Neurons: " + this.NEURONS.size());
			/* System.out.println("LAYER-" + LAYER_ID + " created");
			for (var n : NEURONS) {
				System.out.println("Layer: " + n.LAYER_INDEX + " Neuron: " + n.NEURON_INDEX + " Weights: " + n.WEIGHTS_LIST);
				System.out.println("---------------------");
			} */
		}
	}


	@Override public void run()
	{
		String threadName = Thread.currentThread().getName();

		for (Neuron neuron : NEURONS) {
			neuron.cyclicBarrier = this.cyclicBarrier;
			neuron.INPUT_VALUES = this.INPUT_VALUES;
			Thread worker = new Thread(neuron, "Thread-" + neuron.NEURON_INDEX);
			worker.start();
		}

	}

}
