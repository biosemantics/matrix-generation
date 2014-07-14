package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon.Rank;

public class RankData {
	private String author;
	private Rank rank;
	private String name;

	public RankData(String author, Rank rank, String name) {
		super();
		this.author = author;
		this.rank = rank;
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public Rank getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}
}