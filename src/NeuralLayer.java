import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class NeuralLayer implements Runnable
{
	boolean LOG_INFO = false;

	private Random random = new Random();

	int layerLvl_;
	private List<Neuron> neurons_;
	List<Double> inputValues_;
	private List<List<Double>> edgeWeights_;
	CyclicBarrier cyclicBarrier;
	LayerType layerType_;
	NeuralLayer nextLayerRef_;
	List<Double> computedSums_;

	public enum LayerType
	{INPUT, INTERNAL, OUTPUT}

	public NeuralLayer(int layerLevel, LayerType layerType, List<Neuron> neurons)
	{
		this.layerLvl_ = layerLevel;
		this.layerType_ = layerType;
		this.neurons_ = neurons;
		this.computedSums_ = new ArrayList<>(this.neurons_.size());
		for (int i = 0; i < this.neurons_.size(); i++) {
			computedSums_.add(i, 0.0);
		}
		this.cyclicBarrier = new CyclicBarrier(neurons_.size(), new Runnable()
		{
			@Override public void run()
			{
				System.out.print(Thread.currentThread().getName() + " Layer-" + layerLevel + " Sums: ");
				for (var sum : computedSums_) {
					System.out.print(sum + ", ");
				}
				System.out.println();
				if (layerType != LayerType.OUTPUT) {
					nextLayerRef_.inputValues_ = computedSums_;
					nextLayerRef_.run();
				} else {
					System.out.println("--------------\n\tIndex of output layer neuron with largest value: ");
					var maxOutput = computedSums_.stream().max(Double::compareTo).get();
					System.out.print("\t\tNeuronID " + computedSums_.indexOf(maxOutput) + ": value " + computedSums_.stream().max(Double::compareTo).get());
					System.out.println("\n--------------");
				}
			}
		});

		// Provide Neural layer ref to neurons which belong to this layer
		// Used for setting Neuron::weightedSum_ to NeuralLayer::computedSums_
		for (var neuron : neurons_) {
			neuron.setLayerRef_(this);
		}

		if (LOG_INFO) {
			System.out.println("LAYER-" + this.layerLvl_ + " created");
			System.out.println("Number of Neurons: " + this.neurons_.size());
		}
	}

	List<Double> getComputedSums()
	{
		return this.computedSums_;
	}

	public void setEdgeWeights(List<List<Double>> edgeWeights)
	{
		this.edgeWeights_ = edgeWeights;
		TransposeEdgeWeights();

		if (LOG_INFO) {
			System.out.println("LAYER-" + this.layerLvl_ + " edge weights:");
			for (var weights : this.edgeWeights_) {
				System.out.println("\t" + weights);
			}
		}

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

	protected void setNextLayerRef(NeuralLayer layerRef)
	{
		this.nextLayerRef_ = layerRef;
	}

	protected void setComputedSum(int neuronID, double computedSum)
	{
		this.computedSums_.set(neuronID - 1, computedSum);
	}

	@Override public void run()
	{
		if (this.layerType_ == LayerType.INPUT) {
			for (var neuron : neurons_) {
				neuron.edgeWeights_ = new ArrayList<>(Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0));
			}
		}

		for (Neuron neuron : neurons_) {
			neuron.cyclicBarrier = this.cyclicBarrier;
			neuron.inputValues_ = this.inputValues_;
			Thread worker = new Thread(neuron, "Thread-" + neuron.ID);
			worker.start();
		}
	}

}
