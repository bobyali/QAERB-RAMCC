package org.cloudbus.cloudsim.auction.auctioneer;

import java.util.Comparator;

import org.cloudbus.cloudsim.auction.AuctionException;

/**
 * Default comparator for BidCalculation
 */
public class NormComparator implements Comparator<BidCalculation> {

	@Override
	public int compare(BidCalculation bc1, BidCalculation bc2) {
		if (bc1.getBidderType() != bc2.getBidderType()) {
			throw new AuctionException("Incompatible types to be ordered.");
		}

		int returnValue = 0;
		if (bc1.getNorm() < bc2.getNorm()) {
			returnValue =  -1;  // false means provider bid density list
		} else if (bc1.getNorm() > bc2.getNorm()) {
			returnValue = 1; // Return true means users bid density list
		}
		return returnValue * (bc1.isBidAscOrder()?1:-1);
	}

}