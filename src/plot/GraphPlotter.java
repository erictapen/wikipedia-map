package plot;

import java.util.ArrayList;

import graph.GraphNode;
import graph.GraphProcessing;

public class GraphPlotter {

	private GraphNode root;
	private boolean debug;
	private NodeSetManager manager;
	private double stepsize;       
	private int maxIteration;
	private int iteration;
	private int redrawInterval;
	private double waitingCircleRadius;
	private double movingCircleRadius;
	
	private ArrayList<GraphNode> plottedNodes;
	private ArrayList<GraphNode> movingNodes;
	private ArrayList<GraphNode> waitingNodes;








	/** Also calls init()
	 * @param root
	 * @param debug
	 */
	public GraphPlotter(GraphNode root, boolean debug) {
		super();
		this.root = root;
		this.debug = debug;
		this.manager = new NodeSetManager(this);
		this.plottedNodes = new ArrayList<GraphNode>();
		this.movingNodes = new ArrayList<GraphNode>();
		this.plottedNodes = new ArrayList<GraphNode>();
		this.init();
	}

	public void init() {

		// TODO Do some initialisation stuff. In this method, everything from getting the root
		//		to a plottable Graph and a usable NodeSetManager must happen!
		root.updateNumberOfAllLeafs();
		System.out.println("root has " + root.getNumberOfAllLeafs() + " leafs.");
		this.updateSizes();
		manager.init();

		root.setxPos(0.0);
		root.setyPos(0.0);
		root.setSize(1.0);
		this.plottedNodes.add(root);



	}

	/** Here does the plotting happen.
	 * 
	 */
	public void update() {
		if(this.redrawInterval == 0) this.redrawInterval = this.maxIteration;
		if(this.iteration==0) {    //update nodeLists, get new nodes, initialize the starting circle
			ArrayList<GraphNode> temp = new ArrayList<GraphNode>();
			for(GraphNode x : this.waitingNodes) {
				temp.addAll(x.getChildren());
			}
			this.plottedNodes.addAll(movingNodes);
			this.movingNodes.clear();
			this.movingNodes.addAll(this.waitingNodes);
			this.waitingNodes.clear();
			this.waitingNodes.addAll(temp);
			for(GraphNode x : this.movingNodes) {       //doing the movingCircle
				double rad = 	Math.atan2(x.getxPos(), x.getyPos());
				x.setxPos(Math.sin(rad)*this.movingCircleRadius);
				x.setyPos(Math.cos(rad)*this.movingCircleRadius);
			}
			for(GraphNode x : this.waitingNodes) {       //doing the waitingCircle
				double rad = 	Math.atan2(x.getParent().getxPos(), x.getParent().getyPos()) + 
								Math.random()*this.stepsize - this.stepsize/2.0;
				x.setxPos(Math.sin(rad)*this.waitingCircleRadius);
				x.setyPos(Math.cos(rad)*this.waitingCircleRadius);
			}
		}



		for(int i=0; i<this.redrawInterval; i++) {

			//TODO iterate every Node in the way, the specs say
			
			this.manager.update();   //to update the node searching architecture
			iteration++;
			if(this.iteration == this.maxIteration) {    //if one round is finished

				this.iteration = 0;
				return;
			}
		}

	}

	public GraphNode getRoot() {
		return root;

	}


	/** Updates all the size fields in the tree, according to this.getSizeFromLeafs(int)
	 * @param root
	 */
	public void updateSizes() {
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		ArrayList<GraphNode> temp  = new ArrayList<GraphNode>();
		nodes.add(this.root);
		while(!nodes.isEmpty()) {
			for(GraphNode x : nodes) {
				x.setSize(this.getSizeFromLeafs(x.getNumberOfAllLeafs()));
				temp.addAll(x.getChildren());
			}
			nodes.clear();
			nodes.addAll(temp);
			temp.clear();
		}
	}

	/** Here sits the transformation function which defines, how large a GraphNode will be plotted, 
	 * compared to root. It may be necessary to define very complex functions in an external file.
	 * @param n Number of Leafs of a GraphNode
	 * @return The actual size in ] 0.0 ; 1.0 [
	 */
	private double getSizeFromLeafs(int n) {
		return (double) n / (double) root.getNumberOfAllLeafs();
	}


	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/** Stepsize must be about 10 times smaller than the smallest
	 * @param stepsize
	 */
	public void setStepsize(double stepsize) {
		this.stepsize = stepsize;
	}

	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	public void setRedrawInterval(int redrawInterval) {
		this.redrawInterval = redrawInterval;
	}

	public ArrayList<GraphNode> getPlottedNodes() {
		return plottedNodes;
	}

	public void addPlottedNode(GraphNode plottedNode) {
		this.plottedNodes.add(plottedNode);
	}

	public ArrayList<GraphNode> getMovingNodes() {
		return movingNodes;
	}

	public void addMovingNode(GraphNode movingNode) {
		this.movingNodes.add(movingNode);
	}

	public ArrayList<GraphNode> getWaitingNodes() {
		return waitingNodes;
	}

	public void addWaitingNode(GraphNode waitingNode) {
		this.waitingNodes.add(waitingNode);
	}

	public void setWaitingCircleRadius(double waitingCircleRadius) {
		this.waitingCircleRadius = waitingCircleRadius;
	}

	public void setMovingCircleRadius(double movingCircleRadius) {
		this.movingCircleRadius = movingCircleRadius;
	}


	
	
}