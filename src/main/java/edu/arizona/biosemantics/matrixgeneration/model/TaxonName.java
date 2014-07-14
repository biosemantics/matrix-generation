package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.LinkedList;

public class TaxonName {
	
	private LinkedList<RankData> rankData;
	private String author;
	private String date;

	public TaxonName(LinkedList<RankData> rankData, String author, String date) {
		super();
		this.rankData = rankData;
		this.author = author;
		this.date = date;
	}

	public LinkedList<RankData> getRankData() {
		return rankData;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public String toString() {
		return rankData.toString() + ":" + author + "," + date;
	}
}