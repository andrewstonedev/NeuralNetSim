import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class NeuralLayer implements Runnable
{
	boolean LOG_INFO = false;
	private Random random = new Random();

	private int layerLvl_;
	List<Neuron> neurons_;
	List<Double> inputValues_;
	private List<List<Double>> edgeWeights_;
	CyclicBarrier cyclicBarrier;
	LayerType layerType_;

	public enum LayerType
	{INPUT, INTERNAL, OUTPUT}


	public NeuralLayer(int layerLevel, LayerType layerType, List<Neuron> neurons)
	{
		this.layerLvl_ = layerLevel;
		this.layerType_ = layerType;
		this.neurons_ = neurons;

		cyclicBarrier = new CyclicBarrier(neurons_.size());

		if (LOG_INFO) {
			System.out.println("LAYER-" + this.layerLvl_ + " created");
			System.out.println("Number of Neurons: " + this.neurons_.size());
		}
	}

	public void setEdgeWeights(List<List<Double>> edgeWeights)
	{
		this.edgeWeights_ = edgeWeights;
		if (LOG_INFO) {
			System.out.println("LAYER-" + this.layerLvl_ + " edge weights:");
			for (var weights : this.edgeWeights_) {
				System.out.println("\t" + weights);
			}
		}
		TransposeEdgeWeights();

		// set weights of neurons in layer
		for (int i = 0; i < neurons_.size(); i++) {
			neurons_.get(i).edgeWeights_ = edgeWeights_.get(i);
		}
	}

	//  Transpose edge weights so weights can be assigned to Neurons
	//  for weighted-sum computation
	private void TransposeEdgeWeights()
	{
		List<List<Double>> edgeWeights_transposed = new ArrayList<>();
		double[][] transposedMtx = new double[edgeWeights_.get(0).size()][edgeWeights_.size()];

		for (int i = 0; i < edgeWeights_.size(); i++) {
			for (int j = 0; j < edgeWeights_.get(i).size(); j++) {
				transposedMtx[j][i] = edgeWeights_.get(i).get(j);
			}
		}

		List<Double> list;
		for (double[] mtx : transposedMtx) {
			list = new ArrayList<>();
			for (int n = 0; n < transposedMtx[0].length; n++) {
				list.add(mtx[n]);
			}
			edgeWeights_transposed.add(list);
		}
		this.edgeWeights_ = edgeWeights_transposed;
	}


	@Override public void run()
	{
		String threadName = Thread.currentThread().getName();

		if (this.layerType_==LayerType.INPUT)
		{

		}

		for (Neuron neuron : neurons_) {
			neuron.cyclicBarrier = this.cyclicBarrier;
			neuron.inputValues_ = this.inputValues_;
			Thread worker = new Thread(neuron, "Thread-" + neuron.ID);
			worker.start();
		}

	}

}
