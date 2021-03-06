package org.cloudbus.cloudsim.auction.vm;

import org.cloudbus.cloudsim.Vm;

/**
 * VM Allocation Based on Combinatorial Double Auction
 * Provides some utilities for auction based calculations
 * 
 * @author Youness Teimoury
 * Blog: http://youness-teimoury.blogspot.com/
 * Email: youness126@gmail.com
 * Created on 2011/9/16
 */
public class VmUtils {
	/**
	 * The policy by which we calculate the weight of the vm in VM Allocation Based 
	 * on Combinatorial Double Auction
	 * Moreover, by this criteria, the VMs are ordered ascendingly in a bundle.   
	 */
	public static double getWeight(Vm vm) {
		return vm.getMips() * vm.getPesNumber(); // total number of Quantity 
	}
	
	/**
	 * The policy by which we calculate the weight of the vm in VM Allocation Based 
	 * on Combinatorial Double Auction  
	 * @param amount The amount of vm instances
	 */
	public static double getTotalWeight(Vm vm, int amount) {  //calculating total weight M
		return VmUtils.getWeight(vm) * amount;
	}

	/**
	 * Checks if the offeredVm can satisfy the requestedVm's needs or not	 * 
	 * @param offeredVM Vm
	 * @param requestedVM Vm
	 * @return boolean
	 */
	// check that the concern provider can fulfil the user request or not if not then in Auctioneer class we check it
	public static boolean canServe(Vm offeredVM, Vm requestedVM) {
		return VmUtils.getWeight(requestedVM) <= VmUtils.getWeight(offeredVM) && 
		requestedVM.getRam() <= offeredVM.getRam() && 
		requestedVM.getSize() <= offeredVM.getSize() &&
		requestedVM.getBw() <= offeredVM.getBw() &&
		requestedVM.getQ1() <= offeredVM.getQ1()&&
		requestedVM.getQ2() <= offeredVM.getQ2()&&
		requestedVM.getQ3() <= offeredVM.getQ3()&&
		requestedVM.getQ4() <= offeredVM.getQ4();
	}

}
