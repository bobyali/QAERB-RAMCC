package org.cloudbus.cloudsim.examples.auction.AuctionExample5;

public class ProvidersRattings {
	
	int q1Rating= 0;
	int q2Rating= 0;
	int q3Ratting= 0;
	int q4Ratting= 0;
	public ProvidersRattings(int q1Rating, int q2Rating,
			int q3Ratting, int q4Ratting) {
		super();
		this.q1Rating = q1Rating;
		this.q2Rating = q2Rating;
		this.q3Ratting = q3Ratting;
		this.q4Ratting = q4Ratting;
	}
	public ProvidersRattings() {
		super();
		this.q1Rating = 0;
		this.q2Rating = 0;
		this.q3Ratting = 0;
		this.q4Ratting = 0;
	}
	public int getQ1Rating() {
		return q1Rating;
	}
	public void setQ1Rating(int q1Rating) {
		this.q1Rating = q1Rating;
	}
	public int getQ2Rating() {
		return q2Rating;
	}
	public void setQ2Rating(int q2Rating) {
		this.q2Rating = q2Rating;
	}
	public int getQ3Ratting() {
		return q3Ratting;
	}
	public void setQ3Ratting(int q3Ratting) {
		this.q3Ratting = q3Ratting;
	}
	public int getQ4Ratting() {
		return q4Ratting;
	}
	public void setQ4Ratting(int q4Ratting) {
		this.q4Ratting = q4Ratting;
	}
	

}
