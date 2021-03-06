package org.cloudbus.cloudsim.auction.auctioneer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.auction.AuctionException;
import org.cloudbus.cloudsim.auction.bid.Bid;
import org.cloudbus.cloudsim.auction.vm.VmUtils;
import org.cloudbus.cloudsim.examples.auction.AuctionExample5.AuctionExample5;

/**
 * Contains the calculation data for a Bid that is hidden from bidder view
 * @author Youness Teimoury
 * Blog: http://youness-teimoury.blogspot.com/
 * Email: youness126@gmail.com
 * Created on 2011/8/17
 */
class BidCalculation implements Comparable<BidCalculation> {

	/**
	 * The primary requested/offered bid
	 * Contains the bundle of resources and their relative amount that are requested/offered.
	 * Contains the requested/offered price for resources
	 */
	private Bid bid;

	/**
	 * The final assignments
	 * Contains the bundle of resources and their relative amount that are assigned.
	 * Contains the calculated price for assigned resources
	 */
	private Bid assignedBid;

	/**
	 * The density by which the ordering of Bids is decided
	 */
	private Double BidDensityCalculation;

	/**
	 * Used to cache ordered resources.
	 * The resources in each bid must be ordered.
	 * This flag tells if the ordering is done on the bid or not, to avoid multiple orderings.
	 * TODO: caching is not implemented yet
	 */
	//private boolean isBidOrdered;

	public BidCalculation(Bid bid) {
		this.bid = bid;
	}

	public void subtractCalculatedPrice(double price) {
		this.assignedBid.setPrice(this.assignedBid.getPrice() - price);
	}
	
	public double getCalculatedPrice() {
		if (this.assignedBid != null) {
			return this.assignedBid.getPrice();
		}
		return 0;
	}

	/**
	 * Called when a datacenter can offer some/all requested amount for a 
	 * requested VM amount by a broker   
	 * @param vm {@link Vm} 
	 * @param assignedAmount The amount of allocated VMs
	 * @param price The price which must be get/paid for the assigned VM
	 */
	void assignAllocation(Vm vm, int assignedAmount, double price) {
		if (this.assignedBid == null) {
			this.assignedBid = new Bid(this.bid.getBidderID()) {

				// Though the following override is not necessary
				@Override
				public boolean isAscOrder() {
					return false;
				}
			};
		}

		if (this.assignedBid.containsVM(vm)) { // update if exists from before
			this.assignedBid.updateVM(vm, assignedAmount + this.assignedBid.getQuantity(vm));
		} else { // create new if doesn't exist from before 
			this.assignedBid.addVM(vm, assignedAmount);
		}
		
		this.assignedBid.addPrice(price);
	}
	
	/**
	 * Called when a datacenter can offer some/all requested amount for a 
	 * requested VM amount by a broker   
	 * @param vm {@link Vm} 
	 * @param subtractAmount The amount of allocated VMs
	 * @param subtractPrice The price which must be get/paid for the assigned VM
	 */
	void releaseAllocation(Vm vm, int subtractAmount, double subtractPrice) {
		if (this.assignedBid != null && this.assignedBid.containsVM(vm)) {
			this.assignedBid.updateVM(vm, this.assignedBid.getQuantity(vm) - subtractAmount);
			this.subtractCalculatedPrice(subtractPrice);
		}
	}
	
	public List<Vm> getOrderedResources() {			
		return this.bid.getOrderedResources();
	}

	int getRemainingQuantity(Vm vm)  {
		int totalQuantity = this.bid.getQuantity(vm);
		/**
		 * If some amount is previously assigned
		 */
		if (this.assignedBid != null &&
				this.assignedBid.containsVM(vm)){
			totalQuantity = totalQuantity - this.assignedBid.getQuantity(vm);
		}
		return totalQuantity;
	}

	public Class<? extends Bid> getBidderType() {
		return this.bid.getClass();
	}

	public int getBidderID() {
		return this.bid.getBidderID();
	}

	/**
	 * Returns the density by which the ordering of Bids is decided :D
	 * TODO: Null checks are not handled in this method to detect the problems in code. 
	 Changes in the available work.
	 */
	public double getNorm() {
		if (this.BidDensityCalculation == null) {
			Map<Vm, Integer> bundle = this.bid.getResourceBundle();
			
			if (bundle != null) {
				double totalWeight = 0;
				boolean isVmDatacenterVm=false;
				int datacenterID1to7=0;
				double brokerQavg=0;
				for (Vm vm : bundle.keySet()) {
					int amount = bundle.get(vm);
					totalWeight += VmUtils.getTotalWeight(vm, amount);
					brokerQavg += ((vm.getQ1()+vm.getQ2()+vm.getQ3()+vm.getQ4())/4);
					isVmDatacenterVm=vm.isOfTypeDataCenter();
					if (isVmDatacenterVm) {
						datacenterID1to7=vm.getSerNumber();
					}
					
				}
				if (isVmDatacenterVm) {
				this.BidDensityCalculation = (this.bid.getPrice() / Math.sqrt(totalWeight)) * this.bid.getPriority();
				
				if (AuctionExample5.arrOFProvedersFRVd[datacenterID1to7-1]>0) {
					this.BidDensityCalculation=this.BidDensityCalculation/AuctionExample5.arrOFProvedersFRVd[datacenterID1to7-1];
				}
				
				}else{
					
				this.BidDensityCalculation = ((this.bid.getPrice() / Math.sqrt(totalWeight)) * this.bid.getPriority())+brokerQavg;
				}
			}
		}

		return this.BidDensityCalculation;
	}

	/**
	 * @return the average bid or unit price that a bidder has assigned for its resources
	 */
	//Calculating average price of user and providers
	
	public double getAverageBid() {
		return this.bid.getPrice() / this.bid.getTotalQuantity(); 
	}

	/**
	 * @return true if all the requests/offers of the bidder are assigned, false if did not. 
	 */
	public boolean isSatisfied() {
		if (this.bid != null) {
			Set<Vm> primaryVms = this.bid.getResources();
			if (primaryVms != null) {
				for (Vm vm : primaryVms) {
					if (this.getRemainingQuantity(vm) != 0) {							
						return false;
					}
				} // end of for (Vm primaryVm : primaryVms)
			} // end of if (primaryVms != null)
		} // end of if (this.bid != null)
		
		return true;
	}
	
	public Set<Vm> getAllocatedVMs() {
		if (this.assignedBid != null) {
			return this.assignedBid.getResources();
		}
		return null;
	}
	
	public boolean containsVm(Vm vm) {
		if (this.bid != null && this.bid.containsVM(vm)) {
			return true;
		}
		return false;
	}
	// Ascending order of all bids not for bid density calculations 
	public boolean isBidAscOrder() {
		return this.bid.isAscOrder();
	}

	//Default comparator for BidCalculation
	// Bid density  
	private Comparator<BidCalculation> normComparator = new NormComparator();
	
	@Override
	// here is bid density comparison
	public int compareTo(BidCalculation bc) {
		return this.normComparator.compare(this, bc);
	}
	
	@Override
	public String toString() {
		return this.bid.toString();
	}

}